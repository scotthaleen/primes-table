(ns io.scotthaleen.primes.table.primes
  (:require [com.stuartsierra.component :as component]))


(def ^{:private true
       :doc "Square Function"}
  square (fn [x] (* x x)))


(defn- gen-inputs
  "Lazy seq of numbers used to check a prime
   Shortcutted to start with 2 and only increment
   on odds"
  ([] (cons 2 (gen-inputs 3)))
  ([n] (lazy-seq (cons n (gen-inputs (+ 2 n))))))


(defn- inputs
  "Inputs for testing a prime, numbers less than √n"
  [n]
  (take-while #(<= (square %) n) (gen-inputs)))


(defn- is-prime
  "checks for prime if every modulo is not 0"
  [n]
  (every? #(pos? (mod n %)) (inputs n)))


(def ^{:private true
       :doc "lazy sequence of prime numbers"}
  primes-seq
  (for [n (gen-inputs)
        :when (is-prime n)]
    n))


(defprotocol IPrimes
  (prime-numbers [this] "return list of primes"))


(defrecord Primes [n]
  component/Lifecycle
  (start [component]
    (assoc component ::prime-numbers (take n primes-seq)))
  (stop [component]
    (dissoc component ::prime-numbers))
  IPrimes
  (prime-numbers [component] (::prime-numbers component)))


(defn construct-prime
  "creates a component for IPrimes"
  [n]
  (map->Primes {:n n}))
