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
          (where {:login login})
          (limit 1)))

(defn find-by-permissao [id-usuario permissao]
  (select usuarios
          (with permissoes)
          (where {:id [in (subselect permissoes (fields :permissoes_id) (where {:name permissao}))]})))