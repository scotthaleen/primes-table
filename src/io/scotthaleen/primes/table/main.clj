(ns io.scotthaleen.primes.table.main
  (:require
   [clojure.string :as sz]
   [clojure.tools.cli :refer [parse-opts]]
   [io.scotthaleen.primes.table.system :as sys]
   [com.stuartsierra.component :as component]
   [io.scotthaleen.primes.table.runtime :as runtime])
  (:gen-class))

(set! *warn-on-reflection* true)


(def ^:const program "primes-table-*-standalone.jar")


(def cli-options
  [["-n" "--num-primes N" "number of primes to generate"
    :default 10
    :id :n
    :parse-fn #(Integer/parseInt %)
    :validate [#(int? %) "Must be a number"]]
   ["-h" "--help"]])


(defn usage [options-summary]
  (->>
   [(str "Usage: " program " [Options]")
    "Options:"
    options-summary]
   (sz/join \newline)))


(defn parsing-error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (sz/join \newline errors)))


(defn system-exit!
  "Wrap System/exit so it can be redef'd for testing"
  [status]
  (System/exit status))

(defn exit!
  "Exit code helper"
  ([] (exit! 0))
  ([status] (system-exit! status))
  ([status msg]
   (do
     (println msg))
   (system-exit! status)))


(defn -main
  [& args]
  (parse-opts args cli-options)
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)
        system (sys/new-system options)]

    (cond
      (:help options) (exit! 0 (usage summary))
      errors (exit! 1 (parsing-error-msg errors)))

    (runtime/set-default-uncaught-exception-handler!
     (fn [thread e]
       (println "Uncaught exception, system exiting.")
       (exit! 1 (:cause (Throwable->map e)))))


    (runtime/add-shutdown-hook!
     ::stop-system
     #(do
        ;;"System exiting, running shutdown hooks."
        (component/stop system)))


    (component/start system)))

