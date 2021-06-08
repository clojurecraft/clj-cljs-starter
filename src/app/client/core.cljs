(ns ^:figwheel-hooks app.client.core
  (:require
    [reagent.dom :as rdom]
    [re-frame.core :refer [dispatch-sync]]
    [app.client.router :as router]
    [app.client.pages :refer [pages]]
    [app.client.state] ;; loads re-frame subs and events
    [app.client.ui.app :refer [app-view]]))

(defn render
  []
  (rdom/render
    [app-view]
    (js/document.getElementById "app")))

(defn -main []
  (dispatch-sync [:initialize!])
  (router/initialize! pages)
  (render))

(defn ^:after-load reload
  []
  (render))


