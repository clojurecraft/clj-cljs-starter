(ns app.client.ui.tasks-page
  (:require
    [re-frame.core :refer [subscribe dispatch]]
    [goog.object :as o]
    [app.client.router :as router]))

(defn new-task-view []
  [:form {:on-submit (fn [e]
                       (.preventDefault e)
                       (dispatch [:create-task!
                                  {:task/id (random-uuid)
                                   :task/description (-> e .-target .-elements
                                                         (o/get "description")
                                                         .-value)
                                   :task/done? false}]))}
   [:input {:type "text"
            :class "border rounded"
            :name "description"}]
   [:button {:class "bg-gray-200 hover:bg-gray-400"} "+"]])

(defn task-view
  [task]
  [:div.task
   [:input {:type "checkbox"
            :checked (:task/done? task)
            :on-change (fn [_]
                         (if (:task/done? task)
                           (dispatch [:uncomplete-task! (:task/id task)])
                           (dispatch [:complete-task! (:task/id task)])))}]
   [:input {:type "text"
            :class "border rounded"
            :value (:task/description task)
            :on-change (fn [e]
                         (dispatch [:change-description! (:task/id task)
                                    (.. e -target -value)]))}]
   [:a {:href (router/path-for [:task {:id (:task/id task)}])} "..."]])

(defn tasks-view []
  [:div.tasks
   (for [task @(subscribe [:tasks])]
     ^{:key (:task/id task)}
     [task-view task])])

(defn tasks-page-view []
  [:div
   [tasks-view]
   [new-task-view]])
