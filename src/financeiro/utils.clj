(ns financeiro.utils
  (:require [cheshire.core :as cheshire]))

(def contexto "/api/v1")

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(defn parse-int [s]
  (if s
    (Integer/parseInt (re-find #"-?\d+" s))
    0))