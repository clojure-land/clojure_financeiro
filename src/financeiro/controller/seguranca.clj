(ns financeiro.controller.seguranca
  (:require [financeiro.db.usuarios :as db]))

(defn find-by-login-e-senha
  [login senha]
  (let [usuarios (db/find-by-login-e-senha login senha)]
    (if (not-empty usuarios)
      (first usuarios)
      nil)))

(defn get-senha-by-id
  [id]
  (let [usuarios (db/find-by-id id)]
    (if (not-empty usuarios)
      (:senha (first usuarios))
      nil)))