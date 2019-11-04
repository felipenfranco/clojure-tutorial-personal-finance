(ns finance.db-test
  (:require [midje.sweet :refer :all]
            [finance.db :refer :all]))

(facts "Store transaction in an atom" :db
       (against-background [(before :facts (clean-transactions))]
                           (fact "Initial transaction collection is empty"
                                 (count (transactions)) => 0)
                           (fact "transaction is the first register"
                                 (register-transaction {:value 7 :type "expense"})
                                 => {:id 1 :value 7 :type "expense"})))

(facts "Given a list of transactions, compute the balance" :db
       (against-background [(before :facts (clean-transactions))]
                           (fact "positive when only deposits"
                                 (register-transaction {:value 10 :type "deposit"})
                                 (register-transaction {:value 11 :type "deposit"})
                                 (balance) => 21)
                           (fact "negative when only expenses"
                                 (register-transaction {:value 10 :type "expense"})
                                 (register-transaction {:value 11 :type "expense"})
                                 (balance) => -21)
                           (fact "balance is the sum of deposits minus the sum of expenses"
                                 (register-transaction {:value 10 :type "deposit"})
                                 (register-transaction {:value 11 :type "expense"})
                                 (balance) => -1)))
