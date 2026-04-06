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
Este projeto implementa um sistema de gerenciamento para uma loja, utilizando estruturas de dados avançadas como tabelas hash para armazenamento eficiente de cartões e clientes, e árvores para ordenação de clientes por nome. O sistema permite o cadastro de clientes, gerenciamento de cartões de crédito/débito, e processamento de solicitações de cancelamento, com histórico de alterações. 

O projeto inclui duas interfaces: uma versão console (LojaApp) e uma versão gráfica (LojaGuiApp) utilizando Java Swing. As estruturas de dados são implementadas manualmente para fins educacionais, demonstrando conceitos de hashing, colisões e balanceamento de árvores.

## Screenshots
Adicione 3 ou mais screenshots do projeto em funcionamento aqui.

1. Tela principal da interface gráfica
2. Menu de opções no console
3. Relatório de clientes

## Instalação 
Linguagem: Java<br>
Framework: Swing (para interface gráfica)<br>
Pré-requisitos: Java 8 ou superior, Maven instalado.

Comandos para instalação e execução:
1. Clone o repositório: `git clone https://github.com/eda2-2026/G10_Busca_EDA2-2026.1.git`
2. Navegue para o diretório: `cd G10_Busca_EDA2-2026.1`
3. Compile o projeto: `mvn compile`
4. Execute a versão console: `mvn exec:java -Dexec.mainClass="br.unb.eda2.loja.LojaApp"`
5. Execute a versão gráfica: `mvn exec:java -Dexec.mainClass="br.unb.eda2.loja.LojaGuiApp"`

## Uso 
Após executar o comando de instalação, o sistema estará rodando. Na versão console, siga as opções do menu para cadastrar clientes, gerenciar cartões e processar solicitações. Na versão gráfica, utilize os painéis para navegar pelas funcionalidades: PainelClientes para gerenciar clientes, PainelCartoes para cartões, e PainelSolicitacoes para solicitações de cancelamento.

Para gerar relatórios, utilize as opções disponíveis no menu ou interface gráfica.

## Outros 
O projeto inclui testes unitários na pasta `test/`, que podem ser executados com `mvn test`. As estruturas de dados implementadas incluem TabelaHash para cartões e clientes, ArvoreClientesPorNome para ordenação, e demonstrações de colisões em tabelas hash. O sistema utiliza repositórios em memória para persistência simples.