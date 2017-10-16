(ns io.scotthaleen.primes.table.system-test
  (:require
   [clojure.test :refer :all]
   [io.scotthaleen.primes.table.system :as sys]
   [com.stuartsierra.component :as component]
   [io.scotthaleen.primes.table
    [primes :refer [IPrimes]]
    [multiplication-table :refer [IMultiplicationTable]]
    [app :refer [IApp]]]))


(def expect
  {:primes (partial satisfies? IPrimes)
   :multiplication-table (partial satisfies? IMultiplicationTable)
   :app (partial satisfies? IApp)})


(deftest test-system-construct
  (let [system (sys/new-system {})]
    (testing "test new system construction"
      (is
       (= (set (keys expect))
          (set (keys system))))
      (is (every? true?
           (map  (fn [[k v]] ((k expect) v)) system))))))

