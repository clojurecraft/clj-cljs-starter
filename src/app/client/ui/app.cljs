(ns app.client.ui.app
  (:require
    [app.client.router :as router]))

(defn app-view []
  [router/current-page-view])
