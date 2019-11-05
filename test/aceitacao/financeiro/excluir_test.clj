(ns financeiro.excluir-test
  (:require [midje.sweet :refer :all]
            [cheshire.core :as json]
            [financeiro.auxiliares :refer :all]
            [financeiro.controller.transacoes :as controller]
            [clj-http.client :as http]))

(against-background [(before :facts [(iniciar-servidor porta-padrao)
                                     (controller/limpar)])
                     (around :facts (let [token (get-token "adm" "123456")] ?form))
                     (after :facts (parar-servidor))]
  (fact "Inserindo e deletando uma transação" :aceitacao
        (let [response (http/post (endereco-para "/transacoes" "/") (receita 1000 token))
              id-auto (:generated_key (json/parse-string (:body response) true))]
          (http/delete (str (endereco-para "/transacoes") "/" id-auto) (get-header-autorizacao token)))
        (json/parse-string (conteudo "/transacoes" "/saldo" token) true) => {:saldo 0}))