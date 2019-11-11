(ns financeiro.routes.transacoes
  (:require [compojure.route :as route]
            [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [financeiro.utils :as utils]
            [financeiro.routes.seguranca-utils :refer :all]
            [financeiro.controller.transacoes :as controller]))

(s/defschema transacoes
  {:tipo                     s/Str
   :valor                    s/Num
   (s/optional-key :rotulos) [s/Str]})

(def transacoes-routes
  (context (str utils/contexto "/transacoes") []
    :tags ["Transacoes"]
    (GET "/saldo" []
      :permissoes #{"SELECT"}
      :usuario user
      (ok {:saldo (controller/saldo)}))
    (POST "/" []
      :permissoes #{"INSERT"}
      :usuario user
      :body [requisicao transacoes]
      (let [{:keys [rotulos tipo valor]} requisicao]
        (if (controller/valida? tipo valor)
          (created "/transacoes" (controller/registrar rotulos tipo valor))
          (bad-request {:mensagem "Requisição inválida"}))))
    (GET "/" {filtros :params}
      :permissoes #{"SELECT"}
      :usuario user
      (ok {:transacoes
           (if (empty? filtros)
             (controller/find-all)
             (controller/transacoes-com-filtro filtros))}))
    (DELETE "/:id" []
      :permissoes #{"DELETE"}
      :usuario user
      :path-params [id :- s/Int]
      (ok {:qtd (controller/delete-by-id id)}))
    (GET "/receitas" []
      :permissoes #{"SELECT"}
      :usuario user
      (ok {:transacoes (controller/transacoes-do-tipo "receita")}))
    (GET "/despesas" []
      :permissoes #{"SELECT"}
      :usuario user
      (ok {:transacoes (controller/transacoes-do-tipo "despesa")}))
    (undocumented
      (compojure.route/not-found (ok {:mensagem "Recurso não encontrado"})))))