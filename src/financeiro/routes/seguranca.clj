(ns financeiro.routes.seguranca
  (:require [compojure.route :as route]
            [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [financeiro.utils :as utils]))

(s/defschema credenciais
  {:login s/Str
   :senha s/Str})

(def seguranca-routes
  (context (str utils/contexto "/auth") []
    :tags ["auth"]

    (undocumented
      (compojure.route/not-found (ok {:mensagem "Recurso não encontrado"})))))