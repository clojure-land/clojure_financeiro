(ns financeiro.auxiliares
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [financeiro.handler :refer [app]]
            [financeiro.utils :refer [contexto]]
            [cheshire.core :as json]
            [clj-http.client :as http]))

(def servidor
  (atom nil))

(defn iniciar-servidor
  [porta]
  (swap! servidor
         (fn [_] (run-jetty app {:port porta :join? false}))))

(defn parar-servidor
  []
  (.stop @servidor))

(def porta-padrao 3001)

(defn endereco-para
  ([rota] (str "http://localhost:" porta-padrao contexto rota))
  ([sub-contexto rota] (endereco-para (str sub-contexto rota)))
  )

(def requisicao-para
  (comp http/get endereco-para))

(defn conteudo
  ([rota]
  (:body (requisicao-para rota)))
  ([sub-contexto rota] (conteudo (str sub-contexto rota))))

(defn conteudo-como-json [transacao]
  {:content-type     :json
   :body             (json/generate-string transacao)
   :throw-exceptions false})

(defn despesa
  [valor]
  (conteudo-como-json {:valor valor :tipo "despesa"}))

(defn receita
  [valor]
  (conteudo-como-json {:valor valor :tipo "receita"}))