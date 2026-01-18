(ns kanban.http.responses)

(defn ok [body]
  {:status 200
   :body body})

(defn created [body]
  {:status 201
   :body body})

(defn general-error [body]
  {:status 400
   :body body})

(defn not-found [body]
  {:status 404
   :body body})

(defn method-not-allowed [body]
  {:status 405
   :body body})

(defn internal-server-error [body]
  {:status 500
   :body body})