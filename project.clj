(defproject mdc-util "0.2.0"
  :description "A small utility library for use of MDC with tools.logging"
  :dependencies [[org.clojure/clojure "1.9.0" :scope "provided"]]
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"
            :author "Maximilian Karasz"
            :year 2018
            :key "mit"}
  :profiles {:dev {:dependencies [[ch.qos.logback/logback-classic "1.2.3"]]}})
