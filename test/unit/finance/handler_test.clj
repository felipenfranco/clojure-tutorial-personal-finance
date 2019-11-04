(ns finance.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [finance.handler :refer :all]
            [cheshire.core :as json]))

(facts "Root URL is 'Hello World'" :unit
       (let [response (app (mock/request :get "/"))]
         (fact "status code is 200"
               (:status response) => 200)
         (fact "response body is 'Hello World'"
               (:body response) => "Hello World")))

(facts "Invalid route does not exist" :unit
       (let [response (app (mock/request :get "/invalid-route"))]
         (fact "status code is 404"
               (:status response) => 404)
         (fact "response body is 'Not Found'"
               (:body response) => "Not Found")))

(facts "Initial balance is 0" :unit
       (against-background (json/generate-string {:balance 0}) => "{\"balance\":0}")
       (let [response (app (mock/request :get "/balance"))]
         (fact "status code is 200"
               (:status response) => 200)
         (fact "format is JSON"
               (get-in response [:headers "Content-Type"]) => "application/json; charset=utf-8")
         (fact "response body is '0"
               (:body response) => "{\"balance\":0}")))
