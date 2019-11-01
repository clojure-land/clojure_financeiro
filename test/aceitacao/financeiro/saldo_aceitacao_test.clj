(ns financeiro.saldo-aceitacao-test
  (:require [midje.sweet :refer :all]
            [cheshire.core :as json]
            [financeiro.auxiliares :refer :all]
            [financeiro.controller.transacoes :as controller]
            [clj-http.client :as http]))

(against-background [(before :facts [(iniciar-servidor porta-padrao)
                                     (controller/limpar)])
                     (after :facts (parar-servidor))]
  (fact "O saldo inicial é 0" :aceitacao
        (json/parse-string (conteudo "/transacoes" "/saldo") true) => {:saldo 0})
  (fact  "O saldo é 10 quando a única transação é uma receita de 10" :aceitacao
     (http/post (endereco-para "/transacoes" "/") (receita 2000))
     (http/post (endereco-para "/transacoes" "/") (receita 2000))
     (http/post (endereco-para "/transacoes" "/") (despesa 3000))
     (json/parse-string (conteudo "/transacoes" "/saldo") true) => {:saldo 1000.0})
  (fact "Rejeita uma transação sem valor" :aceitacao
        (let [response (http/post (endereco-para "/transacoes" "/")
                                  (conteudo-como-json {:tipo "receita"}))]
          (:status response) => 400))
  (fact "Rejeita uma transação com valor negativo" :aceitacao
        (let [response (http/post (endereco-para "/transacoes" "/")
                                  (receita -100))]
          (:status response) => 400))
  (fact "Rejeita uma transação com valor que não é um número" :aceitacao
        (let [response (http/post (endereco-para "/transacoes" "/")
                                  (receita "mil"))]
          (:status response) => 400))
  (fact "Rejeita uma transação sem tipo" :aceitacao
        (let [response (http/post (endereco-para "/transacoes" "/")
                                  (conteudo-como-json {:valor 70}))]
          (:status response) => 400)))