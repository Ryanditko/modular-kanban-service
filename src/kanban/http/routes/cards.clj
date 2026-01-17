(ns kanban.http.routes.cards
  (:require [kanban.http.handlers.cards :as cards]))

(def routes
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
                       :summary "Remove a card"}}]])