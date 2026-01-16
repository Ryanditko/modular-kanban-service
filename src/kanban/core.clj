(ns kanban.core
  (:require
   [ring.adapter.jetty :as jetty]
   [kanban.http.routes :as routes]
   [kanban.infra.middleware :refer [wrap-http]])
  (:gen-class))

(def PORT 3000)

(defn -main [& _]
  (let [app (wrap-http routes/app-routes)]
    (println "Server runnning on port" PORT)
    (jetty/run-jetty app {:port PORT :join? false}))) 

