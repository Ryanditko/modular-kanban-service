(ns kanban.domain.cards)

(defonce cards-db (atom {}))

(def valid-statuses #{"todo" "doing" "done"})

(defn valid-status? [status]
  (contains? valid-statuses status))

(defn- generate-id []
  (str (java.util.UUID/randomUUID)))

(defn- current-timestamp []
  (str (java.time.Instant/now)))

(defn create-card! [{:keys [title description status]}]
  ;; Schema validation ensures status is present and valid
  (let [id (generate-id)
        now (current-timestamp)
        card {:id id
              :title title
              :description (or description "")
              :status status
              :created-at now
              :updated-at now}]
    (swap! cards-db assoc id card)
    card))

(defn get-card [id]
  (get @cards-db id))

(defn list-cards
  ([] (vals @cards-db))
  ([status]
   (if status
     (filter #(= (:status %) status) (vals @cards-db))
     (vals @cards-db))))

(defn update-card! [id updates]
  (when-not (get-card id)
    (throw (ex-info "Card not found" {:type :not-found :id id})))
  (when-let [new-status (:status updates)]
    (when-not (valid-status? new-status)
      (throw (ex-info "Invalid status" {:type :invalid-status
                                        :status new-status
                                        :valid-statuses valid-statuses}))))
  (let [current-card (get-card id)
        updated-card (-> current-card
                         (merge updates)
                         (assoc :id id)
                         (assoc :updated-at (current-timestamp)))]
    (swap! cards-db assoc id updated-card)
    updated-card))

(defn delete-card! [id]
  (if (get-card id)
    (do (swap! cards-db dissoc id) true)
    false))

(defn move-card! [id new-status]
  (when-not (valid-status? new-status)
    (throw (ex-info "Invalid status" {:type :invalid-status
                                      :status new-status
                                      :valid-statuses valid-statuses})))
  (update-card! id {:status new-status}))

(defn get-board []
  {:todo (list-cards "todo")
   :doing (list-cards "doing")
   :done (list-cards "done")})

(defn count-by-status []
  {:todo (count (list-cards "todo"))
   :doing (count (list-cards "doing"))
   :done (count (list-cards "done"))})

(defn clear-all! []
  (reset! cards-db {}))

(defn seed-data! []
  (clear-all!)
  (create-card! {:title "Implement login" :description "Create auth screen and API" :status "todo"})
  (create-card! {:title "Fix header bug" :description "Header not showing on mobile" :status "doing"})
  (create-card! {:title "Deploy to production" :description "Deploy v1.0" :status "done"})
  (println "Sample data created!")
  (get-board))
