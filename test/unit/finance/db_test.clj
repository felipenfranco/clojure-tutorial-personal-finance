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
