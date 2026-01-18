(ns kanban.domain.cards-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [kanban.domain.cards :as cards]))

(use-fixtures :each
  (fn [f]
    (cards/clear-all!)
    (f)))

;; TODO: Implement business logic tests
;; - Create, list, update, delete cards
;; - Filter cards by status
;; - Validate status transitions
;; - Test business rules
