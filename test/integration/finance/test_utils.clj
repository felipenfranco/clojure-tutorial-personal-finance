(ns finance.test-utils
  (:require [finance.handler :refer [app]]
            [ring.adapter.jetty :refer [run-jetty]]
            [clj-http.client :as http]
            [cheshire.core :as json]
            [finance.db :as db]))

(def port 3001)

(def server (atom nil))

(defn start-server [port]
  (swap! server
         (fn [_] (run-jetty app {:port port :join? false}))))

(defn stop-server []
  (.stop @server))

(defn get-path [path]
  (:body (http/get (str "http://localhost:" port path))))

(defn get-path-json [path]
  (json/parse-string (get-path path) true))

(defn post-to-path [path payload]
  (http/post (str "http://localhost:" port path)
             {:content-type :json
              :body (json/generate-string payload)
              :throw-exceptions false}))