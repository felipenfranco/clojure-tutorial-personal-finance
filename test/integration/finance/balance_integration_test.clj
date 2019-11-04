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

(defn post-to-path [path payload]
  (http/post (str "http://localhost:" port path)
             {:content-type :json
              :body (json/generate-string payload)}))

(against-background [(before :facts (start-server port))
                     (after :facts (stop-server))]
                    (fact "Initial balance is 0"
                          :integration
                          (get-path-json "/balance") => {:balance 0})
                    (fact "Balance is 10 if only transaction is of type expense with value of 10"
                          :integration
                          (post-to-path "/transactions" {:value 10 :type "expense"})
                          (get-path-json "/balance") => {:balance 10}))
