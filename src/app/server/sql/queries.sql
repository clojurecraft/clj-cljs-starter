-- :name all-tasks :? :*
SELECT * FROM task;

-- :name task-exists? :? :1
SELECT "id"
FROM task
WHERE
"id" = :task/id

-- :name task :? :1
SELECT * FROM task WHERE "id" = :task/id;
