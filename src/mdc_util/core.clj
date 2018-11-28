(ns mdc-util.core
  (:import [org.slf4j MDC]
           [java.util Map]))

(defprotocol MDCProvider
  (mdc [this]))

(extend-protocol MDCProvider
  java.lang.Object
  (mdc [_] {})
  java.util.Map
  (mdc [this]
    (->> (.keySet this)
         (filter #(.get this %))
         (map #(do [(str %) (str (.get this %))]))
         (into {}))))

(defmacro with-context [mdc-provider & body]
  `(let [ctx# (mdc ~mdc-provider)]
     (doseq [[name# value#] ctx#]
       (MDC/put ^String name# ^String value#))
     (try
       ~@body
       (finally
         (doseq [name# (-> ctx# keys vec)]
           (MDC/remove ^String name#))))))

(defmacro with-safe-context [mdc-provider & body]
  `(let [old-context# (MDC/getCopyOfContextMap)]
     (doseq [[name# value#] (mdc ~mdc-provider)]
       (MDC/put ^String name# ^String value#))
     (try
       ~@body
       (finally
         (MDC/setContextMap ^Map old-context#)))))
