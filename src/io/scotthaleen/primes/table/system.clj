(ns io.scotthaleen.primes.table.system
  (:require [com.stuartsierra.component :as component]
            [io.scotthaleen.primes.table.primes :refer [construct-prime]]
            [io.scotthaleen.primes.table.multiplication-table
             :refer [construct-multiplication-table]]
            [io.scotthaleen.primes.table.app :refer [construct-app]]))


(def ^{:private true}
  defaults {:n 10})


(defn new-system [opts]
  (let [config (merge defaults opts)]
    (->
     (component/system-map
      :primes (construct-prime (:n config))
      :multiplication-table (construct-multiplication-table)
      :app (construct-app))
     (component/system-using
      {:multiplication-table {:primes :primes}
       :app {:multiplication-table :multiplication-table}}))))

