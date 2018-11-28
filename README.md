# mdc-util

A tiny clojure library wrapping the MDC functionality in slf4j.

## Usage

Provides two simple macros to set up a logging context that will be used
for all nested logging calls. The logging context is expected to be
a `Map<String,String>`.

```clojure
(with-context {"key-a" "value-a"
               "key-b" "value-b"}
			   ...
			   (log/info "hello with values")
			   ...)
```

This will make the map available for downstream processing (eg in the case
of logstash-logback-encoder add the map to the emitted JSON map, see
here: [https://github.com/logstash/logstash-logback-encoder#loggingevent_mdc](https://github.com/logstash/logstash-logback-encoder#loggingevent_mdc)).

For other types of appenders, configuration is required to integrate
selected values into log messages (see logback documentation here:
[http://logback.qos.ch/manual/mdc.html](http://logback.qos.ch/manual/mdc.html)).

Bonus feature: `with-safe-context`

This enables proper shadowing of context members, ie nested calls with
different values assigned to `key-a` in each nesting are scoped as would
be expected with a lexical binding. The unsafe version clears up all keys
it has set, potentially removing keys that were present in an outer context.

The drawback of `with-safe-context` is that currently a backup of the current
context is being held and recovered at the end of the form. These maps,
unfortunately, have to be *copied* every time, which might result in a quite
noticable performance overhead in some scenarios.
