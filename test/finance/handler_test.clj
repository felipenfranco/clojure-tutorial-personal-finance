(ns finance.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [finance.handler :refer :all]))

(facts "Root URL is 'Hello World'"
       (let [response (app (mock/request :get "/"))]
         (fact "status code is 200"
               (:status response) => 200)
         (fact "response body is 'Hello World'"
               (:body response) => "Hello World")))

(facts "Invalid route does not exist"
       (let [response (app (mock/request :get "/invalid-route"))]
         (fact "status code is 404"
               (:status response) => 404)
         (fact "response body is 'Not Found'"
               (:body response) => "Not Found")))