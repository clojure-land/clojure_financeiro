(defproject financeiro "0.5.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [compojure "1.6.1"]
                 [cheshire "5.9.0"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.5.0"]
                 [clj-http "3.10.0"]
                 [metosin/compojure-api "2.0.0-alpha30"]
                 [metosin/spec-tools "0.10.0"]
                 [korma "0.4.3"]
                 [mysql/mysql-connector-java "8.0.18"]
                 [environ "1.1.0"]
                 [buddy/buddy-auth "2.2.0"]
                 [javax.servlet/servlet-api "2.5"]
                 [ring/ring-mock "0.4.0"]
                 [midje "1.9.9"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-jetty-adapter "1.7.1"]]
  :plugins [[lein-ring "0.12.5"]
            [lein-environ "1.1.0"]
            [lein-midje "3.2.1"]
            [lein-cloverage "1.0.13"]
            [lein-ancient "0.6.15"]
            [jonase/eastwood "0.3.3"]
            [lein-kibit "0.1.6"]
            [lein-nvd "0.6.0"]]
  :ring {:handler financeiro.handler/app}
  :profiles
  {:uberjar {:aot :all}
   :dev  {:dependencies [[javax.servlet/servlet-api "2.5"]]
          :env          {:db-classname   "com.mysql.jdbc.Driver"
                         :db-subprotocol "mysql"
                         :db-subname     "//localhost/transacoes"
                         :db-user        "root"
                         :db-delimiters  "`"
                         :db-make-pool  true
                         :db-password    "transacoes"
                         :jwt-secret     "secret-123"}}
   :test {:dependencies [[javax.servlet/servlet-api "2.5"]
                         [ring/ring-mock "0.4.0"]
                         [midje "1.9.9"]
                         [ring/ring-core "1.7.1"]
                         [ring/ring-jetty-adapter "1.7.1"]]
          :env          {:db-classname   "com.mysql.jdbc.Driver"
                         :db-subprotocol "mysql"
                         :db-subname     "//localhost/transacoes_test"
                         :db-user        "root"
                         :db-delimiters  "`"
                         :db-make-pool  true
                         :db-password    "transacoes"
                         :jwt-secret     "teste-123"}
          :plugins      [[lein-midje "3.2.1"]
                         [lein-cloverage "1.0.13"]
                         [lein-ancient "0.6.15"]
                         [jonase/eastwood "0.3.3"]
                         [lein-kibit "0.1.6"]
                         [lein-nvd "0.6.0"]]}
   :testci {:dependencies [[javax.servlet/servlet-api "2.5"]
                         [ring/ring-mock "0.4.0"]
                         [midje "1.9.9"]
                         [ring/ring-core "1.7.1"]
                         [ring/ring-jetty-adapter "1.7.1"]]
          :env          {:db-classname   "com.mysql.jdbc.Driver"
                         :db-subprotocol "mysql"
                         :db-subname     "//mysql/transacoes_test"
                         :db-user        "root"
                         :db-delimiters  "`"
                         :db-make-pool  true
                         :db-password    "transacoes"
                         :jwt-secret     "teste-123"}
          :plugins      [[lein-midje "3.2.1"]
                         [lein-cloverage "1.0.13"]
                         [lein-ancient "0.6.15"]
                         [jonase/eastwood "0.3.3"]
                         [lein-kibit "0.1.6"]
                         [lein-nvd "0.6.0"]]}}
  :uberjar-name "financeiro.jar"
  :test-paths ["test/unitarios" "test/aceitacao"])