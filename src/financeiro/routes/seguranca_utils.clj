(ns financeiro.routes.seguranca-utils
  (:require [clojure.string :as cstr]
            [ring.util.http-response :refer :all]
            [environ.core :refer [env]]
            [buddy.core.mac :as mac]
            [buddy.core.codecs :as codecs]
            [financeiro.utils :as utils]
            [financeiro.controller.seguranca :as controller]
            [compojure.api.meta :refer [restructure-param]]))

(def ^:private secret (:jwt-secret env))

(defn codificar-dados
  [usuario]
  (str (:id usuario) ":"
       (:login usuario) ":"
       (->> (:permissoes usuario)
            (map :name)
            (cstr/join "/")
            ) ":"
       (+ (quot (System/currentTimeMillis) 1000) (* 60 60 24 30)))) ; 30 Dias

(defn sign [msg password]
  (codecs/bytes->hex (mac/hash msg {:key (str secret password), :alg :hmac+sha256})))

(defn token-valido?
  [payload passw token-sign]
  (= token-sign (sign payload passw)))

(defn- parse-token
  [token]
  (let [[payload token-sign] (cstr/split token #"\.")
        usuario (zipmap [:id :login :permissoes :expiracao]
                        (cstr/split payload #":"))
        passw (controller/get-senha-by-id (:id usuario))]
    (when (token-valido? payload passw token-sign)
      usuario)))

(defn- parse-header [request token-name]
  (some->> (some-> (find-header request "authorization")
                   (second))
           (re-find (re-pattern (str "^" token-name " (.+)$")))
           (second)))

(defn- not-expire?
  [usuario]
  (let [now (quot (System/currentTimeMillis) 1000)]
    (when (< now (-> usuario :expiracao utils/parse-int))
      usuario)))

(defn- permissao?
  [permissoes permissoes-requeridas]
  (let [permissoes-split (cstr/split permissoes #"/")
        matched-roles (clojure.set/intersection (set permissoes-split) permissoes-requeridas)]
    (not (empty? matched-roles))))

(defn- verifica-permissoes
  [handler permissoes-requeridas]
  (fn [request]
    (let [usuario (some-> (parse-header request "Token")
                          (parse-token)
                          (not-expire?)
                          )]
      (if-not usuario
        (unauthorized "Não autorizado!")
        (if-not (permissao? (:permissoes usuario) permissoes-requeridas)
          (forbidden "Permissão negada!")
          (let [request (assoc request :identity usuario)]
            (handler request)))))))

(defmethod restructure-param :permissoes
  [_ permissoes acc]
  (update-in acc [:middleware] conj [verifica-permissoes permissoes]))

(defmethod restructure-param :usuario
  [_ binding acc]
  (update-in acc [:letks] into [binding `(:identity ~'+compojure-api-request+)]))
