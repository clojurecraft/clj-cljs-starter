(ns app.client.ui.task-page
  (:require
    [app.client.router :as router]
    [re-frame.core :refer [subscribe]]))

(defn task-page-view
  [[_ {:keys [id]}]]
  [:div
   (let [task @(subscribe [:task id])]
     (:task/description task))

   [:a {:href (router/path-for [:tasks])} "Back"]])
