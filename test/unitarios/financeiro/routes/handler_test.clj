(ns financeiro.routes.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as json]
            [financeiro.handler :refer :all]
            [financeiro.utils :refer :all]))

(facts "Rota inválida não existe"
       (let [response (app (mock/request :get (str contexto "/invalid")))
             body     (parse-body (:body response))]
         (fact "o código de erro é 404"
               (:status response) => 404)
         (fact "o texto do corpo é 'Recurso não encontrado'"
               (:mensagem body) => "Recurso não encontrado")))

