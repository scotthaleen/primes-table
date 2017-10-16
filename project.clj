(require '[clojure.string :as s]
         '[clojure.java.shell :refer [sh]])

(def VERSION "0.1.0")

(def short-hash
  (try
    (let [{:keys [out exit]}
          (sh "git" "rev-parse" "--short" "HEAD")]
      (if (= 0 exit) (s/trim out) ""))
    (catch Exception e
      (println "WARNING: error occured parsing git revision"))))


(defproject primes-table VERSION
  :description "Application that prints multiplication tables for primes"
  :dependencies [
                 [org.clojure/clojure "1.9.0-beta2"]
                 [com.stuartsierra/component "0.3.2"]
                 [org.clojure/tools.cli "0.3.5"]]
  :main io.scotthaleen.primes.table.main
  :target-path "target/%s"
  :global-vars {*warn-on-reflection* true}
  :test-paths ["test"]
  :profiles {:dev [:project/dev :aot/test]
             :uberjar [:project/uberjar]
             :test {:dependencies [[org.clojure/test.check "0.10.0-alpha2"]]}
             :lint [:project/lint :aot/test]
             :doc {:plugins [[lein-codox "0.10.3"]] }
             :aot/test {:aot [io.scotthaleen.primes.table.system-exit-exception]}
             :project/dev {
                           :dependencies [[reloaded.repl "0.2.3"]
                                          [org.clojure/tools.namespace "0.2.11"]
                                          [org.clojure/tools.nrepl "0.2.12"]
                                          [eftest "0.3.1"]
                                          [org.clojure/test.check "0.10.0-alpha2"]]
                           :source-paths ["dev"]
                           :jvm-opts ["-Dclojure.spec.check-asserts=true"]
                           :repl-options {:init-ns user}}
             :project/lint {:plugins [[jonase/eastwood "0.2.5"]]}
             :project/uberjar {
                               :aot :all
                               :uberjar-name ~(str
                                               "primes-table-%s"
                                               (if (not-empty short-hash) (str "-" short-hash))
                                               "-standalone.jar")}}
  :aliases {"lint" ["with-profile" "lint" "eastwood"]
            "travis-ci" ["do" ["clean"] ["lint"] ["test"] ["uberjar"]]})
