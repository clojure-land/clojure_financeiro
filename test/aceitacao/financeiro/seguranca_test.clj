(ns financeiro.seguranca-test
  (:require [midje.sweet :refer :all]
            [cheshire.core :as json]
            [financeiro.auxiliares :refer :all]
            [financeiro.controller.transacoes :as controller]
            [clj-http.client :as http]))

(against-background [(before :facts [(iniciar-servidor porta-padrao)
                                     (controller/limpar)])
                     (after :facts (parar-servidor))]
  (fact  "Autenticando com sucesso!" :aceitacao
     (let [response (http/post (endereco-para "/seguranca" "/login") (dados-autenticacao "adm" "123456"))
           login (get-in (json/parse-string (:body response) true) [:usuario :login])]
           login => "adm")))