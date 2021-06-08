(ns app.client.state
  (:require
    [app.client.ajax]
    [re-frame.core :refer [reg-sub reg-event-fx dispatch]]))

(defn key-by [f coll]
  (zipmap (map f coll)
          coll))

(reg-event-fx
  :initialize!
  (fn [{db :db} _]
    {:db (assoc db :db/tasks {})}))

(reg-event-fx
  :fetch-tasks!
  (fn [_ _]
    {:fetch {:url "/api/tasks"
             :then ::store-tasks!}}))

(reg-event-fx
  :fetch-task!
  (fn [_ [_ id]]
    {:fetch {:url (str "/api/tasks/" id)
             :then (fn [task]
                    (dispatch [::store-tasks! [task]]))}}))

(reg-event-fx
  ::store-tasks!
  (fn [{db :db} [_ data]]
    {:db (assoc db :db/tasks (key-by :task/id data))}))

(reg-event-fx
  :complete-task!
  (fn [{db :db} [_ task-id]]
    {:db (assoc-in db [:db/tasks task-id :task/done?] true)
     :dispatch [::persist-task! task-id]}))

(reg-event-fx
  :uncomplete-task!
  (fn [{db :db} [_ task-id]]
    {:db (assoc-in db [:db/tasks task-id :task/done?] false)
     :dispatch [::persist-task! task-id]}))

(reg-event-fx
  :change-description!
  (fn [{db :db} [_ task-id description]]
    {:db (assoc-in db [:db/tasks task-id :task/description] description)
     :dispatch [::persist-task! task-id]}))

(reg-event-fx
  :create-task!
  (fn [{db :db} [_ task]]
    {:db (assoc-in db [:db/tasks (:task/id task)] task)
     :dispatch [::persist-task! (:task/id task)]}))

(reg-event-fx
  ::persist-task!
  (fn [{db :db} [_ task-id]]
    {:fetch {:url "/api/tasks"
             :method :put
             :body (get-in db [:db/tasks task-id])}}))

(reg-sub
  :tasks
  (fn [db _]
    (vals (:db/tasks db))))

(reg-sub
  :task
  (fn [db [_ id]]
    (get-in db [:db/tasks id])))
