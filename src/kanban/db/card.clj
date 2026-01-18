(ns kanban.db.card
  (:require [next.jdbc.sql :as sql]))

(defn find-all-cards [db]
  (sql/query db ["SELECT * FROM card"]))

(defn find-cards-by-status [db status]
  (sql/query db ["SELECT * FROM card
                  WHERE status = ?", status]))

(defn insert-card [db card]
  (sql/insert! db :card card))