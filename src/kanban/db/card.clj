(ns kanban.db.card
  (:require [next.jdbc.sql :as sql]))

(defn insert-task [db card]
  (sql/insert! db :card card))