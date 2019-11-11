(ns financeiro.controller.transacoes
  (:require [financeiro.db.transacoes :as db]
            [schema.core :as schema]))

(defn valida? [tipo valor]
  (and (number? valor)
       (pos? valor)
       (or (= "despesa" tipo)
           (= "receita" tipo))))

(defn registrar [rotulos tipo valor]
  (db/create rotulos tipo valor))

(defn- despesa? [transacao]
  (= (:tipo transacao) "despesa"))

(defn- calcular [acumulado transacao]
  (let [valor (:valor transacao)]
    (if (despesa? transacao)
      (- acumulado valor)
      (+ acumulado valor))))

(defn saldo []
  (reduce calcular 0 (db/find-all)))

(defn transacoes-do-tipo [tipo]
  (db/find-by-tipo tipo))

(defn find-all []
  (db/find-all))

(defn transacoes-com-filtro [filtros]
  (let [rotulos (->> (:rotulos filtros)
                     (vector)
                     (flatten)
                     (set))]
    (db/find-by-rotulos rotulos)))

(defn limpar []
  (db/delete-all))

(defn delete-by-id [id]
  (db/delete-by-id id))
