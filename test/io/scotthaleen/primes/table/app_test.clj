(ns io.scotthaleen.primes.table.app-test
  (:require [clojure.test :refer :all]
            [clojure.string :as sz]
            [com.stuartsierra.component :as component]
            [io.scotthaleen.primes.table.app :refer [run ->App]]
            [io.scotthaleen.primes.table.multiplication-table :as mt])
  (:import  [io.scotthaleen.primes.table.primes IPrimes]))


(def expect-stdout
  (str "     2  3" \newline
       "  2  4  6" \newline
       "  3  6  9" \newline))


(def mock-primes
  (reify IPrimes
    (prime-numbers [_] '(2  3))))


(def sample-MT (component/start (mt/->MultiplicationTable mock-primes)))


(deftest test-app-run
  (let [app (->App sample-MT)]
    (testing "test app run"
      (is (= expect-stdout (with-out-str (component/start app)))))))

