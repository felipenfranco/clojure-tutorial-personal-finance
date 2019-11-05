# clojure-tutorial-personal-finance

Tutorial web application to handle personal finance

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running tests

    lein midje :filter unit

    lein midje :filter integration

    lein midje :filter db

## Running

To start a web server for the application, run:

    lein ring server

## Deploying

To create a JAR artefact for deployment:

    lein ring uberjar

## Running artifact

To run the JAR artifact

    java -jar targert/finance.jar


## Test that your application is working using CURL

    > curl http://localhost:3000/balance
    {"balance": 0}

    > curl -X POST -d '{"value": 1500, "type": "expense"}' -H "Content-Type: application/json" localhost:3000/transactions
    {"value":1500,"type":"expense","id":1}

    > curl -X POST -d '{"value": 130, "type": "expense"}' -H "Content-Type: application/json" localhost:3000/transactions
    {"value":130,"type":"expense","id":2}

    > curl http://localhost:3000/balance
    {"balance":-1630}

    > curl -X POST -d '{"value": 1700, "type": "deposit"}' -H "Content-Type: application/json" localhost:3000/transactions
    {"value":1700,"type":"deposit","id":3}

    > curl http://localhost:3000/balance
    {"balance":70}

    > curl -X POST -d '{"value": 1700, "type": "invalid type"}' -H "Content-Type: application/json" localhost:3000/transactions
    {"message":"Invalid transaction"}

    > curl localhost:3000/transactions
    {"transactions":[{"value":1500,"type":"expense"},{"value":130,"type":"expense"},{"value":1700,"type":"deposit"}]}

    > curl localhost:3000/expenses
    {"transactions":[{"value":1500,"type":"expense"},{"value":130,"type":"expense"}]}

    > curl localhost:3000/deposits
    {"transactions":[{"value":1700,"type":"deposit"}]}

    > curl -X POST -d '{"value": 1700, "type": "deposit", "tags": ["A"]}' -H "Content-Type: application/json" localhost:3000/transactions
    {"value":1700,"type":"deposit","tags":["A"],"id":4}

    >  curl -X POST -d '{"value": 1300, "type": "expense", "tags": ["B", "C"]}' -H "Content-Type: application/json" localhost:3000/transactions
    {"value":1300,"type":"expense","tags":["B", "C"],"id":5}

    > curl localhost:3000/transactions?tag=A
    {"transactions":[{"value":1700,"type":"deposit","tags":["A"]}]}

    > curl localhost:3000/transactions?tag=B
    {"transactions":[{"value":1300,"type":"expense","tags":["B","C"]}]}

    > curl localhost:3000/transactions?tag=A\&tag=C
    {"transactions":[{"value":1700,"type":"deposit","tags":["A"]},{"value":1300,"type":"expense","tags":["B","C"]}]}


## Database configuration

We will use Datomic as database.
Download and unzip datomic. On the root folder of Datomic, run the following command to initialize an in-memory database for testing purposes:

    > bin/run -m datomic.peer-server -h localhost -p 8998 -a myaccesskey,mysecret -d finance_db,datomic:mem://finance_db