(ns system
  (:require
    [hawk.core :as hawk]
    [girouette.processor]
    [figwheel.main.api :as repl-api]
    [app.server.system]))

(defn figwheel-config [env]
  (case env
    :dev
    {:id "dev"
     :config {:connect-url "ws://[[client-hostname]]:[[server-port]]/figwheel-connect"
              :mode :serve
              :open-url false
              :watch-dirs ["src"]
              :css-dirs ["resources/public/css"]}
     :options {:main "app.client.core"
               :output-to "resources/public/js/app.js"
               :asset-path "/js/dev"
               :output-dir "resources/public/js/dev"
               :closure-defines {"goog.DEBUG" true}
               :parallel-build true}}
    :prod
    {:id "prod"
     :options {:main "app.client.core"
               :output-to "resources/public/js/app.js"
               :output-dir "target/cljs-prod"
               :closure-defines {"goog.DEBUG" false}
               :optimizations :advanced
               :parallel-build true
               :infer-externs true
               :static-fns true
               :fn-invoke-direct true}}))

(def dev-system
  (fn []
    {:components
     (merge (:components (app.server.system/system-config :dev))
            {:figwheel {:start
                        `(repl-api/start (figwheel-config :dev))
                        :stop (fn [_] (repl-api/stop-all))}
             :girouette {:start
                         `(girouette.processor/process
                            {:css {:output-file "resources/public/css/styles.css"}
                             :verbose? false
                             :input {:file-filters [".cljs" ".cljc"]}
                             :watch? true})
                         :stop `hawk/stop!}})}))
