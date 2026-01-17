(ns kanban.http.routes.endpoints
  (:require
   [kanban.http.handlers.health :as health]
   [kanban.http.handlers.cards :as cards]))

(def routes 
  [["/api"
    ["/health" {:get {:handler health/handler
                      :summary "Health check endpoint"}}]

    ["/cards"
     ["" {:get {:handler cards/list-cards
                :summary "List all cards"}
          :post {:handler cards/create-card
                 :summary "Create a new card"}}]

     ["/:id" {:get {:handler cards/get-card
                    :summary "Get a card by ID"}
              :put {:handler cards/update-card
                    :summary "Update a card"}
              :delete {:handler cards/delete-card
                       :summary "Remove a card"}}]]]])