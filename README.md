# credit-recovery-service

> **Work in progress** — projeto de estudo em desenvolvimento ativo.

Serviço backend que gerencia o ciclo de vida de dívidas em recuperação de
crédito: registro de débitos, régua de cobrança automatizada, negociação de
acordos de parcelamento e baixa de pagamentos.

Projeto desenvolvido para praticar, em um domínio realista, padrões de
arquitetura e engenharia aplicados a sistemas financeiros.

## Domínio

O sistema modela o processo de recuperação de crédito:

- **Devedor** — pessoa ou empresa com débito em aberto.
- **Dívida** — valor devido, com status que evolui por uma máquina de estados.
- **Régua de Cobrança** — sequência de ações disparadas ao longo do tempo
  enquanto a dívida não é quitada.
- **Acordo** — negociação que parcela a dívida em novas condições.
- **Pagamento** — baixa total ou de parcela, com garantia de idempotência.

### Máquina de estados da Dívida

    REGISTRADA --> EM_COBRANCA --> EM_NEGOCIACAO --> ACORDADA --> QUITADA
                        |
                        v
                INADIMPLENTE_CRITICA

## Stack

- **Java 21** (LTS)
- **Spring Boot 4** / Spring Framework 7
- **PostgreSQL** — persistência relacional
- **Apache Kafka** — arquitetura orientada a eventos
- **Maven** — build
- **Docker / Docker Compose** — ambiente local
- **Kubernetes / Helm** — orquestração
- **JUnit 5, Mockito, Testcontainers** — testes
- **Prometheus / Grafana** — observabilidade

## Arquitetura

Arquitetura hexagonal (ports & adapters), com o domínio isolado de
frameworks e infraestrutura. As decisões técnicas estão registradas como
ADRs em `/docs/adr`.

## Status

Em construção. Acompanhe a evolução pelos commits e pelos ADRs.

## Como rodar

Instruções de execução serão adicionadas conforme o ambiente for montado.

## Licença

MIT