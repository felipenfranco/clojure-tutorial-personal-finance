(ns finance.balance-integration-test
  (:require [midje.sweet :refer :all]
            [finance.handler :refer [app]]
            [ring.adapter.jetty :refer [run-jetty]]
            [clj-http.client :as http]
            [cheshire.core :as json]))

(def server (atom nil))

(defn start-server [port]
  (swap! server
         (fn [_] (run-jetty app {:port port :join? false}))))

(defn stop-server []
  (.stop @server))

(def port 3001)

(defn get-path [path]
  (:body (http/get (str "http://localhost:" port path))))

(defn get-path-json [path]
  (json/parse-string (get-path path) true))

(against-background [(before :facts (start-server port))
                     (after :facts (stop-server))]
                    (fact "Initial balance is 0" :integration
                          (get-path-json "/balance") => {:balance 0}))
