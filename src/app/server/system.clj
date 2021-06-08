(ns app.server.system
  (:require
    [org.httpkit.server :as http]
    [app.server.db :as db]
    [app.server.handler :refer [handler]]
    [app.server.routes :refer [routes]]))

(defn system-config
  [env]
  {:components
   {:db {:start `(db/start! {:jdbc-url "jdbc:h2:./data/dev"})
         :stop `db/stop!}
    :routes {:start `(routes {:db (clip/ref :db)})}
    :handler {:start `(handler (clip/ref :routes))}
    :http {:start `(http/run-server (clip/ref :handler)
                                    {:port 8080})
           :post-start `(println "Started http-server on port 8080...")
           :stop '(this :timeout 500)}}})
