(ns kanban.migrations 
  (:require
   [kanban.db.core :as db]
   [migratus.core :as migratus]))

(defn -main [& _]
  (println "Running DB migrations...")
  (migratus/migrate (db/read-config "migratus.edn"))
  (println "Migrations finished"))