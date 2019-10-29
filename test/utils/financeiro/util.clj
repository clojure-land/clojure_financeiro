(ns financeiro.util
  (:require [cheshire.core :as cheshire]))

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))