(ns app.server.spa
  (:require
    [hiccup.core :as hiccup]))

(defn index-html []
  (hiccup/html
    [:html
     [:head
      [:title "App"]
      [:link {:rel "stylesheet"
              :href "/css/styles.css"
              :media "screen"}]]
     [:body
      [:div#app
       [:div#message {:style "display: flex; justify-content: center; align-items: center; height: 100%"}
        "This app requires Javascript. Please enable Javascript in your browser."]]
      [:script {:type "text/javascript"}
       "document.getElementById('message').outerHTML= '';"]
      [:script {:type "text/javascript"
                :src "/js/app.js"}]
      [:script {:type "text/javascript"}
       (str "app.client.core._main();")]]]))
