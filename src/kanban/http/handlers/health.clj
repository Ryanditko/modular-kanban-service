(ns kanban.http.handlers.health
  (:require [kanban.http.responses :as resp]))

(defn handler [_]
  (resp/ok {:message "ok"}))