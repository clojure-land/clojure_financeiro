# Projeto Financeiro

Projeto baseado no Livro da Casa do Código - Programação Funcional - Uma introdução em Clojure de Gregório Melo

## Use

* 0.1.0
    1. Clojure "1.10.0"
        * Compojure "1.6.1"
        * Ring "0.4.0"
        * Cheshire "5.8.1"
        * clj-http "3.9.1"
        * Korma "0.4.3"
        * mysql-connector-java "5.1.6"
    2. Docker "18.09.9"
    3. MySql "5.7.28"

* 0.2.0
    1. Clojure "1.10.0"
        * Compojure "1.6.1"
        * Ring "0.4.0"
        * Cheshire "5.8.1"
        * clj-http "3.9.1"
        * Korma "0.4.3"
        * mysql-connector-java "5.1.6"
        * metosin/compojure-api "2.0.0-alpha30"
        * metosin/spec-tools "0.9.2"
    2. Docker "18.09.9"
    3. MySql "5.7.28"
    
* 0.3.0
    1. Clojure "1.10.0"
        * Compojure "1.6.1"
        * Ring "0.4.0"
        * Cheshire "5.8.1"
        * clj-http "3.9.1"
        * Korma "0.4.3"
        * mysql-connector-java "5.1.6"
        * metosin/compojure-api "2.0.0-alpha30"
        * metosin/spec-tools "0.9.2"
        * environ "1.1.0"
    2. Docker "18.09.9"
    3. MySql "5.7.28"

* 0.4.0
    1. Clojure "1.10.0"
        * Compojure "1.6.1"
        * Ring "0.4.0"
        * Cheshire "5.8.1"
        * clj-http "3.9.1"
        * Korma "0.4.3"
        * mysql-connector-java "5.1.6"
        * metosin/compojure-api "2.0.0-alpha30"
        * metosin/spec-tools "0.9.2"
        * environ "1.1.0"
    2. Docker "18.09.9"
    3. MySql "5.7.28"
    
* 0.5.0
    1. Clojure "1.10.0"
        * Compojure "1.6.1"
        * Ring "0.4.0"
        * Cheshire "5.8.1"
        * clj-http "3.9.1"
        * Korma "0.4.3"
        * mysql-connector-java "5.1.6"
        * metosin/compojure-api "2.0.0-alpha30"
        * metosin/spec-tools "0.9.2"
        * environ "1.1.0"
    2. Docker "18.09.9"
    3. MySql "5.7.28"
    4. Kubernetes "1.16.2" / Minikube "1.5.2" / VirtualBox "6" / Kubectl "1.16.2"
    5. Jenkins "2.203-alpine"

* 0.6.0
    1. Clojure "1.10.0"
        * Compojure "1.6.1"
        * Ring "0.4.0"
        * Cheshire "5.8.1"
        * clj-http "3.9.1"
        * Korma "0.4.3"
        * mysql-connector-java "5.1.6"
        * metosin/compojure-api "2.0.0-alpha30"
        * metosin/spec-tools "0.9.2"
        * environ "1.1.0"
    2. Docker "18.09.9"
    3. MySql "5.7.28"
    4. Kubernetes "1.16.2" / Minikube "1.5.2" / VirtualBox "6" / Kubectl "1.16.2"
    5. Jenkins "2.203-alpine"
    6. SonarQube "7.9.1"

## Release History

* 0.1.0
    * Projeto inicial - Financeiro baseado no livro com alteração para executar com bando de dados relacional - MySql;
    * Inclusão do endpoint: DELETE para remoção de transações.
* 0.2.0
    * Inclusão da biblioteca compojure-api;
    * Inclusão do swagger.
* 0.3.0
    * Inclusão da biblioteca Environ para ambientes por profiles;
    * Criação da base de teste para execução dos testes;
    * Inclusão do transactional do korma.
* 0.4.0
    * Inclusão da biblioteca Buddy para inclusão de segurança com JWT - Autenticação e Autorização;
    * Criação das tabelas: usuarios e permissoes;
    * Versionamento dos endpoints;
    * Reestruturação do projeto.
* 0.5.0
    * Inclusão de ambiente CI/CD para deploy continuo.

## Links 

