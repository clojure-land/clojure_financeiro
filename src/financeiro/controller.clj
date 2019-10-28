(ns financeiro.controller
  (:require [financeiro.dao.transacoes :as dao]))

(defn valida? [tipo valor]
  (and (number? valor)
       (pos? valor)
       (or (= "despesa" tipo)
           (= "receita" tipo))))

(defn registrar [rotulos tipo valor]
  (dao/create rotulos tipo valor))

(defn- despesa? [transacao]
  (= (:tipo transacao) "despesa"))

(defn- calcular [acumulado transacao]
  (let [valor (:valor transacao)]
    (if (despesa? transacao)
      (- acumulado valor)
      (+ acumulado valor))))

(defn saldo []
  (->>
      (dao/find-all)
      (reduce calcular 0)))

(defn transacoes-do-tipo [tipo]
  (dao/find-by-tipo tipo))

(defn find-all []
  (dao/find-all))

(defn transacoes-com-filtro [filtros]
  (let [rotulos (->> (:rotulos filtros)
                     (conj [])
                     (flatten)
                     (set))]
    (dao/find-by-rotulos rotulos)))

(defn limpar []
  (dao/delete-all))

(defn find-by-id [id]
  (dao/find-by-id id))

(defn delete-by-id [id]
  (dao/delete-by-id id))