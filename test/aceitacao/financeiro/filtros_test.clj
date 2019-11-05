(ns financeiro.filtros-test
  (:require [midje.sweet :refer :all]
            [cheshire.core :as json]
            [financeiro.auxiliares :refer :all]
            [clj-http.client :as http]
            [financeiro.controller.transacoes :as controller]))

(def transacoes-aleatorias
  '({:valor 7.0M :tipo "despesa" :rotulos ["sorvete" "entretenimento"]}
    {:valor 88.0M :tipo "despesa" :rotulos ["livro" "educacao"]}
    {:valor 106.0M :tipo "despesa" :rotulos ["curso" "educacao"]}
    {:valor 8000.0M :tipo "receita" :rotulos ["salario"]}))

(against-background [(before :facts
                             [(iniciar-servidor porta-padrao)
                              (controller/limpar)])
                     (around :facts (let [token (get-token "adm" "123456")] ?form))
                     (after :facts (parar-servidor))]
(fact "Não existem receitas" :aceitacao
      (json/parse-string (conteudo "/transacoes" "/receitas" token) true) => {:transacoes '()})

(fact "Não existem despesas" :aceitacao
      (json/parse-string (conteudo "/transacoes" "/despesas" token) true)  => {:transacoes '()})

(fact "Não existem transacoes" :aceitacao
      (json/parse-string (conteudo "/transacoes" "/" token) true) => {:transacoes '()})

(against-background
  [(before :facts (doseq [transacao transacoes-aleatorias]
                    (controller/registrar (:rotulos transacao) (:tipo transacao) (:valor transacao))))
   (after :facts (controller/limpar))]
  (fact "Existem 3 despesas" :aceitacao
        (count (:transacoes (json/parse-string
                              (conteudo "/transacoes" "/despesas" token) true))) => 3)
  (fact "Existe 1 receita" :aceitacao
        (count (:transacoes (json/parse-string
                              (conteudo "/transacoes" "/receitas" token) true))) => 1)
  (fact "Existem 4 transações" :aceitacao
        (count (:transacoes (json/parse-string
                              (conteudo "/transacoes" "/" token) true))) => 4)
  (fact "Existe 1 receita com rótulo 'salario'" :aceitacao
        (count (:transacoes (json/parse-string (conteudo "/transacoes" "/?rotulos=salario" token) true))) => 1)

  (fact "Existem 2 despesas com rótulo 'livro' ou 'curso'" :aceitacao
        (count (:transacoes (json/parse-string (conteudo "/transacoes" "/?rotulos=livro&rotulos=curso" token) true))) => 2)

  (fact "Existem 2 despesas com rótulo 'educacao'" :aceitacao
        (count (:transacoes (json/parse-string (conteudo "/transacoes" "/?rotulos=educacao" token) true))) => 2)))