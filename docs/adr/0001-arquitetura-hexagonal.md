# ADR-0001 — Arquitetura Hexagonal e ausência de Lombok

- **Status:** Aceito
- **Data:** 2026-05-25

## Contexto

O `credit-recovery-service` modela um domínio de negócio com regras
próprias — máquina de estados de dívidas, condições de acordo, idempotência
de pagamento. Esse núcleo de regras precisa evoluir sem ficar acoplado a
detalhes de infraestrutura (banco de dados, mensageria, framework web), que
mudam por motivos diferentes e em ritmos diferentes.

Era necessário decidir, no início do projeto, como organizar o código para
manter essa separação de forma explícita e verificável.

## Decisão

Adotar **arquitetura hexagonal** (ports & adapters), com três camadas:

- **domain** — entidades e regras de negócio. Java puro, sem dependência de
  framework. Define *portas* (interfaces) para o que precisa do mundo
  externo.
- **application** — casos de uso que orquestram o domínio. Depende apenas de
  `domain`.
- **infrastructure** — adaptadores que implementam as portas: web (REST),
  persistência (JPA), mensageria (Kafka).

Regra de dependência: as dependências apontam sempre para dentro
(`infrastructure` → `application` → `domain`). O domínio não conhece a
infraestrutura.

Decisão complementar: **não usar Lombok**. Para classes imutáveis (DTOs,
eventos, value objects) será usado `record`, nativo do Java. Entidades de
domínio são escritas explicitamente. Entidades JPA, quando necessário, terão
getters/setters e `equals`/`hashCode` escritos à mão, baseados no
identificador.

## Justificativa

- Isola o núcleo de negócio de mudanças de infraestrutura.
- Aplica o princípio de Inversão de Dependência (SOLID): o domínio depende
  de abstrações que ele mesmo define, não de implementações concretas.
- Torna o domínio testável sem subir banco, broker ou contexto web.
- A ausência de Lombok mantém o projeto sem manipulação de bytecode em tempo
  de compilação; todo o código é explícito e revisável. O ganho de Lombok
  (redução de boilerplate) é pequeno no escopo deste projeto e não compensa
  a perda de transparência.

## Consequências

**Positivas**
- Domínio testável de forma rápida e isolada.
- Trocar um adaptador (ex.: outro banco) não afeta o domínio.
- Estrutura comunica a intenção de arquitetura a quem lê o código.

**Negativas / custos**
- Mais classes e mapeamentos (ex.: domínio ↔ entidade JPA) do que numa
  abordagem em camadas simples.
- Exige disciplina para não vazar dependências de infraestrutura para
  dentro do domínio.

## Alternativas consideradas

- **Arquitetura em camadas tradicional (controller → service → repository):**
  mais simples, porém tende a acoplar regra de negócio ao framework de
  persistência. Descartada por enfraquecer o isolamento do domínio.
- **Usar Lombok nas entidades JPA:** decisão válida em projetos maiores, mas
  descartada aqui pelos motivos descritos acima.