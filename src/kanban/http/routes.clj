(ns kanban.http.routes
  (:require
   [reitit.ring :as ring]
   [kanban.http.routes.endpoints :as app-routes]
   [kanban.http.handlers.errors :as errors]))

(def app-routes
  (ring/ring-handler
   (ring/router app-routes/routes)
   (ring/create-default-handler
    {:not-found errors/not-found-handler
     :method-not-allowed errors/method-not-allowed-handler})))