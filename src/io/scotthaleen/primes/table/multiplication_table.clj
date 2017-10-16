(ns io.scotthaleen.primes.table.multiplication-table
  (:require [com.stuartsierra.component :as component]
            [io.scotthaleen.primes.table.primes :refer [prime-numbers]]))


(defn- build-table
  "Generate table data"
  [xs]
  (partition
   (count xs)
   (for [x xs
         y xs]
     (* x y))))


(defprotocol IMultiplicationTable
  (table [this] "multiplication table of primes")
  (header [this] "headers"))


(defrecord MultiplicationTable [primes]
  component/Lifecycle
  (start [component]
    (as-> component <>
        (assoc <> ::primes (prime-numbers primes))
        (assoc <> ::table (build-table (::primes <>)))))
  (stop [component]
    (dissoc component ::table ::primes))
  IMultiplicationTable
  (table [component] (::table component))
  (header [component] (::primes component)))


(defn construct-multiplication-table []
  (map->MultiplicationTable {}))
