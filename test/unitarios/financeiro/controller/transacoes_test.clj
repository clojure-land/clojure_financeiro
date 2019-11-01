(ns financeiro.controller.transacoes-test
  (:require [midje.sweet :refer :all]
            [financeiro.controller.transacoes :refer :all]))

(facts "Guarda uma transação na base de dados"
   (against-background [(before :facts (limpar))]
   (fact "a coleção de transações inicia vazia"
         (count (find-all)) => 0)
   (fact "agora a coleção deve ser 1"
         (registrar [] "receita" 10)
         (count (find-all)) => 1)))

(facts "Calcula o saldo dada uma coleção de transações"
   (against-background [(before :facts (limpar))]
                       (fact "saldo é positivo quando só tem receita"
                             (registrar [] "receita" 1)
                             (registrar [] "receita" 10)
                             (registrar [] "receita" 100)
                             (registrar [] "receita" 1000)
                             (saldo) => 1111.00M)

                       (fact "saldo é negativo quando só tem despesa"
                             (registrar [] "despesa" 2)
                             (registrar [] "despesa" 20)
                             (registrar [] "despesa" 200)
                             (registrar [] "despesa" 2000)
                             (saldo) => -2222.00M)

                       (fact "saldo é a soma das receitas menos a soma das despesas"
                             (registrar [] "despesa" 2)
                             (registrar [] "receita" 10)
                             (registrar [] "despesa" 200)
                             (registrar [] "receita" 1000)
                             (saldo) => 808.00M)))

(facts "filtra transações por tipo"
   (def transacoes-aleatorias '({:valor 2 :tipo "despesa"}
                                {:valor 10 :tipo "receita"}
                                {:valor 200 :tipo "despesa"}
                                {:valor 1000 :tipo "receita"}))

   (against-background [(before :facts [(limpar)
                                        (doseq [transacao transacoes-aleatorias]
                                          (registrar [] (:tipo transacao) (:valor transacao)))])]
                       (fact "encontra apenas as receitas"
                             (count (transacoes-do-tipo "receita")) => 2)
                       (fact "encontra apenas as despesas"
                             (count (transacoes-do-tipo "despesa")) => 2)))

(facts "filtra transações por rótulo"
   (def transacoes-aleatorias '({:valor 7.0M :tipo "despesa" :rotulos ["sorvete" "entretenimento"]}
                                {:valor 88.0M :tipo "despesa" :rotulos ["livro" "educacao"]}
                                {:valor 106.0M :tipo "despesa" :rotulos ["curso" "educacao"]}
                                {:valor 8000.0M :tipo "receita" :rotulos ["salario"]}))
   (against-background
     [(before :facts [(limpar) (doseq [transacao transacoes-aleatorias]
                                 (registrar (:rotulos transacao) (:tipo transacao) (:valor transacao)))])]
     (fact "encontra a transação com rótulo 'salario'"
           (count (transacoes-com-filtro {:rotulos "salario"})) => 1)
     (fact "encontra as 2 transações com rótulo 'educacao'"
           (count (transacoes-com-filtro {:rotulos ["educacao"]})) => 2)
     (fact "encontra as 2 transações com rótulo 'livro' ou 'curso'"
           (count (transacoes-com-filtro {:rotulos ["livro" "curso"]})) => 2)))