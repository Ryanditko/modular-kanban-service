(ns kanban.http.handlers.cards
  (:require [kanban.http.responses :as resp]
            [kanban.domain.cards :as domain]
            [kanban.http.schemas.cards :as schemas]))

(defn list-cards [req]
  (let [status (get-in req [:query-params "status"])]
    (resp/ok {:cards (if status
                       (domain/list-cards status)
                       (domain/list-cards))})))

(defn get-cards-by-status [_req]
  (resp/ok (domain/get-board)))

(defn create-card [req]
  (try
    (let [body (:body-params req)
          datasource (:db req)]
      (schemas/validate! schemas/CreateCardRequest body)
      (let [card (domain/create-card! datasource body)]
        (resp/created {:message "Card created successfully"
                       :card card})))
    (catch clojure.lang.ExceptionInfo e
      (case (:type (ex-data e))
        :validation-error (resp/general-error {:error "Validation failed"
                                               :errors (ex-data e)})
        :invalid-status (resp/general-error {:error "Invalid status"
                                             :errors (ex-data e)}) 
        (resp/internal-server-error {:error "Internal server error"
                                     :message (.getMessage e)})))
    (catch Exception e
      (resp/internal-server-error {:error "Unexpected error"
                                   :message (.getMessage e)}))))

(defn get-card [req]
  (let [id (get-in req [:path-params :id])
        card (domain/get-card id)]
    (if card
      (resp/ok {:card card})
      (resp/not-found {:error "Card not found"
                       :id id}))))

(defn update-card [req]
  (let [id (get-in req [:path-params :id])
        updates (:body-params req)]
    (try
      (schemas/validate! schemas/UpdateCardRequest updates)
      (let [card (domain/update-card! id updates)]
        (resp/ok {:message "Card updated successfully"
                  :card card}))
      (catch clojure.lang.ExceptionInfo e
        (case (:type (ex-data e))
          :validation-error
          {:status 400
           :body {:error "Validation failed"
                  :details (:errors (ex-data e))}}

          :not-found
          (resp/not-found {:error "Card not found" :id id})

          {:status 500
           :body {:error "Internal server error"
                  :message (.getMessage e)}}))
      (catch Exception e
        {:status 500
         :body {:error "Unexpected error"
                :message (.getMessage e)}}))))

(defn delete-card [req]
  (let [id (get-in req [:path-params :id])]
    (if (domain/delete-card! id)
      {:status 204
       :body nil}
      (resp/not-found {:error "Card not found"
                       :id id}))))

(defn move-card [req]
  (let [id (get-in req [:path-params :id])
        body (:body-params req)]
    (try
      (schemas/validate! schemas/MoveCardRequest body)
      (let [new-status (:status body)
            card (domain/move-card! id new-status)]
        (resp/ok {:message "Card moved successfully"
                  :card card}))
      (catch clojure.lang.ExceptionInfo e
        (case (:type (ex-data e))
          :validation-error
          {:status 400
           :body {:error "Validation failed"
                  :details (:errors (ex-data e))}}

          :not-found
          (resp/not-found {:error "Card not found" :id id})

          {:status 500
           :body {:error "Internal server error"
                  :message (.getMessage e)}}))
      (catch Exception e
        {:status 500
         :body {:error "Unexpected error"
                :message (.getMessage e)}}))))
