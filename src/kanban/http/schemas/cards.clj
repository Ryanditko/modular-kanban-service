(ns kanban.http.schemas.cards
  (:require [malli.core :as m]
            [malli.error :as me]))

(def CardStatus
  [:enum "todo" "doing" "done"])

(def CreateCardRequest
  [:map
   [:title [:string {:min 1 :max 200}]]
   [:description {:optional true} [:string {:max 1000}]]
   [:status {:optional true} CardStatus]])

(def UpdateCardRequest
  [:map
   [:title {:optional true} [:string {:min 1 :max 200}]]
   [:description {:optional true} [:string {:max 1000}]]
   [:status {:optional true} CardStatus]])

(def MoveCardRequest
  [:map
   [:status CardStatus]])

(defn validate! [schema data]
  (when-not (m/validate schema data)
    (let [explained (m/explain schema data)
          errors (me/humanize explained)]
      (throw (ex-info "Validation failed"
                      {:type :validation-error
                       :errors errors})))))
