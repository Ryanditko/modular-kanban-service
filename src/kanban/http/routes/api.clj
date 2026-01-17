(ns kanban.http.routes.api
  (:require
   [kanban.http.routes.health :as health]
   [kanban.http.routes.cards :as cards]))

(def routes 
  ["/api"
    health/routes
    cards/routes])