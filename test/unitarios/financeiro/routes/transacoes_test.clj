(ns financeiro.routes.transacoes-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as json]
            [financeiro.handler :refer :all]
            [financeiro.utils :refer :all]
            [financeiro.routes.seguranca-utils :as seguranca-utils]
            [financeiro.controller.transacoes :as controller]))

(def contexto-transacoes (str contexto "/transacoes"))

(def playload-test "1:adm:SELECT/INSERT/UPDATE/DELETE:1575931138")

(def senha-test "123456")

(def token-sign-test "53acedb42b16aab6dd1a140204286c481facfc3d4831cabff106de9f0011c827")

(def token-test (str playload-test "." token-sign-test))

(facts "Saldo inicial é 0"
       (against-background [(json/generate-string {:saldo 0}) => "{\"saldo\":0}"
                            (controller/saldo) => 0
                            (seguranca-utils/token-valido? playload-test senha-test token-sign-test) => true])
       (let [response (app (-> (mock/request :get (str contexto-transacoes "/saldo"))
                               (mock/header "authorization" (str "Token " token-test))))
             body     (parse-body (:body response))]
         (fact "o formato é 'application/json'"
               (get-in response [:headers "Content-Type"]) => "application/json; charset=utf-8")
         (fact "o status da resposta é 200"
               (:status response) => 200)
         (fact "o texto do corpo é '0'"
               (json/generate-string body) => "{\"saldo\":0}")))

(facts "Registra uma receita no valor de 10"
       (against-background [(controller/registrar [] "receita" 10) => 1
                            (seguranca-utils/token-valido? playload-test senha-test token-sign-test) => true])
       (let [response (app (-> (mock/request :post (str contexto-transacoes "/"))
                               (mock/header "authorization" (str "Token " token-test))
                               (mock/json-body {:rotulos [] :valor 10 :tipo "receita"})))]
         (fact "o status da resposta é 201"
               (:status response) => 201)
         (fact "o texto do corpo é um JSON com o conteúdo enviado e um id"
               (json/generate-string (:body response)) => "1")
         )
       )

(facts "Existe rota para lidar com filtro de transação por tipo"
       (against-background [(controller/transacoes-do-tipo "receita") => '({:id 1 :valor 2000 :tipo "receita"})
                            (controller/transacoes-do-tipo "despesa") => '({:id 2 :valor 89 :tipo "despesa"})
                            (controller/find-all) => '({:id 1 :valor 2000 :tipo "receita"} {:id 2 :valor 89 :tipo "despesa"})
                            (seguranca-utils/token-valido? playload-test senha-test token-sign-test) => true]
         (fact "Filtro por receita"
               (let [response (app (-> (mock/request :get (str contexto-transacoes "/receitas"))
                                       (mock/header "authorization" (str "Token " token-test))))
                     body     (parse-body (:body response))]
                 (:status response) => 200
                 (json/generate-string body) => (json/generate-string
                                       {:transacoes '({:id 1 :valor 2000 :tipo "receita"})})))
         (fact "Filtro por despesa"
               (let [response (app (-> (mock/request :get (str contexto-transacoes "/despesas"))
                                       (mock/header "authorization" (str "Token " token-test))))
                     body     (parse-body (:body response))]
                 (:status response) => 200
                 (json/generate-string body) => (json/generate-string
                                       {:transacoes '({:id 2 :valor 89 :tipo "despesa"})})))
         (fact "Sem filtro"
               (let [response (app (-> (mock/request :get (str contexto-transacoes "/"))
                                       (mock/header "authorization" (str "Token " token-test))))
                     body     (parse-body (:body response))]
                 (:status response) => 200
                 (json/generate-string body) => (json/generate-string
                                       {:transacoes '({:id 1 :valor 2000 :tipo "receita"}
                                                      {:id 2 :valor 89 :tipo "despesa"})})))))

(facts "Filtra transações por parâmetros de busca na URL"
       (def livro {:id 1 :valor 88 :tipo "despesa" :rotulos ["livro" "educação"]})
       (def curso {:id 2 :valor 106 :tipo "despesa" :rotulos ["curso" "educação"]})
       (def salario {:id 3 :valor 8000 :tipo "receita" :rotulos ["salário"]})
       (against-background [(controller/transacoes-com-filtro {:rotulos ["livro" "curso"]}) => [livro curso]
                            (controller/transacoes-com-filtro {:rotulos "salário"}) => [salario]
                            (seguranca-utils/token-valido? playload-test senha-test token-sign-test) => true]
         (fact "Filtro múltiplos rótulos"
               (let [response (app (-> (mock/request :get (str contexto-transacoes "/?rotulos=livro&rotulos=curso"))
                                       (mock/header "authorization" (str "Token " token-test))))
                     body     (parse-body (:body response))]
                                    (:status response) => 200
                                    (json/generate-string body) => (json/generate-string {:transacoes [livro curso]})))
         (fact "Filtro com único rótulo"
               (let [response (app (-> (mock/request :get (str contexto-transacoes "/?rotulos=salário"))
                                       (mock/header "authorization" (str "Token " token-test))))
                     body     (parse-body (:body response))]
                                    (:status response) => 200
                                    (json/generate-string body) => (json/generate-string {:transacoes [salario]})))))

(facts "Deletando uma transacao que não existe"
       (against-background [(controller/delete-by-id 0) => 0
                            (seguranca-utils/token-valido? playload-test senha-test token-sign-test) => true])
       (let [response (app (-> (mock/request :delete (str contexto-transacoes "/0"))
                               (mock/header "authorization" (str "Token " token-test))))
             body     (parse-body (:body response))]
         (fact "o código de erro é 200"
               (:status response) => 200)
         (fact "o texto do corpo é '0'"
               (:qtd body) => 0)))

(facts "Deletando uma transacao que existe"
       (against-background [(controller/delete-by-id 1) => 1
                            (seguranca-utils/token-valido? playload-test senha-test token-sign-test) => true])
       (let [response (app (-> (mock/request :delete (str contexto-transacoes "/1"))
                               (mock/header "authorization" (str "Token " token-test))))
             body     (parse-body (:body response))]
         (fact "o código de erro é 200"
               (:status response) => 200)
         (fact "o texto do corpo é '0'"
               (:qtd body) => 1)))