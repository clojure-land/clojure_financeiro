(ns financeiro.dao.transacoes
  (:require [korma.db :refer :all]
            [korma.core :refer :all]
            [financeiro.dao.db :refer :all]
            ))

(defentity rotulos)

(defentity transacoes
  (has-many rotulos))

(defn find-all
  []
  (select transacoes
          (with rotulos)))

(defn find-by-id [id]
  (select transacoes
          (where {:id id})
          (limit 1)))

(defn find-by-tipo [tipo]
  (select transacoes
          (where {:tipo tipo})))

(defn find-by-rotulos [rotulos-in]
  (select transacoes
          (with rotulos)
          (where {:id [in (subselect rotulos (fields :transacoes_id) (where {:description [in rotulos-in]}))]})))

(defn create
  [rotulos-in tipo valor]
  (let [id-auto (:generated_key (insert transacoes (values {:tipo tipo :valor valor})))]
    (doseq [description rotulos-in] (insert rotulos
                              (values {:description description :transacoes_id id-auto})))
    id-auto))

(defn delete-by-id [id]
  (delete transacoes
          (where {:id id})))

(defn delete-all []
  (delete transacoes))