(ns app.client.pages
  (:require
    [re-frame.core :refer [dispatch]]
    [app.client.ui.tasks-page :refer [tasks-page-view]]
    [app.client.ui.task-page :refer [task-page-view]]))

(def pages
  [{:page/id :tasks
    :page/view #'tasks-page-view
    :page/path "/"
    :page/on-enter! (fn [_]
                      (dispatch [:fetch-tasks!]))}
   {:page/id :task
    :page/view #'task-page-view
    :page/path "/task/:id"
    :page/parameters [:map
                      [:id uuid?]]
    :page/on-enter! (fn [[_ {id :id}]]
                      (dispatch [:fetch-task! id]))}])
