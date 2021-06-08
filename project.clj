(defproject clj-cljs-starter "0.0.1"
  :dependencies [;; back-end
                 [org.clojure/clojure "1.10.0"]
                 [juxt/clip "0.23.0"]
                 [http-kit "2.5.3"]
                 [metosin/muuntaja "0.6.8"]
                 [hiccup "1.0.5"]
                 [com.layerware/hugsql "0.5.1"]
                 [com.layerware/hugsql-adapter-next-jdbc "0.5.1"]
                 [com.h2database/h2 "1.4.199"]
                 [hikari-cp "2.13.0"]

                 ;; both
                 [metosin/reitit "0.5.13"]

                 ;; front-end
                 [org.clojure/clojurescript "1.10.866"]
                 [re-frame "1.2.0"]
                 [reagent "1.1.0"]
                 [cljsjs/react "17.0.2-0"]
                 [cljsjs/react-dom "17.0.2-0"]
                 [lambdaisland/fetch "1.0.33"]
                 [venantius/accountant "0.2.5"]]

  :main app.core

  :profiles
  {:uberjar {:aot :all}
   :dev
   {:repl-options {:init-ns user}
    :source-paths ["src" "dev"]
    :dependencies
    [[com.bhauman/figwheel-main "0.2.13"]
     [girouette/girouette "0.0.3"]
     [girouette/processor "0.0.2"]]}})
