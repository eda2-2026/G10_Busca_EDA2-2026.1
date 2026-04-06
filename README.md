# SistemaDeLoja


Número da Lista: 10 <br>
Conteúdo da Disciplina: Busca<br>

## Alunos
|Matrícula | Aluno |
| -- | -- |
| 211063185  | Lucas Ribeiro de Souza |
| 211062796  | Mariiana Siqueira Neris |

## Apresentação

[Link para o vídeo de apresentação]()

## Sobre 
Este projeto implementa um sistema de gerenciamento para uma loja, utilizando **tabelas hash** (encadeamento separado) para busca de clientes e cartões, e uma **árvore binária de busca** (`ArvoreClientesPorNome`) para ordenação alfabética de clientes via percurso **in-order**. O sistema permite cadastro de clientes, gerenciamento de cartões e processamento de solicitações de cancelamento, com histórico de alterações.

Há duas formas de uso: **console** (`LojaApp`) e **interface gráfica Swing** (`LojaGuiApp`). As estruturas são implementadas manualmente para fins educacionais (hashing, colisões, BST).

## Screenshots
Adicione 3 ou mais screenshots do projeto em funcionamento aqui.

1. Tela principal da interface gráfica (abas Clientes / Cartões / Solicitações)
2. Menu de opções no console
3. Listagem ordenada por nome ou fluxo de solicitações na GUI

## Instalação 
Linguagem: Java 17<br>
Interface gráfica: Swing<br>
Pré-requisitos: **JDK 17** (ou superior compatível com `release` 17 do Maven) e **Maven** instalados.

Comandos para instalação e execução:
1. Clone o repositório: `git clone https://github.com/eda2-2026/G10_Busca_EDA2-2026.1.git`
2. Navegue para o diretório: `cd G10_Busca_EDA2-2026.1`
3. Compile o projeto: `mvn compile`
4. **Console:** `mvn exec:java` (usa `LojaApp` por padrão no `pom.xml`)  
   ou explicitamente: `mvn exec:java -Dexec.mainClass="br.unb.eda2.loja.LojaApp"`
5. **Gráfica:** `mvn exec:java@gui`  
   ou: `mvn exec:java -Dexec.mainClass="br.unb.eda2.loja.LojaGuiApp"`

Por padrão a GUI carrega **dados de exemplo** para demonstração. Para iniciar vazia:  
`mvn exec:java@gui -Dexec.args="--sem-demo"`

## Uso 
Na versão **console**, siga o menu para cadastrar clientes, cartões e solicitações de cancelamento.

Na versão **gráfica**, use as abas: **Clientes** (`PainelClientes`), **Cartões** (`PainelCartoes`) e **Solicitações** (`PainelSolicitacoes`) — cancelamento, filtro por status e histórico.

Relatórios agregados (por status, motivo e período) estão implementados em **`RelatorioService`** e na documentação técnica (`DOCUMENTACAO_TECNICA.md`); não há tela dedicada a eles na Swing neste momento.

## Outros 
Testes unitários em `test/`: `mvn test`. Estruturas: `TabelaHashClientes`, `TabelaHashCartoes`, `ArvoreClientesPorNome`; repositórios em memória. Há material sobre colisão em hash no pacote `estrutura` (ex.: `DemonstracaoColisaoTabelaHash`).
