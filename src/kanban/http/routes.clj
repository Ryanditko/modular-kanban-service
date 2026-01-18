(ns kanban.http.routes
  (:require
   [reitit.ring :as ring]
   [kanban.http.routes.api :as api]
   [kanban.http.handlers.errors :as errors]
   [reitit.ring.middleware.parameters :as parameters]
   [reitit.ring.coercion :as coercion]
   [reitit.coercion.malli :as malli]))

(def app-routes
  (ring/ring-handler
   (ring/router
    [api/routes]
    {:data {:coercion malli/coercion
            :middleware [parameters/parameters-middleware
                         coercion/coerce-request-middleware]}})
   (ring/create-default-handler
    {:not-found errors/not-found-handler
     :method-not-allowed errors/method-not-allowed-handler})))