* Compojure (https://github.com/weavejester/compojure)
* Ring (https://github.com/ring-clojure)
* Korma (https://github.com/korma/Korma)
* Cheshire (https://github.com/dakrone/cheshire)
* Clj-http (https://github.com/dakrone/clj-http)
* Midje (https://github.com/marick/Midje)
* Compojure-api (https://github.com/metosin/compojure-api)
* Spec-tools (https://github.com/metosin/spec-tools)
* Environ (https://github.com/weavejester/environ)
* Buddy (https://github.com/funcool/buddy)

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

Install [Docker][] 

[Docker]: https://docs.docker.com/install/

Init Mysql in docker

    docker run -d -p 3306:3306 --name clojure-mysql -e MYSQL_ROOT_PASSWORD=transacoes -e MYSQL_DATABASE=transacoes mysql:5.7.28

After install, create tables:

Inspect docker container: 

    docker exec -it clojure-mysql bash 

-> Entrando no console do mysql

    exec mysql -uroot -p transacoes

- Informando a senha: transacoes

-> Criando as tabelas 

```sql
CREATE TABLE transacoes (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tipo` varchar(10) NOT NULL,
  `valor` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`)
);
```

```sql
CREATE TABLE rotulos (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `description` varchar(100) NOT NULL,
    `transacoes_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (transacoes_id)
    REFERENCES transacoes(id) ON DELETE CASCADE 
);
```

```sql
CREATE TABLE usuarios (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(10) NOT NULL UNIQUE,
  `senha` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);
```
As permissões para este escopo poderão ser: SELECT / INSERT / UPDATE / DELETE

```sql
CREATE TABLE permissoes (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(10) NOT NULL,
    `usuarios_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (usuarios_id)
    REFERENCES usuarios(id) ON DELETE CASCADE 
);
```

Inserindo os usuários, sendo que o adm tem acesso total e o usuario tem apenas acesso de leitura

```sql
INSERT INTO `usuarios` (login,senha) VALUES ('adm','123456');
SET @last_id_in_usuarios = LAST_INSERT_ID();
INSERT INTO `permissoes` (name,usuarios_id) VALUES ('SELECT',@last_id_in_usuarios);
INSERT INTO `permissoes` (name,usuarios_id) VALUES ('INSERT',@last_id_in_usuarios);
INSERT INTO `permissoes` (name,usuarios_id) VALUES ('UPDATE',@last_id_in_usuarios);
INSERT INTO `permissoes` (name,usuarios_id) VALUES ('DELETE',@last_id_in_usuarios);
INSERT INTO `usuarios` (login,senha) VALUES ('usuario','123456');
SET @last_id_in_usuarios = LAST_INSERT_ID();
INSERT INTO `permissoes` (name,usuarios_id) VALUES ('SELECT',@last_id_in_usuarios);

```

-> Criando o banco de teste

    create database transacoes_test;

-> Acessando o banco de dados de teste

    use transacoes_test;

-> Criando as tabelas novamente, igual ao passo anterior no banco de teste    

-> Saindo

    exit

## Running

*A partir da versão 0.3.0 é necessário inserir o profile de test, conforme exemplo abaixo*

    lein with-profile test midje

To test unit and accepts: 

    leign midje
   
Somente testes de aceitação

    leign midje :filters aceitacao 

Somente testes de unitarios

    leign midje :filters -aceitacao
    
Cobertura de teste

    lein cloverage --runner :midje

To start a web server for the application, run:

    lein ring server-headless

## Test with curl (Até a versão 0.3.0)

    curl http://localhost:3000/saldo

    curl http://localhost:3000/transacoes

    curl http://localhost:3000/receitas

    curl http://localhost:3000/despesas

    curl http://localhost:3000/transacoes?rotulos=salario

    curl -X POST -d '{"valor": 800, "tipo": "receita"}' -H "Content-Type: application/json" http://localhost:3000/transacoes

    curl -X POST -d '{"valor": 800, "tipo": "receita", "rotulos": ["salario", "empresa"]}' -H "Content-Type: application/json" http://localhost:3000/transacoes

## Test with swagger (Todas as versões)

    http://localhost:3000     

## Studying 

http://www.4clojure.com/ 

https://kimh.github.io/clojure-by-example/

https://www.braveclojure.com/

https://hub.docker.com/_/clojure

https://minikube.sigs.k8s.io/docs/reference/persistent_volumes/

https://www.elastic.co/guide/en/elasticsearch/reference/current/vm-max-map-count.html

https://github.com/fsantiag/sonar-clojure

## License

Copyright © 2019 FIXME