(ns io.scotthaleen.primes.table.multiplication-table-test
  (:require [com.stuartsierra.component :as component]
            [io.scotthaleen.primes.table.multiplication-table :as mt]
            [io.scotthaleen.primes.table.primes :as primes]
            [clojure.test :refer :all])
  (:import  [io.scotthaleen.primes.table.primes IPrimes]))


(def input-primes '(2  3  5  7  11  13  17  19  23  29))


(def mock-primes
  (reify IPrimes
    (prime-numbers [_] input-primes)))


(deftest test-multiplication-table
  (let [table (component/start (mt/->MultiplicationTable mock-primes))
        expect (map * (repeat (first input-primes)) input-primes)
        expect2 (map * (repeat (last input-primes)) input-primes)]
    (testing "header is corret list of primes"
      (is (= input-primes (mt/header table))))
    (testing "table multiplication is correct"
      (is (= expect (first (mt/table table))))
      (is (= expect2 (last (mt/table table)))))))

