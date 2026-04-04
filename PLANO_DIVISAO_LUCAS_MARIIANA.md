# Plano de Trabalho - EDA2

## Integrantes

- Lucas
- Mariiana

## Tema do Projeto

Sistema de Gestao de Clientes de uma Loja de Departamentos com foco em cancelamento de cartoes.

## Objetivo do arquivo

Este documento organiza exatamente:

- o que Lucas vai fazer;
- o que Mariiana vai fazer;
- a ordem das entregas;
- o que precisa estar pronto em cada etapa.

---

## Regras de organizacao do time

- Cada tarefa concluida deve ser marcada com `[x]`.
- Sempre que alguem finalizar uma parte, avisar a outra pessoa antes de seguir.
- Nao alterar a parte da outra pessoa sem combinar.
- Ao final de cada etapa, validar juntos se o sistema ainda executa.

---

## Etapa 1 - Base do projeto (OO e estrutura inicial)

**Meta:** deixar o esqueleto do sistema pronto e padronizado.

### Lucas faz

- [x] Criar classes de entidade (`src/main/java/br/unb/eda2/loja/dominio/`):
  - [x] `Cliente`
  - [x] `Cartao`
  - [x] `SolicitacaoCancelamento`
- [x] Criar enums de status:
  - [x] `StatusCartao`
  - [x] `StatusSolicitacao`
- [x] Definir atributos obrigatorios e validacoes basicas (classe `util/Validadores`: CPF 11 digitos, id > 0, e-mail se informado; unicidade de id fica para o repositorio).

### Mariiana faz

- [x] Criar interfaces de repositorio:
  - [x] `ClienteRepository`
  - [x] `CartaoRepository`
  - [x] `SolicitacaoRepository`
- [x] Criar metodos CRUD basicos para testes iniciais.
- [x] Preparar menu inicial (ou classe principal) com opcoes de cadastro e busca simples.

### Entregavel da etapa

- [x] Estrutura OO funcionando.
- [x] Cadastro e leitura basica de clientes/cartoes.

---

## Etapa 2 - Estruturas principais (Hashing e Arvore)

**Meta:** garantir performance e organizacao dos dados.

### Lucas faz (Hashing)

- Implementar `TabelaHashClientes` (chave por CPF ou id).
- Implementar `TabelaHashCartoes` (chave por idCartao).
- Criar metodos:
  - `inserir`
  - `buscar`
  - `remover`
- Testar casos de colisao (encadeamento ou sondagem, conforme decidido).

### Mariiana faz (Arvore)

- [x] Implementar `ArvoreClientesPorNome` (ou outra chave ordenada).
- [x] Implementar insercao e busca na arvore.
- [x] Implementar percurso `in-order` para relatorios ordenados.
- [x] Integrar arvore com os dados de cliente.

### Entregavel da etapa

- Buscas rapidas por hash.
- Relatorios ordenados por arvore.

---

## Etapa 3 - Regra de negocio de cancelamento

**Meta:** implementar o fluxo principal do trabalho.

### Lucas faz

- Criar `CancelamentoService`.
- Implementar abertura de solicitacao de cancelamento.
- Aplicar validacoes:
  - cartao existe?
  - cartao ja esta cancelado?
  - cliente ativo?
- Atualizar status do cartao ao concluir cancelamento.

### Mariiana faz

- Implementar fluxo de analise da solicitacao:
  - em analise
  - aprovada
  - recusada
- Criar historico de alteracoes da solicitacao.
- Criar tela/menu para consultar solicitacoes por status.

### Entregavel da etapa

- Fluxo completo de cancelamento funcionando ponta a ponta.

---

## Etapa 4 - Conteudo exigido da disciplina (buscas sequenciais)

**Meta:** implementar todos os metodos pedidos pelo professor.

### Lucas faz

- Busca sequencial normal.
- Busca sequencial com sentinela.
- Busca sequencial com mover para frente.
- Criar comparacao de numero de comparacoes entre os 3 metodos.

### Mariiana faz

- Busca sequencial com transposicao.
- Busca sequencial indexada com indice primario.
- Busca sequencial indexada com indice primario e secundario.
- Criar comparacao de numero de comparacoes entre os 3 metodos.

### Entregavel da etapa

- Modulo de testes/benchmark com os 6 algoritmos implementados.

---

## Etapa 5 - Relatorios, testes e documentacao final

**Meta:** fechar com qualidade para entrega e apresentacao.

### Lucas faz

- Escrever documentacao tecnica:
  - arquitetura do sistema
  - justificativa das estruturas (hash + arvore + buscas)
  - analise de complexidade (Big-O)
- Revisar padrao dos nomes de classes e metodos.

### Mariiana faz

- Criar testes de cenarios:
  - cliente nao encontrado
  - cartao ja cancelado
  - solicitacao aprovada/recusada
- Gerar relatorios finais (por status, por motivo, por periodo).
- Separar evidencias para apresentacao (prints, execucoes e exemplos).

### Entregavel da etapa

- Projeto completo, testado e documentado.

---

## Checklist final antes de entregar

- Todas as classes compilam/executam sem erro.
- Todas as estruturas pedidas pelo professor foram implementadas.
- Hashing foi usado em busca principal.
- Arvore foi usada para ordenacao/relatorio.
- Os 6 tipos de busca sequencial foram demonstrados.
- Documento final com explicacao e conclusao pronto.
- Cada integrante sabe explicar a propria parte na apresentacao.

---

## Responsabilidade resumida (versao rapida)

### Lucas

- Entidades e validacoes
- Hashing
- Regras principais de cancelamento
- 3 buscas sequenciais (normal, sentinela, mover para frente)
- Documentacao tecnica e Big-O

### Mariiana

- Repositorios e menu
- Arvore e relatorios ordenados
- Fluxo de analise/historico de solicitacoes
- 3 buscas sequenciais (transposicao, indexada primaria, indexada primaria+secundaria)
- Testes finais e evidencias da apresentacao

