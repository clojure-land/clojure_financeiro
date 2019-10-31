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
    2. Docker
    3. MySql

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
    2. Docker
    3. MySql

## Release History

* 0.1.0
    * Projeto inicial - Financeiro baseado no livro com alteração para executar com bando de dados relacional - MySql;
    * Inclusão do endpoint: DELETE para remoção de transações.
* 0.2.0
    * Inclusão da biblioteca compojure-api;
    * Inclusão do swagger.
* 0.3.0
    * em dev :)

## Links 

* Compojure (https://github.com/weavejester/compojure)
* Ring (https://github.com/ring-clojure)
* Korma (https://github.com/korma/Korma)
* Cheshire (https://github.com/dakrone/cheshire)
* Clj-http (https://github.com/dakrone/clj-http)
* Midje (https://github.com/marick/Midje)
* Compojure-api (https://github.com/metosin/compojure-api)
* Spec-tools (https://github.com/metosin/spec-tools)


## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

Install Docker 

[Docker]: https://docs.docker.com/install/

Init Mysql in docker

    docker -d -p 3306:3306 --name clojure-mysql -e MYSQL_ROOT_PASSWORD=transacoes -e MYSQL_DATABASE=transacoes mysql:5.7.28

After install, create tables:

Inspect docker container: 

    docker exec -it a255b0f46647 bash 

a255b0f46647 is ID the container, see at list containers by command: docker ps after run.

-> 

    exec mysql -uroot -p transacoes

- Set pass transacoes

then -> 

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
    `description` varchar(100) NULL,
    `transacoes_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (transacoes_id)
    REFERENCES transacoes(id) ON DELETE CASCADE 
);
```

## Running

To test unit and accepts: 

    leign midje 

To start a web server for the application, run:

    lein ring server-headless


## Test with curl 

    curl http://localhost:3000/saldo

    curl http://localhost:3000/transacoes

    curl http://localhost:3000/receitas

    curl http://localhost:3000/despesas

    curl http://localhost:3000/transacoes?rotulos=salario

    curl -X POST -d '{"valor": 800, "tipo": "receita"}' -H "Content-Type: application/json" http://localhost:3000/transacoes

    curl -X POST -d '{"valor": 800, "tipo": "receita", "rotulos": ["salario", "empresa"]}' -H "Content-Type: application/json" http://localhost:3000/transacoes


## Studying 

http://www.4clojure.com/

https://kimh.github.io/clojure-by-example/

https://www.braveclojure.com/

## License

Copyright © 2019 FIXME
