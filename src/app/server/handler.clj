(ns app.server.handler
  (:require
    [muuntaja.core :as m]
    [reitit.ring :as ring]
    [reitit.coercion.malli]
    [reitit.ring.coercion :as rrc]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.parameters :as parameters]
    [app.server.spa :as spa]))

(defn handler [routes]
  (ring/ring-handler
    (ring/router
      routes
      {:data {:coercion reitit.coercion.malli/coercion
              :muuntaja m/instance
              :middleware [parameters/parameters-middleware
                           muuntaja/format-negotiate-middleware
                           muuntaja/format-request-middleware
                           rrc/coerce-request-middleware
                           muuntaja/format-response-middleware]}})
    (ring/routes
      (ring/create-resource-handler {:path "/"
                                     :index-files []})
      (fn [_]
        {:status 200
         :body (spa/index-html)})
      (ring/create-default-handler))))

