(ns kanban.http.routes.health
  (:require [kanban.http.handlers.health :as health]))

(def routes
  ["/health" {:get {:handler health/handler
                    :summary "Health check endpoint"}}])