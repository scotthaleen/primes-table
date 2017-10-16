(ns io.scotthaleen.primes.table.system-exit-exception
  (:gen-class
   :name io.scotthaleen.primes.table.SystemExitException
   :extends java.lang.Exception
   :state state
   :init init
   :methods [[exitcode [] Integer]]
   :constructors {[Integer] []}))


(defn -init [^Integer exitcode]
  [[] exitcode])


(defn ^Integer -exitcode [this]
  (.state ^io.scotthaleen.primes.table.SystemExitException this))

