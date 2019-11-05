(ns finance.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as json]
            [finance.db :as db]
            [finance.transaction :refer [valid-transaction?]]
            [ring.middleware.json :refer [wrap-json-body]]
            [ring.middleware.defaults :refer [wrap-defaults
                                              api-defaults]]))

(defn as-json [content & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body (json/generate-string content)
   :throw-exceptions false})

(defn balance-as-json []
  (as-json {:balance (db/balance)}))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/balance" [] (balance-as-json))
  (POST "/transactions" request
    (let [transaction (:body request)]
      (if (valid-transaction? transaction)
        (-> (db/register-transaction transaction)
            (as-json 201))
        (as-json {:message "Invalid transaction"} 422))))
  (GET "/transactions" {filters :params}
    (as-json {:transactions
              (if (empty? filters)
                (db/transactions)
                (db/transactions-with-filters filters))}))
  (GET "/expenses" []
    (as-json {:transactions (db/transactions-of-type "expense")}))
  (GET "/deposits" []
    (as-json {:transactions (db/transactions-of-type "deposit")}))
  (GET "/q" {params :params} (str params))
  (route/not-found "Not Found"))

(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-json-body {:keywords? true :bigdecimals? true})))
