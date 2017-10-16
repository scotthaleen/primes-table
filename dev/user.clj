(ns ^:no-doc user
  (:require
   [clojure.repl :refer :all]
   [clojure.java.io :as io]
   [clojure.pprint :refer [pprint]]
   [clojure.tools.namespace.repl :refer [refresh set-refresh-dirs]]
   [com.stuartsierra.component :as component]
   [eftest.runner :as eftest]
   [reloaded.repl :refer [system init start stop go reset reset-all]]
   [clojure.spec.alpha :as s]
   [io.scotthaleen.primes.table.system :as system]))

(ns-unmap *ns* 'test)


(defn test []
  (eftest/run-tests (eftest/find-tests "test") {:multithread? false}))


(when (io/resource "local.clj")
  (load "local"))


(reloaded.repl/set-init! #(system/new-system {}))


(defmacro ellapsed [form]
  `(do
     (let [start# (. System (nanoTime))
           res# ~form]
       (/ (double (- (. System (nanoTime)) start#)) 1000000.0))))


(defn conform!
  ([spec data] (conform! spec data nil))
  ([spec data context]
   (when-not (s/valid? spec data)
     (throw (ex-info "Data did not match spec"
                     (merge (s/explain-data spec data)
                            (when context {::context context})))))
   data))
