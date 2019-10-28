(ns financeiro.transacoes-test
  (:require [midje.sweet :refer :all]
            [financeiro.controller :refer :all]))

(fact "Uma transação com valor negativo não é válida"
      (valida? "receita" -10) => false)

(fact "Uma transação com valor não numérico não é válida"
      (valida? "receita" "mil") => false)

(fact "Uma transação com tipo desconhecido não é válida"
      (valida? "investimento" 8) => false)

(fact "Uma transação com valor numérico positivo e com tipo conhecido é válida"
      (valida? "receita" 230) => true)