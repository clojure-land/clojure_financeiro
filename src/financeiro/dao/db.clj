(ns financeiro.dao.db
  (:use korma.db))

(defdb db (mysql
            { :classname "com.mysql.jdbc.Driver"
             :subprotocol "mysql"
             :subname "//localhost/transacoes"
             :user "root"
             :password "transacoes"}))