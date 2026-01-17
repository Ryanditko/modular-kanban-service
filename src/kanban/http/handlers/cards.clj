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
    (let [body (:body-params req)]
      (schemas/validate! schemas/CreateCardRequest body)
      (let [card (domain/create-card! body)]
        {:status 201
         :body {:message "Card created successfully"
                :card card}}))
    (catch clojure.lang.ExceptionInfo e
      (if (= :validation-error (:type (ex-data e)))
        {:status 400
         :body {:error "Validation failed"
                :details (:errors (ex-data e))}}
        {:status 500
         :body {:error "Internal server error"
                :message (.getMessage e)}}))
    (catch Exception e
      {:status 400
       :body {:error "Failed to create card"
              :message (.getMessage e)}})))

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
        (let [error-type (:type (ex-data e))]
          (cond
            (= :validation-error error-type)
            {:status 400
             :body {:error "Validation failed"
                    :details (:errors (ex-data e))}}

            (= :not-found error-type)
            (resp/not-found {:error "Card not found" :id id})

            :else
            {:status 500
             :body {:error "Internal server error"
                    :message (.getMessage e)}})))
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
        (let [error-type (:type (ex-data e))]
          (cond
            (= :validation-error error-type)
            {:status 400
             :body {:error "Validation failed"
                    :details (:errors (ex-data e))}}

            (= :not-found error-type)
            (resp/not-found {:error "Card not found" :id id})

            :else
            {:status 500
             :body {:error "Internal server error"
                    :message (.getMessage e)}})))
      (catch Exception e
        {:status 500
         :body {:error "Unexpected error"
                :message (.getMessage e)}}))))
