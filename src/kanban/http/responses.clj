(ns kanban.http.responses)

(defn ok [body]
  {:status 200
   :body body})

(defn not-found [body]
  {:status 404
   :body body})

(defn method-not-allowed [body]
  {:status 405
   :body body})
