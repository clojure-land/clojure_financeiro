(ns financeiro.controller.seguranca
  (:require [financeiro.db.usuarios :as db]))

(defn find-by-login-e-senha
  [login senha]
  (let [usuarios (db/find-by-login-e-senha login senha)]
    (when (not-empty usuarios)
      (first usuarios))))

(defn get-senha-by-id
  [id]
  (let [usuarios (db/find-by-id id)]
    (when (not-empty usuarios)
      (:senha (first usuarios)))))