(ns finance.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [finance.handler :refer :all]))

(facts "Root URL is 'Hello World'"
       (fact "status code is 200"
             (let [response (app (mock/request :get "/"))]
               (:status response) => 200))

       (fact "response body is 'Hello World'"
             (let [response (app (mock/request :get "/"))]
               (:body response) => "Hello World")))

(facts "Invalid route does not exist"
       (fact "status code is 404"
             (let [response (app (mock/request :get "/invalid-route"))]
               (:status response) => 404)))