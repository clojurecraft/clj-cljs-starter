(ns app.server.db
  (:require
    [hikari-cp.core :as hikari]
    [hugsql.core :as hugsql]
    [next.jdbc.result-set :as rs]
    [hugsql.adapter.next-jdbc :as next-adapter]))

(defn start! [opts]
  (hugsql/set-adapter! (next-adapter/hugsql-adapter-next-jdbc
                         {:builder-fn rs/as-lower-maps}))
  (hikari/make-datasource opts))

(defn stop! [datasource]
  (hikari/close-datasource datasource))

(hugsql/def-db-fns "app/server/sql/init.sql")
(hugsql/def-db-fns "app/server/sql/queries.sql")
(hugsql/def-db-fns "app/server/sql/commands.sql")

