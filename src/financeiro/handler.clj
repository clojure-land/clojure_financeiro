(ns financeiro.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as json]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body]]
            [ring.util.response :refer [redirect]]
            [financeiro.controller :as controller]))

;; o conteúdo agora é passado como argumento
(defn como-json [conteudo & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body (json/generate-string conteudo)})

(defroutes app-routes
  (GET "/" [] (redirect "/saldo"))
  (GET "/saldo" [] (como-json {:saldo (controller/saldo)}))
  (POST "/transacoes" requisicao
    (let [rotulos (get-in requisicao [:body "rotulos"])
            tipo (get-in requisicao [:body "tipo"])
            valor (get-in requisicao [:body "valor"])]
      (if (controller/valida? tipo valor)
        (-> (controller/registrar rotulos tipo valor)
            (como-json 201))
        (como-json {:mensagem (format "Requisição inválida")} 422)
      )
    )
 )
 (GET "/transacoes" {filtros :params}
   (como-json {:transacoes
               (if (empty? filtros)
                 (controller/find-all)
                 (controller/transacoes-com-filtro filtros))}))
 (DELETE "/transacoes/:id" [id]
   (println (format "Parametros: id -> %s" id))
   (if (controller/find-by-id id)
     (->
       (controller/delete-by-id id)
       (como-json 200)
       )
     (como-json {:mensagem (format "Recurso não encontrado")} 404)))
 (GET "/receitas" []
   (como-json {:transacoes (controller/transacoes-do-tipo "receita")}))
 (GET "/despesas" []
   (como-json {:transacoes (controller/transacoes-do-tipo "despesa")}))

  (route/not-found "Recurso não encontrado"))

(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-json-body {:bigdecimals? true})))