(ns financeiro.handler
  (:require [compojure.route :as route]
            [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [financeiro.controller :as controller]))

(s/defschema transacoes
  {:tipo  s/Str
   :valor s/Num
   (s/optional-key :rotulos) [s/Str]})

(def contexto "/api")

(def app
  (api
   {:swagger {:ui "/"
               :spec "/swagger.json"
               :data {:info {:title "Financeiro"
                    :description "Financeiro with Compojure API"}
               :tags [{:name "api", :description "financeiro"}]
               :consumes ["application/json"]
               :produces ["application/json"]}}}

    (context contexto []  :tags ["api"]
      (GET "/saldo" [] (ok {:saldo (controller/saldo)}))
      (POST "/transacoes" []
        :body [requisicao transacoes]
        (let [{:keys [rotulos tipo valor]} requisicao]
          (if (controller/valida? tipo valor)
            (->> (controller/registrar rotulos tipo valor)
                (created "/transacoes"))
            (bad-request {:mensagem "Requisição inválida"}))))
        (GET "/transacoes" {filtros :params}
          (ok {:transacoes
                      (if (empty? filtros)
                        (controller/find-all)
                        (controller/transacoes-com-filtro filtros))}))
        (DELETE "/transacoes/:id" []
          :path-params [id :- s/Int]
            (ok {:qtd (controller/delete-by-id id)}))
        (GET "/receitas" []
          (ok {:transacoes (controller/transacoes-do-tipo "receita")}))
        (GET "/despesas" []
          (ok {:transacoes (controller/transacoes-do-tipo "despesa")}))
        (undocumented
          (compojure.route/not-found (ok {:mensagem "Recurso não encontrado"}))))))
