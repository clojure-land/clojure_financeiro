# financeiro

Projeto baseado no Livro da Casa do Código - Programação Funcional - Uma introdução em Clojure de Gregório Melo

Features: 

1. Inclusão de banco relacional - mysql
2. Inclusão de novos endpoints: DELETE

## Use

1. Clojure 1.10.0
    1.1. Compojure (https://github.com/weavejester/compojure) + Ring (https://github.com/ring-clojure)
    1.2. Korma (https://github.com/korma/Korma)
    1.3. Cheshire (https://github.com/dakrone/cheshire)
    1.4. Clj-http (https://github.com/dakrone/clj-http)
2. Docker
3. Mysql


## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

Install Docker 

https://docs.docker.com/install/

Init Mysql in docker

docker -d -p 3306:3306 --name clojure-mysql -e MYSQL_ROOT_PASSWORD=transacoes -e MYSQL_DATABASE=transacoes mysql:5.7.28

After install, create tables:

Inspect docker container: 

$ docker exec -it a255b0f46647 bash 

a255b0f46647 is ID the container, see at list containers by command: docker ps after run.

After -> 

$ exec mysql -uroot -p transacoes
- Set pass transacoes

then -> 

CREATE TABLE transacoes (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tipo` varchar(10) NOT NULL,
  `valor` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE rotulos (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `description` varchar(100) NULL,
    `transacoes_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (transacoes_id)
    REFERENCES transacoes(id) ON DELETE CASCADE 
);

## Running

To test unit and accepts: 

    leign midje 

To start a web server for the application, run:

    lein ring server-headless


$ curl http://localhost:3000/saldo

$ curl http://localhost:3000/transacoes

$ curl http://localhost:3000/receitas

$ curl http://localhost:3000/despesas

$ curl http://localhost:3000/transacoes?rotulos=salario

$ curl -X POST -d '{"valor": 800, "tipo": "receita"}' -H "Content-Type: application/json" localhost:3000/transacoes

$ curl -X POST -d '{"valor": 800, "tipo": "receita", "rotulos": ["salario", "empresa"]}' -H "Content-Type: application/json" localhost:3000/transacoes


## Studying 

http://www.4clojure.com/

https://kimh.github.io/clojure-by-example/

https://www.braveclojure.com/

## License

Copyright © 2019 FIXME
