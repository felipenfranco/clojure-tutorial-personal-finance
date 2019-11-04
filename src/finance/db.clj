(ns finance.db)

(def transactions-register
  (atom []))

(defn transactions []
  @transactions-register)

(defn clean-transactions []
  (reset! transactions-register []))

(defn register-transaction [transaction]
  (let [updated-transactions (swap! transactions-register conj transaction)]
    (merge transaction {:id (count updated-transactions)})))