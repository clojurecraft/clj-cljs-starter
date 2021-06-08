-- :name -init!
-- :command :execute
BEGIN;

CREATE TABLE IF NOT EXISTS task (
  "id" uuid PRIMARY KEY,
  "description" varchar NOT NULL,
  "done?" boolean NOT NULL
);

COMMIT;
