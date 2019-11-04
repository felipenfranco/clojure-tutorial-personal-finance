(ns finance.balance-integration-test
  (:require [midje.sweet :refer :all]
            [finance.handler :refer [app]]
            [ring.adapter.jetty :refer [run-jetty]]
            [clj-http.client :as http]))

(def server (atom nil))

(defn start-server [port]
  (swap! server
         (fn [_] (run-jetty app {:port port :join? false}))))

(defn stop-server []
  (.stop @server))

(against-background [(before :facts (start-server 3001))
                     (after :facts (stop-server))]
 (fact "Initial balance is 0" :integration
       (:body (http/get "http://localhost:3001/balance")) => "0"))