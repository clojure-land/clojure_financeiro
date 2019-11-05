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
       (+ (quot (System/currentTimeMillis) 1000) (* 60 60 24 1)))) ; 1 Dia

(defn sign [msg password]
  (-> (mac/hash msg {:key (str secret password) :alg :hmac+sha256})
      (codecs/bytes->hex)))

(defn token-valido?
  [payload passw token-sign]
  (= token-sign (sign payload passw)))

(defn- parse-token
  [token]
  (let [[payload token-sign] (cstr/split token #"\.")
        usuario (zipmap [:id :login :permissoes :expiracao]
                        (cstr/split payload #":"))
        passw (controller/get-senha-by-id (:id usuario))]
    (if (token-valido? payload passw token-sign)
      usuario
      nil)))

(defn- parse-header [request token-name]
  (some->> (some-> (find-header request "authorization")
                   (second))
           (re-find (re-pattern (str "^" token-name " (.+)$")))
           (second)))

(defn- not-expire?
  [usuario]
  (let [now (quot (System/currentTimeMillis) 1000)]
    (if (< now (-> usuario :expiracao utils/parse-int))
      usuario
      nil)))

(defn- permissao?
  [permissoes permissoes-requeridas]
  (let [permissoes-split (cstr/split permissoes #"/")
        matched-roles (clojure.set/intersection (into #{} permissoes-split) permissoes-requeridas)]
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
