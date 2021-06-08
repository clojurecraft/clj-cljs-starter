-- :name insert-task! :!
INSERT INTO task ("id", "description", "done?")
VALUES (:task/id, :task/description, :task/done?);

-- :name update-task! :!
UPDATE task
SET
  "id" = :task/id,
  "description" = :task/description,
  "done?" = :task/done?
WHERE
  "id" = :task/id;
