(ns app.server.routes
  (:require
    [app.server.db :as db]))

(def Task
  [:map
   [:task/id uuid?]
   [:task/description string?]
   [:task/done? boolean?]])

(defn routes
  [{:keys [db]}]
  [["/api"
    ["/tasks"
     ["" {:put {:parameters
                {:body Task}
                :handler
                (fn [{{task :body} :parameters}]
                  (if (db/task-exists? db task)
                    (db/update-task! db task)
                    (db/insert-task! db task))
                  {:status 200})}
          :get {:responses {200 [:map
                                 [:body [:vector Task]]]}
                :handler
                (fn [_]
                  {:status 200
                   :body (db/all-tasks db)})}}]
     ["/:id"
      {:get {:parameters {:path [:map [:id uuid?]]}
             :responses {200 [:map
                              [:body Task]]}
             :handler
             (fn [{{{id :id} :path} :parameters}]
               {:status 200
                :body (db/task db {:task/id id})})}}]]]])


