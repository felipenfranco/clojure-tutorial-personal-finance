(ns finance.balance-integration-test
  (:require [midje.sweet :refer :all]
            [finance.db :as db]
            [finance.test-utils :refer :all]))


(against-background [(before :facts [(start-server port)
                                     (db/clean-transactions)])
                     (after :facts (stop-server))]
                    (fact "Initial balance is 0"
                          :integration
                          (get-path-json "/balance") => {:balance 0})
                    (fact "Balance is 10 if only transaction is of type expense with value of 10"
                          :integration
                          (post-to-path "/transactions" {:value 10 :type "expense"})
                          (get-path-json "/balance") => {:balance -10})
                    (fact "Balance is 150 when we have one 200 deposit and one 50 expense"
                          :integration
                          (post-to-path "/transactions" {:value 50 :type "expense"})
                          (post-to-path "/transactions" {:value 200 :type "deposit"})
                          (get-path-json "/balance") => {:balance 150})
                    (fact "Rejects no value transaction"
                          :integration
                          (let [response (post-to-path "/transactions" {:type "expense"})]
                            (:status response) => 422))
                    (fact "Rejects negative value transaction"
                          :integration
                          (let [response (post-to-path "/transactions" {:value -100
                                                                        :type "expense"})]
                            (:status response) => 422))
                    (fact "Rejects non-number value transaction"
                          :integration
                          (let [response (post-to-path "/transactions" {:value "one thousand"
                                                                        :type "expense"})]
                            (:status response) => 422))
                    (fact "Rejects no-type transaction"
                          :integration
                          (let [response (post-to-path "/transactions" {:type "expense"})]
                            (:status response) => 422)))
