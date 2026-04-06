# Documentação técnica — Loja Departamentos (EDA2)

## Arquitetura (visão geral)

- **Domínio (`br.unb.eda2.loja.dominio`)**: entidades e enums do problema (`Cliente`, `Cartao`, `SolicitacaoCancelamento`, status e histórico).
- **Estruturas (`br.unb.eda2.loja.estrutura`)**:
  - `TabelaHashClientes` e `TabelaHashCartoes`: base para busca rápida.
  - `ArvoreClientesPorNome`: ordenação e consultas por nome.
- **Repositórios (`br.unb.eda2.loja.repositorio`)**: interfaces e implementações em memória (`memoria/*`) para persistência.
- **Serviços (`br.unb.eda2.loja.servico`)**:
  - `CancelamentoService`: regras de negócio do fluxo de cancelamento.
  - `RelatorioService`: relatórios finais (por status, por motivo e por período).
- **UI (`br.unb.eda2.loja.ui`)**: interface gráfica (Swing) e componentes.
- **Apps**:
  - `LojaApp`: modo terminal.
  - `LojaGuiApp`: modo GUI.
- **Demo (`br.unb.eda2.loja.demo.DadosExemplo`)**: carrega um cenário fixo para apresentação.

## Justificativa das estruturas (Hash + Árvore)

- **Hash**:
  - Usada para as operações principais de busca por chave (ex.: id do cliente/cartão).
  - Objetivo: tempo médio \(O(1)\) para inserir/buscar/remover em cenários típicos.
- **Árvore (clientes por nome)**:
  - Usada para listagens/relatórios ordenados.
  - Objetivo: manter a visualização/relatórios em ordem alfabética sem precisar ordenar toda vez.

## Análise de complexidade (Big-O)

### Hash (`TabelaHashClientes`, `TabelaHashCartoes`)

- **Inserir / Buscar / Remover**:
  - **Médio**: \(O(1)\)
  - **Pior caso** (muitas colisões): \(O(n)\)

### Árvore (`ArvoreClientesPorNome`)

- **Inserção / Busca / Remoção**:
  - **Médio**: \(O(\log n)\) (em árvores balanceadas; a implementação pode degradar se ficar desbalanceada)
  - **Pior caso**: \(O(n)\)
- **Percurso in-order**:
  - \(O(n)\) para visitar todos os nós, retornando clientes ordenados.

## Relatórios finais (Etapa 5)

Foram implementados em `br.unb.eda2.loja.servico.RelatorioService`:

- **Por status**: contagem de solicitações em cada `StatusSolicitacao`.
- **Por motivo**: contagem por motivo normalizado (trim + lower-case pt-BR).
- **Por período**: lista de solicitações entre duas datas (inclusivo) e contagem por motivo no período.

## Testes de cenários (Etapa 5)

Os testes unitários ficam em `src/test/java` e cobrem:

- **Cliente não encontrado** (cartão “órfão”).
- **Cartão já cancelado**.
- **Fluxo aprovado** (cartão cancelado e solicitação concluída).
- **Fluxo recusado** (cartão permanece ativo e solicitação recusada).

### Como executar

- Testes:
  - `mvn test`
- App terminal:
  - `mvn compile exec:java`
- App GUI:
  - `mvn compile exec:java@gui`

