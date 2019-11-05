(ns financeiro.routes.seguranca-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [financeiro.handler :refer :all]
            [financeiro.utils :refer :all]))

(def contexto-seguranca (str contexto "/seguranca"))

(facts "Tentando autenticar com valores invalidos"
       (let [response (app (-> (mock/request :post (str contexto-seguranca "/login"))
                               (mock/json-body {:login "adm" :senha "123"})))
             body     (parse-body (:body response))]
         (fact "o status da resposta é 200"
               (:status response) => 200)
         (fact "o texto do corpo é 'Credenciais inválidas!'"
               (:mensagem body) => "Credenciais inválidas!")))

(facts "Tentando autenticar com valores invalidos"
       (let [response (app (-> (mock/request :post (str contexto-seguranca "/login"))
                               (mock/json-body {:login "adm" :senha "123456"})))
             body     (parse-body (:body response))]
         (fact "o status da resposta é 200"
               (:status response) => 200)
         (fact "o corpo deve conter os dados do usuario"
               (get-in body [:usuario :login]) => "adm")))