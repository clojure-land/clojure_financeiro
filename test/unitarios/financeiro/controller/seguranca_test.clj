(ns financeiro.controller.seguranca-test
  (:require [midje.sweet :refer :all]
            [financeiro.controller.seguranca :refer :all]))

(facts "Validando busca por login e senha"
       (fact "passando login e senha errado (não cadastrado no banco) deve retornar nulo"
             (find-by-login-e-senha "adm" "12345") => nil)

       (fact "passando login e senha errado (cadastrado no banco) deve retornar não nulo"
             (:login (find-by-login-e-senha "adm" "123456")) => "adm"))

(facts "Validando buscar a senha pelo id"
       (fact "passando um id errado (não cadastrado no banco) deve retornar nulo"
             (get-senha-by-id -1) => nil)

       (fact "passando um id existente (cadastrado no banco) deve retornar não nulo"
             (get-senha-by-id 1) => "123456"))