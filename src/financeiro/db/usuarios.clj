(ns financeiro.db.usuarios
  (:require [korma.db :refer :all]
            [korma.core :refer :all]
            [financeiro.db.db :refer :all]))

(defentity permissoes)

(defentity usuarios
           (has-many permissoes))

(defn find-by-login-e-senha
  [login senha]
  (select usuarios
          (with permissoes)
          (where (and {:login login}
                      {:senha senha}))
          (limit 1)))

(defn find-by-id
  [id]
  (select usuarios
          (where {:id id})
          (limit 1)))