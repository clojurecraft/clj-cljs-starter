(ns app.core
  (:gen-class)
  (:require
    [juxt.clip.core :as clip]
    [app.server.system :refer [system-config]]))

(defonce system (atom nil))

(defn -main
  [& _]
  (let [system-config (system-config :prod)]
    (reset! system (clip/start system-config))
    (.addShutdownHook
      (Runtime/getRuntime)
      (Thread. #(clip/stop system-config @system))))
  @(promise))
