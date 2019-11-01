(ns financeiro.dao.db
  (:require [environ.core :refer [env]]
            [korma.db :refer :all]))

(defdb db {:classname   (:db-classname env)
             :subprotocol (:db-subprotocol env)
             :subname     (:db-subname env)
             :user        (:db-user env)
             :make-pool?  (:db-make-pool? env)
             :delimiters  (:db-delimiters env)
             :password    (:db-password env)})