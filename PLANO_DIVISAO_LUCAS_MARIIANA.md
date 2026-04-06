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

### Mariiana faz (Hashing e Arvore)

- [x] Implementar `TabelaHashClientes` (chave por CPF ou id).
- [x] Implementar `TabelaHashCartoes` (chave por idCartao).
- [x] Criar metodos:
  - [x] `inserir`
  - [x] `buscar`
  - [x] `remover`
- [x] Testar casos de colisao (encadeamento ou sondagem, conforme decidido).
- [x] Implementar `ArvoreClientesPorNome` (ou outra chave ordenada).
- [x] Implementar insercao e busca na arvore.
- [x] Implementar percurso `in-order` para relatorios ordenados.
- [x] Integrar arvore com os dados de cliente.

### Entregavel da etapa

- [x] Buscas rapidas por hash.
- [x] Relatorios ordenados por arvore.

---

## Etapa 3 - Regra de negocio de cancelamento

**Meta:** implementar o fluxo principal do trabalho.

### Lucas faz

- [x] Criar `CancelamentoService`.
- [x] Implementar abertura de solicitacao de cancelamento.
- [x] Aplicar validacoes:
  - [x] cartao existe?
  - [x] cartao ja esta cancelado?
  - [x] cliente ativo?
- [x] Atualizar status do cartao ao concluir cancelamento
- [x] Implementar fluxo de analise da solicitacao:
  - [x] em analise
  - [x] aprovada
  - [x] recusada
- [x] Criar historico de alteracoes da solicitacao.
- [x] Criar tela/menu para consultar solicitacoes por status.

### Entregavel da etapa

- [x] Fluxo completo de cancelamento funcionando ponta a ponta.

---

## Etapa 4 - Interface grafica (apresentacao)

**Meta:** deixar o sistema mais claro para demonstrar na apresentacao, alem do menu no terminal.

### Lucas faz

- [x] Escolher stack com o grupo (ex.: Java Swing, JavaFX ou outra acordada com a disciplina).
- [x] Definir telas ou fluxos principais (cadastro, buscas, cancelamento, listagens).
- [x] Integrar a camada de servico/repositorio existente na interface (sem duplicar regra de negocio).
- [x] Tratamento basico de erros na UI (mensagens ao usuario, campos invalidos).
- [x] Layout, navegacao e consistencia visual (cores, espacamentos, titulos).
- [x] Formularios e tabelas/listas para exibir resultados (clientes, cartoes, solicitacoes).
- [x] Garantir que fluxos criticos do cancelamento fiquem faceis de mostrar ao professor.

### Entregavel da etapa

- [x] Aplicacao com interface grafica executavel, cobrindo os fluxos principais do sistema.
- [x] Classe principal: `br.unb.eda2.loja.LojaGuiApp` — executar: `mvn compile exec:java@gui` (terminal continua com `mvn compile exec:java`).
- [x] `LojaApp` (terminal) pode permanecer como opcao de teste rapido ou ser desativado depois — combinar no grupo.

---

## Etapa 5 - Relatorios, testes e documentacao final

**Meta:** fechar com qualidade para entrega e apresentacao.

### Lucas faz

- [x] Montar cenario de **dados de exemplo** para testes e demo (`br.unb.eda2.loja.demo.DadosExemplo`): clientes, cartoes e solicitacoes em varios status; carrega ao iniciar por padrao; usar argumento `--sem-demo` no `main` para comecar sem dados.

### Mariiana faz

- [x] Escrever documentacao tecnica:
  - [x] arquitetura do sistema
  - [x] justificativa das estruturas (hash + arvore)
  - [x] analise de complexidade (Big-O)
- [x] Revisar padrao dos nomes de classes e metodos.
- [x] Criar testes de cenarios:
  - [x] cliente nao encontrado
  - [x] cartao ja cancelado
  - [x] solicitacao aprovada/recusada
- [x] Gerar relatorios finais (por status, por motivo, por periodo).
- [x] Separar evidencias para apresentacao (prints, execucoes e exemplos).

### Entregavel da etapa

- [x] Projeto completo, testado e documentado.
- [x] Roteiro de testes usando os dados de exemplo (e casos de erro manual).

---

## Checklist final antes de entregar

- [x] Todas as classes compilam/executam sem erro.
- [x] Todas as estruturas combinadas com o professor foram implementadas (hash + arvore + dominio).
- [x] Hashing foi usado em busca principal.
- [x] Arvore foi usada para ordenacao/relatorio.
- [x] Interface grafica pronta para demonstrar o sistema na apresentacao.
- [x] Documento final com explicacao e conclusao pronto.
- [x] Cada integrante sabe explicar a propria parte na apresentacao.

---

## Responsabilidade resumida (versao rapida)

### Lucas

- Entidades e validacoes
- Hashing
- Regras principais de cancelamento
- Integracao da interface grafica com servicos/repositorios
- Dados de exemplo (`DadosExemplo`) e opcao `--sem-demo`
- Documentacao tecnica e Big-O

### Mariiana

- Repositorios e menu (terminal)
- Arvore e relatorios ordenados
- Fluxo de analise/historico de solicitacoes
- Layout, telas e roteiro de demo na interface grafica
- Testes finais e evidencias da apresentacao

