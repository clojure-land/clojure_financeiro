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
  ([sub-contexto rota] (endereco-para (str sub-contexto rota))))

(def requisicao-para
  (comp http/get endereco-para))

(defn get-header-autorizacao
  [token]
  {:content-type     :json
   :headers          {"authorization" (str "Token " token)}
   :throw-exceptions false})

(defn conteudo
  ([rota]
  (:body (requisicao-para rota)))
  ([sub-contexto rota] (conteudo (str sub-contexto rota)))
  ([sub-contexto rota token] (:body (http/get (endereco-para sub-contexto rota) (get-header-autorizacao token)))))

(defn conteudo-como-json
  ([corpo token]
   {:content-type     :json
    :body             (json/generate-string corpo)
    :headers          {"authorization" token}
    :throw-exceptions false})
  ([corpo]
  {:content-type     :json
   :body             (json/generate-string corpo)
   :throw-exceptions false}))

(defn despesa
  ([valor token]
   (conteudo-como-json {:valor valor :tipo "despesa"} (str "Token " token)))
  ([valor]
  (conteudo-como-json {:valor valor :tipo "despesa"})))

(defn receita
  ([valor token]
   (conteudo-como-json {:valor valor :tipo "receita"} (str "Token " token)))
  ([valor]
  (conteudo-como-json {:valor valor :tipo "receita"})))

(defn dados-autenticacao
  [login senha]
  (conteudo-como-json {:login login :senha senha}))

(defn get-token
  [login senha]
  (let [response (http/post (endereco-para "/seguranca" "/login") (dados-autenticacao login senha))
        token (:token (json/parse-string (:body response) true))]
    token))

