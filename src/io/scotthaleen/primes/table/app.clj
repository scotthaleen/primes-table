(ns io.scotthaleen.primes.table.app
  (:require [com.stuartsierra.component :as component]
            [io.scotthaleen.primes.table.multiplication-table :as t])
  (:import [io.scotthaleen.primes.table.multiplication_table MultiplicationTable])
  (:gen-class))

(set! *warn-on-reflection* true)


(defn- calc-cell-size
  "calculate cell size by checking the length of the last element in the table"
  [padding table]
  (->
   ((comp count str last last) table)
   (+ (* 2 padding))))


(defn- format-cell
  "format value to string size"
  [size value]
  (format (str "%" size "s") value))


(defmethod clojure.core/print-method MultiplicationTable
  [mt ^java.io.Writer writer]
  (let [xs (vec (t/header mt))
        table (t/table mt)
        size (calc-cell-size 1 table)
        fmt (partial format-cell size)
        header (map fmt xs)]
    ;; blank cell
    (.write writer (str (fmt "")))

    ;; header row
    (doseq [s header]
      (.write writer ^String (fmt s)))
    (.write writer (str \newline))
    (.flush writer)

    (doseq [s (map-indexed #(vector %1 %2) table)]
      (.write writer ^String (fmt (nth xs (first s))))
      (doseq [x (second s)]
        (.write writer ^String (fmt x)))
      (.write writer (str \newline))
      (.flush writer))))


(defprotocol IApp
  (run [_] "Run app"))


(defrecord App [multiplication-table]
  component/Lifecycle
  (start [component]
    (run component)
    component)
  (stop [component]
    component)
  IApp
  (run [_] (print multiplication-table)))


(defn construct-app []
  (map->App {}))

