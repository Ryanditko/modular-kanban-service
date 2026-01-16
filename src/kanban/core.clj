(ns kanban.core
  (:require
   [ring.adapter.jetty :as jetty])
  (:gen-class))

(def PORT 3000)

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body "Hello World"})

(defn -main [& _]
  (println "Server runnning on port" PORT)
  (jetty/run-jetty handler {:port PORT :join? false}))
