(ns financeiro.routes.seguranca
  (:require [compojure.route :as route]
            [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [financeiro.utils :as utils]
            [financeiro.routes.seguranca-utils :refer :all]
            [financeiro.controller.seguranca :as controller]
            [environ.core :refer [env]]))

(s/defschema credenciais
  {:login s/Str
   :senha s/Str})

(defn- autenticacao
  ([credencial] (autenticacao (:login credencial) (:senha credencial)))
  ([login senha]
   (let [usuario (controller/find-by-login-e-senha login senha)]
     (if (nil? usuario)
       nil
       (let [passw (:senha usuario)
             payload (codificar-dados usuario)
             token (str payload "." (sign payload passw))]
         {:usuario (dissoc usuario :senha :permissoes)
          :token token})))))

(def seguranca-routes
  (context (str utils/contexto "/seguranca") []
    :tags ["Autenticacao"]
    (POST "/login" []
      :body [credencial credenciais]
      :summary "Autenticação"
      (let [retorno (autenticacao credencial)]
        (if (nil? retorno)
          (ok {:mensagem "Credenciais inválidas!"})
          (ok retorno))))
    (undocumented
      (compojure.route/not-found (ok {:mensagem "Recurso não encontrado"})))))