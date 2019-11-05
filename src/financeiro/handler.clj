(ns financeiro.handler
  (:require [compojure.route :as route]
            [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [financeiro.routes.transacoes :refer [transacoes-routes]]
            [financeiro.routes.seguranca :refer [seguranca-routes]]))

(def app
  (api
    {:swagger {:ui   ""
               :spec "/swagger.json"
               :data {:info                {:title       "Financeiro"
                                            :description "Financeiro with Compojure API"}
                      :tags                [{:name "api", :description "financeiro"}]
                      :consumes            ["application/json"]
                      :produces            ["application/json"]
                      :securityDefinitions {:api_key {:type "apiKey" :name "Authorization" :in "header"}}}}}
    transacoes-routes
    seguranca-routes
    (undocumented
      (compojure.route/not-found (ok {:mensagem "Recurso não encontrado"})))))