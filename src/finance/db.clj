(ns finance.db)

(def transactions-register
  (atom []))

(defn transactions []
  @transactions-register)

(defn clean-transactions []
  (reset! transactions-register []))

(defn- expense? [transaction]
  (= (:type transaction) "expense"))

(defn- apply-transaction [total transaction]
  (let [value (:value transaction)]
    (if (expense? transaction)
      (- total value)
      (+ total value))))

(defn balance []
  (reduce apply-transaction 0 @transactions-register))

(defn register-transaction [transaction]
  (let [updated-transactions (swap! transactions-register conj transaction)]
    (merge transaction {:id (count updated-transactions)})))

(defn transactions-of-type [type]
  (filter #(= (:type %) type) (transactions)))