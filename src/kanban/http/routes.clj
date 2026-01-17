(ns kanban.http.routes
  (:require
   [reitit.ring :as ring]
   [kanban.http.routes.api :as api]
   [kanban.http.handlers.errors :as errors]))

(def app-routes
  (ring/ring-handler
   (ring/router
    [api/routes])
   (ring/create-default-handler
    {:not-found errors/not-found-handler
     :method-not-allowed errors/method-not-allowed-handler})))