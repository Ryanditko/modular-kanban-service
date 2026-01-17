(ns kanban.infra.middleware
  (:require [jsonista.core :as json]
            [ring.middleware.params :refer [wrap-params]]))

(def mapper (json/object-mapper {:encode-key-fn name
                                 :decode-key-fn keyword}))

(defn wrap-json-body [handler]
  (fn [request]
    (if (and (:body request)
             (= "application/json" (get-in request [:headers "content-type"])))
      (let [body-str (slurp (:body request))
            body-data (when (not-empty body-str)
                        (json/read-value body-str mapper))]
        (handler (assoc request :body-params body-data)))
      (handler request))))

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
      wrap-params
      wrap-json-body
      wrap-json-response))