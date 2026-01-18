(ns kanban.core
  (:require
   [ring.adapter.jetty :as jetty]
   [kanban.http.routes :refer [app-routes]]
   [kanban.infra.middleware :refer [wrap-http]]
   [kanban.db.core :as db])
  (:gen-class))

(def PORT 3000)

(defn -main [& _]
  (let [{:keys [datasource]} (db/start)
        app (wrap-http app-routes)
        server (jetty/run-jetty app {:port PORT :join? false})]
    
    (println "Server runnning on port" PORT)
    
    (.addShutdownHook
     (Runtime/getRuntime)
     (Thread.
      (fn []
        (println "\nShutting down...")
        (.stop server)
        (.destroy server)
        (db/stop datasource))))))
