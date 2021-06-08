(ns app.client.router
  ";; call at some point to initialize:

   (initialize! [{:page/id :home
                  :page/view #'some-view-fn
                  :page/path \"/\"}
                 {:page/id :profile
                  :page/view (fn [[page-id {:keys [id]}]] ...)
                  :page/path \"/profile/:id\"
                  :page/parameters {:id integer?}}
                 ...])

   ;; include a reagent-view somewhere:
   [current-page-view]

   ;; to generate link string:
   (path-for [:profile {:id 5}])

   ;; to force navigation to some page
   (navigate-to! [:profile {:id 5}])

   ;; to check if some page is active
   (active? [:profile])
   (active? [:profile {:id 5}])
  "
  (:require
    [clojure.set :as set]
    [clojure.string :as string]
    [reagent.core :as r]
    [reitit.core :as reitit]
    [reitit.coercion :as coercion]
    [reitit.coercion.malli]
    [reitit.impl]
    [malli.core :as m]
    [accountant.core :as accountant])
  (:import
    (goog Uri)))

(defonce initialized? (atom false))
(defonce router (atom nil))
(defonce current-page (r/atom nil))

(defn coerce-to-string [o]
  (cond
    (keyword? o)
    (name o)

    :else
    (str o)))

(defn url-encode [value]
  (js/encodeURIComponent (string/replace (coerce-to-string value) "%20" "+")))

(defn params->string
  "Given map of params (allowing array values), returns a query string;
   Keys are sorted, and values within a key are sorted.
   (sorting is done to improve cacheability)"
  [params]
  (->> params
       sort
       (mapcat (fn [[k v]]
                 (cond
                   (coll? v)
                   (for [v' (sort v)]
                     (str (name k) "[]" "=" (url-encode v')))
                   :else
                   [(str (name k) "=" (url-encode v))])))
       (string/join "&")))

(defn url->params
  "Given goog.Uri, read query parameters into Clojure map."
  [^Uri uri]
  (let [q (.getQueryData uri)]
    (->> q
         (.getKeys)
         distinct
         (map (fn [k]
                ;; TODO this is fragile, can do better
                (if (string/ends-with? k "[]")
                  [(-> k
                       (string/replace "[]" "")
                       keyword)
                   (vec (.getValues q k))]
                  [(keyword k) (aget (.getValues q k) 0)])))
         (into {}))))

(defn classify-parameters
  "/a/:bar   {:bar __, :foo ___}
  =>
  {:path {:bar ___}, :query {:foo ___}}

  (assumes any params not in path are query params)"
  [path parameters]
  (let [children (if parameters (m/children parameters) [])
        all-param-keys (set (map first children))
        path-param-keys (:path-params (reitit.impl/parse path {}))
        query-param-keys (set/difference all-param-keys path-param-keys)]
    {:path (into [:map] (->> children
                             (filter (fn [[k _]]
                                       (contains? path-param-keys k)))))
     :query (into [:map] (->> children
                              (filter (fn [[k _]]
                                       (contains? query-param-keys k)))))}))

(defn ->args [current-page]
  [(get-in current-page [:data :config :page/id])
   (merge (get-in current-page [:coerced-parameters :query])
          (get-in current-page [:coerced-parameters :path]))])

(defn initialize!
  "Expects a list of pages, each a map with the following keys:
     :page/id           keyword, used in path-for

     :page/view         reagent view fn (recommend using #'view-fn)
                        receives params of page

     :page/path         string defining path
                        may include param patterns in path ex. /foo/:id
                        (params must also be included in :page/parameters)

     :page/parameters   map, malli coercion of path
                        any parameters not included in :page/path are assumed to be query-params

     :page/on-enter!    fn to call when page is navigated to
                        (right after reagent state is updated with new page-id)
                        receives params (of new page): [page-id params]

     :page/on-exit!     fn to call when page is navigated away from
                        receives params (of old page): [page-id params]"
  [pages]
  (when-not @initialized?
    (reset! initialized? true)

    (reset! router (reitit/router
                     (->> pages
                          (map (fn [page]
                                 [(page :page/path)
                                  {:name (page :page/id)
                                   :coercion reitit.coercion.malli/coercion
                                   :parameters (classify-parameters (page :page/path) (page :page/parameters))
                                   :config page}])))
                     {:compile coercion/compile-request-coercers}))

    (accountant/configure-navigation!
      {:nav-handler (fn [path]
                      (when-let [on-exit! (get-in @current-page [:data :config :page/on-exit!])]
                        (on-exit! (->args @current-page)))

                      (let [uri (.parse Uri path)]
                        (when-let [match (reitit/match-by-path @router (.getPath uri))]
                          (let [match (assoc match :query-params (url->params uri))]
                            (reset! current-page (assoc match :coerced-parameters (coercion/coerce! match)))
                            (when-let [on-enter! (get-in @current-page [:data :config :page/on-enter!])]
                              (on-enter! (->args @current-page)))))))
       :path-exists? (fn [path]
                       (let [uri (.parse Uri path)]
                         (boolean (reitit/match-by-path @router (.getPath uri)))))})
    (accountant/dispatch-current!)))

(defn current-page-view []
  (when-let [view (get-in @current-page [:data :config :page/view])]
    [view (->args @current-page)]))

(defn path-for
  [[page-id params]]
  (let [match (reitit/match-by-name @router page-id params)
        query-params (->> (get-in match [:data :parameters :query])
                          (m/children)
                          (map first)
                          (select-keys params))]
    (str (:path match)
         (when (seq query-params)
           (let [query (params->string query-params)]
             (when-not (string/blank? query)
               (str "?" query)))))))

(defn navigate-to!
  [[page-id params]]
  (accountant/navigate! (path-for [page-id params])))

(defn active?
  [[page-id parameters]]
  (and
    (= page-id
       (get-in @current-page [:data :config :page/id]))
    (if parameters
      (= parameters
         (select-keys (merge (get-in @current-page [:coerced-parameters :query])
                             (get-in @current-page [:coerced-parameters :path]))
                      (keys parameters)))
      true)))

