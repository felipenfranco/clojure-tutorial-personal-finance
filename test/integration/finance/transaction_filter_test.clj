(ns finance.transaction-filter-test
  (:require [midje.sweet :refer :all]
            [finance.test-utils :refer :all]
            [finance.db :as db]))

(def random-transactions
  '({:type "expense" :value 1.0M}
    {:type "expense" :value 11.0M}
    {:type "expense" :value 12.0M}
    {:type "deposit" :value 101.0M}))

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
                                 (count (:transactions (get-path-json "/transactions"))) => 4))))