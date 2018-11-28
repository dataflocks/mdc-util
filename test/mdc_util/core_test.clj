(ns mdc-util.core-test
  (:require [clojure.test :refer :all]
            [mdc-util.core :refer :all])
  (:import [org.slf4j MDC]))

(deftest setting-parameters-works
  (testing "regular context"
    (is (nil? (MDC/get "test-key")))
    (with-context {"test-key" "test-value"}
      (is (= "test-value" (MDC/get "test-key"))))
    (is (nil? (MDC/get "test-key"))))
  (testing "safe context"
    (is (nil? (MDC/get "test-key")))
    (with-safe-context {"test-key" "test-value"}
      (is (= "test-value" (MDC/get "test-key"))))
    (is (nil? (MDC/get "test-key"))))
  (testing "nil safe"
    (is (nil? (MDC/get "test-key")))
    (with-context {"test-key" nil}
      (is (nil? (MDC/get "test-key")))))
  (testing "via protocol reification"
    (is (nil? (MDC/get "test-key")))
    (with-context (reify MDCProvider
                    (mdc [_] {"test-key" "test-value"}))
      (is (= "test-value" (MDC/get "test-key"))))
    (is (nil? (MDC/get "test-key"))))
  (testing "params as function call work"
    (let [cnt (atom 0)
          ctx (fn []
                (swap! cnt inc)
                {"test-key" "test-value"})]
      (with-context (ctx)
        (is (= "test-value" (MDC/get "test-key"))))
      (testing "function is only called once."
        (is (= 1 @cnt))))))

(deftest shadowing
  (testing "regular context"
    (with-context {"test-key" "value-1"}
      (is (= "value-1" (MDC/get "test-key")))
      (with-context {"test-key" "value-2"}
        (is (= "value-2" (MDC/get "test-key"))))
      (is (nil? (MDC/get "test-key")))))
  (testing "safe context"
    (with-safe-context {"test-key" "value-1"}
      (is (= "value-1" (MDC/get "test-key")))
      (with-safe-context {"test-key" "value-2"}
        (is (= "value-2" (MDC/get "test-key"))))
      (is (= "value-1" (MDC/get "test-key"))))))
