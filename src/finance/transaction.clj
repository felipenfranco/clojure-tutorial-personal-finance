(ns finance.transaction)

(defn valid-transaction? [transaction]
  (and (contains? transaction :value)
       (number? (:value transaction))
       (pos? (:value transaction))
       (contains? transaction :type)
       (or (= (:type transaction) "expense")
           (= (:type transaction) "deposit"))))
