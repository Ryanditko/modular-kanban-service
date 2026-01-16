(ns kanban.infra.middleware
  (:require [jsonista.core :as json]))

(def mapper (json/object-mapper {:encode-key-fn name}))

(defn wrap-json-response [handler]
  (fn [request]
    (let [response (handler request)]
      (if (:body response)
        (-> response
            (update :body #(json/write-value-as-string % mapper))
            (assoc-in [:headers "Content-Type"] "application/json"))
        response))))

(defn wrap-http [handler]
  (-> handler
      wrap-json-response))