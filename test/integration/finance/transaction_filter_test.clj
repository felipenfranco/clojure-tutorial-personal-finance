(ns finance.transaction-filter-test
  (:require [midje.sweet :refer :all]
            [finance.test-utils :refer :all]
            [finance.db :as db]))

(def random-transactions
  '({:type "expense" :value 1.0M :tags ["A" "B" "D"]}
    {:type "expense" :value 11.0M :tags ["C" "D"]}
    {:type "expense" :value 12.0M :tags ["D"]}
    {:type "deposit" :value 101.0M :tags ["A" "C" "D"]}))

(facts "Transactions filter endpoints work just fine" :integration
       (against-background [(before :facts
                                    [(start-server port)
                                     (db/clean-transactions)])
                            (after :facts (stop-server))]

                           (fact "There are no transactions"
                                 (get-path-json "/transactions") => {:transactions '()})

                           (fact "There are no expenses"
                                 (get-path-json "/expenses") => {:transactions '()})

                           (fact "There are no deposits"
                                 (get-path-json "/deposits") => {:transactions '()})

       (against-background [(before :facts (doseq [transaction random-transactions]
                                             (db/register-transaction transaction)))
                            (after :facts (db/clean-transactions))]

                           (fact "There are 3 expenses"
                                 (count (:transactions (get-path-json "/expenses"))) => 3)
                           (fact "There is 1 deposit"
                                 (count (:transactions (get-path-json "/deposits"))) => 1)
                           (fact "There are 4 transactions"
                                 (count (:transactions (get-path-json "/transactions"))) => 4)
                           (fact "There are 2 transactions with tag A"
                                 (count (:transactions (get-path-json "/transactions?tag=A"))) => 2)
                           (fact "There are 2 transactions with tag C"
                                 (count (:transactions (get-path-json "/transactions?tag=C"))) => 2)
                           (fact "There is 1 transaction with tag B"
                                 (count (:transactions (get-path-json "/transactions?tag=B"))) => 1)
                           (fact "There are 4 transactions with tag D"
                                 (count (:transactions (get-path-json "/transactions?tag=D"))) => 4)
                           (fact "There are 3 transactions with tag A or C"
                                 (count (:transactions (get-path-json "/transactions?tag=C&tag=A"))) => 3))))