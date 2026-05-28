# ADR-0002 — Schema management: ddl-auto=update temporário, Flyway adiado

- **Status:** Aceito (temporário)
- **Data:** 2026-05-27
- **Revisão prevista:** ao migrar para ambiente CI/CD ou produção

## Contexto

A intenção inicial da camada de persistência (ver micro-step 2.2 do projeto)
era usar **Flyway** para gestão de schema versionado, com Hibernate
configurado em `ddl-auto=validate`. Essa é a abordagem padrão para sistemas
financeiros e ambientes regulados, onde cada alteração de banco precisa ser
auditável, reproduzível e revisada em pull request.

Durante a configuração inicial, identifiquei uma incompatibilidade entre
**Spring Boot 4.0.6** e **Flyway 11.14.1**: a autoconfiguração do Flyway
não foi disparada em nenhuma combinação testada (propriedades em
`application.properties`, bean explícita em `@Configuration` com
`@Bean(initMethod = "migrate")`, e variações de ordem). O log da aplicação
não apresentava nenhuma linha do Flyway, mesmo com a dependência presente
no classpath (confirmado via `mvn dependency:tree`) e a classe de
configuração compilada (confirmado via inspeção em `target/classes`).

Spring Boot 4 foi lançado em novembro de 2025 e tem mudanças significativas
em módulos de autoconfiguração — o Flyway parece ser um dos casos não
totalmente estabilizados no momento desta decisão.

## Decisão

Adotar, em ambiente local de desenvolvimento, a seguinte configuração:

- `spring.jpa.hibernate.ddl-auto=update` — Hibernate cria e atualiza
  tabelas a partir das entidades `@Entity`.
- Remover temporariamente as dependências do Flyway do `pom.xml`.
- Remover temporariamente a classe `FlywayConfig` (renomeada para `.bak`).

O arquivo `src/main/resources/db/migration/V1__schema_inicial.sql`
**permanece no repositório**, versionado, como documentação do schema
oficial pretendido.

## Justificativa

- **Desbloquear progresso do projeto.** A engenharia da camada de
  persistência (entidades JPA, mappers, adaptador, porta no domínio) está
  pronta e correta — paralisar o projeto por um bug de ferramenta seria
  perda desproporcional de tempo.
- **Trade-off consciente e limitado.** A decisão se aplica apenas a
  ambiente local. Em produção, `ddl-auto=update` é proibido por motivos de
  auditoria e controle de mudanças.
- **Reversibilidade alta.** O caminho de volta para Flyway está mapeado: o
  V1 está escrito, a `FlywayConfig` está preservada, e o `application.properties`
  só precisa de uma linha alterada (`validate` no lugar de `update`).
- **Honestidade técnica.** Esconder o problema com configuração mais
  complexa, ou abandonar o conceito de migrations versionadas, seriam piores
  do que registrar a decisão.

## Consequências

**Positivas**
- Projeto destravado, demais micro-steps (casos de uso, API REST, testes
  de integração) podem prosseguir.
- A migration `V1__schema_inicial.sql` continua no repositório, servindo
  como documentação executável do schema esperado.

**Negativas / custos**
- O schema atual em desenvolvimento foi gerado pelo Hibernate, e pode ter
  divergências sutis do V1 escrito à mão (nomes de constraint, ordem de
  colunas, defaults). Será necessário revisar antes da reativação do Flyway.
- Warning recorrente nos logs do tipo `constraint "ukXxx..." of relation
  "devedor" does not exist, skipping` — consequência típica de `update`
  tentando reconciliar estado entre execuções; não afeta o funcionamento.
- Esta decisão não pode ser propagada para CI/CD nem produção sem revisão.

## Plano de migração de volta ao Flyway

1. Acompanhar releases do Spring Boot 4 (issues no GitHub do projeto) e
   atualizações do Flyway que mencionem compatibilidade com Boot 4.
2. Quando houver versão compatível confirmada:
   a. Comparar o schema gerado pelo Hibernate com o `V1__schema_inicial.sql`
   atual e ajustar o V1 se necessário.
   b. Recriar a `FlywayConfig` (renomear `.bak` de volta) ou usar
   autoconfiguração se já funcionar.
   c. Adicionar de volta as dependências do Flyway no `pom.xml`.
   d. Trocar `ddl-auto=update` para `ddl-auto=validate`.
   e. Recriar o banco do zero (`docker compose down -v && up -d`) e
   validar que o Flyway aplica o V1 e o Hibernate valida com sucesso.
3. Esta validação **deve ocorrer obrigatoriamente** antes de qualquer
   deploy em ambiente compartilhado.

## Alternativas consideradas

- **Persistir tentando configurar o Flyway:** descartada por não haver
  evidência clara do motivo da autoconfiguração falhar — investigação
  adicional teria custo aberto, sem garantia de sucesso no tempo previsto.
- **Downgrade para Spring Boot 3.x:** descartada porque a escolha de Boot 4
  é deliberada (versão estável atual; pratica stack moderna). Voltar para
  versão anterior eliminaria parte do valor de aprendizado do projeto.
- **Trocar Flyway por Liquibase:** descartada porque a complexidade
  adicional não se justifica num projeto de portfólio; e Liquibase também
  poderia apresentar problemas similares no mesmo ecossistema imaturo.