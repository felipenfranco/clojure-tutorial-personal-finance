(ns finance.transaction-test
  (:require [midje.sweet :refer :all]
            [finance.transaction :refer :all]))
(facts "Transactions validation" :unit
       (fact "No value transaction is invalid"
             (valid-transaction? {:type "expense"}) => false)

       (fact "Negative value transaction is invalid"
             (valid-transaction? {:type "expense" :value -100}) => false)

       (fact "Non-numeric value transaction is invalid"
             (valid-transaction? {:type "expense" :value "one thousand"}) => false)

       (fact "No type transaction is invalid"
             (valid-transaction? {:value 100}) => false)

       (fact "Unknown type transaction is invalid"
             (valid-transaction? {:type "invalid type" :value 100}) => false)

       (fact "Positive value and known type is a valid transaction"
             (valid-transaction? {:type "expense" :value 100}) => true))