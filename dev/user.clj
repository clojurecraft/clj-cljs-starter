(ns user
  (:require
    [app.server.db :as db]
    [system]
    [juxt.clip.repl :refer [start stop reset set-init! system]]))

(set-init! system/dev-system)

#_(start)
#_(stop)

#_(db/-init! (:db system))
#_(db/insert-task! (:db system)
    {:task/id #uuid "b80e4e92-1120-4b8a-bee9-fa91ee4d49e1"
     :task/description "test"
     :task/done? true})
#_(db/update-task! (:db system)
    {:task/id #uuid "b80e4e92-1120-4b8a-bee9-fa91ee4d49e1"
     :task/description "xtest"
     :task/done? true})
#_(db/task-exists? (:db system)
    {:task/id #uuid "b80e4e92-1120-4b8a-bee9-fa91ee4d49e1"})
#_(db/all-tasks
    (:db system))
