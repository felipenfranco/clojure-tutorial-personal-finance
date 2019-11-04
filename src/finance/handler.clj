(ns finance.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as json]
            [finance.db :as db]
            [ring.middleware.json :refer [wrap-json-body]]
            [ring.middleware.defaults :refer [wrap-defaults
                                              api-defaults]]))

(defn as-json [content & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body (json/generate-string content)})

(defn balance-as-json []
  (as-json {:balance (db/balance)}))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/balance" [] (balance-as-json))
  (POST "/transactions"
    transaction (-> (db/register-transaction (:body transaction))
                    (as-json 201)))
  (route/not-found "Not Found"))

(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-json-body {:keywords? true :bigdecimals? true})))
