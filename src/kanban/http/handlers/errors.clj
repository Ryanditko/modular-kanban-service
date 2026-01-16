(ns kanban.http.handlers.errors
  (:require
   [kanban.http.responses :as resp]))

(defn not-found-handler [_]
  (resp/not-found {:error "not found"}))

(defn method-not-allowed-handler [_]
  (resp/method-not-allowed {:error "method not allowed"}))