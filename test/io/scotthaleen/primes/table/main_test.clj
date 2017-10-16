(ns io.scotthaleen.primes.table.main-test
  (:require
   [clojure.test :refer :all]
   [clojure.string :as sz]
   [io.scotthaleen.primes.table.main :as main]
   [com.stuartsierra.component :as component])
  (:import [io.scotthaleen.primes.table SystemExitException]))


(defn- system-exit [status]
  (throw (SystemExitException. (int status))))


(defmacro sys-exit-trap
  " Trap System/exit codes and check the return code
    Throw an exception to mimic a system exit behavior"
  ([body & [expected-exit-code]]
   (let [expected-exit-code (or expected-exit-code 0)]
   `(with-redefs [main/system-exit! system-exit]
      (try
        ~body
        (catch SystemExitException ~'e
          (is (= ~expected-exit-code  (.exitcode ~'e)))))))))


(deftest test-help
  (testing "test main entry with help"
    (is (sz/starts-with?
         (sz/trim
          (with-out-str
            (sys-exit-trap
             (#'io.scotthaleen.primes.table.main/-main "-h"))))
         "Usage:"))))


(deftest test-invalid-args
  (testing "test main entry with invalid arguments"
    (is (sz/starts-with?
         (sz/trim
          (with-out-str
            (sys-exit-trap
             (#'io.scotthaleen.primes.table.main/-main "-n" "a")
             1)))
         "The following errors"))))


(deftest test-main
  (testing "test main entry with n 1"
    (is
     ((complement sz/blank?)
      (with-out-str
        (sys-exit-trap
         (#'io.scotthaleen.primes.table.main/-main "-n" "1")))))))


