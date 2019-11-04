# clojure-tutorial-personal-finance

Tutorial web application to handle personal finance

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running tests

    lein midje :filter unit

    lein midje :filter integration

## Running

To start a web server for the application, run:

    lein ring server
