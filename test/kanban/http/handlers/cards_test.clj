(ns kanban.http.handlers.cards-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [ring.mock.request :as mock]
            [kanban.http.handlers.cards :as handlers]
            [kanban.domain.cards :as cards]))

(use-fixtures :each
  (fn [f]
    (cards/clear-all!)
    (f)))

;; TODO: Implement HTTP handlers tests
;; - Test endpoints: GET, POST, PUT, DELETE
;; - Test status codes (200, 201, 404, 400)
;; - Test input validation
;; - Test JSON response format

