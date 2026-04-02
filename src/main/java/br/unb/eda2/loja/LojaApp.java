package br.unb.eda2.loja;

import br.unb.eda2.loja.dominio.Cartao;
import br.unb.eda2.loja.dominio.Cliente;
import br.unb.eda2.loja.dominio.SolicitacaoCancelamento;
import br.unb.eda2.loja.dominio.StatusCartao;
import br.unb.eda2.loja.dominio.StatusSolicitacao;
import br.unb.eda2.loja.repositorio.CartaoRepositorio;
import br.unb.eda2.loja.repositorio.ClienteRepositorio;
import br.unb.eda2.loja.repositorio.SolicitacaoRepositorio;
import br.unb.eda2.loja.repositorio.memoria.CartaoRepositorioMemoria;
import br.unb.eda2.loja.repositorio.memoria.ClienteRepositorioMemoria;
import br.unb.eda2.loja.repositorio.memoria.SolicitacaoRepositorioMemoria;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Menu inicial: cadastro e busca simples de clientes, cartões e solicitações (Etapa 1).
 */

public final class LojaApp {

    private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private LojaApp() {
    }

    public static void main(String[] args) {
        ClienteRepositorio clientes = new ClienteRepositorioMemoria();
        CartaoRepositorio cartoes = new CartaoRepositorioMemoria(clientes);
        SolicitacaoRepositorio solicitacoes = new SolicitacaoRepositorioMemoria(cartoes);

        try (Scanner in = new Scanner(System.in)) {
            boolean sair = false;
            while (!sair) {
                System.out.println();
                System.out.println("=== Loja — Menu (Etapa 1) ===");
                System.out.println("1 — Cadastrar cliente");
                System.out.println("2 — Buscar cliente por ID");
                System.out.println("3 — Buscar cliente por CPF");
                System.out.println("4 — Listar clientes");
                System.out.println("5 — Cadastrar cartão");
                System.out.println("6 — Buscar cartão por ID");
                System.out.println("7 — Listar cartões");
                System.out.println("8 — Cadastrar solicitação de cancelamento");
                System.out.println("9 — Listar solicitações");
                System.out.println("0 — Sair");
                System.out.print("Opção: ");

                String linha = in.nextLine().trim();
                try {
                    switch (linha) {
                        case "1" -> cadastrarCliente(in, clientes);
                        case "2" -> buscarClientePorId(in, clientes);
                        case "3" -> buscarClientePorCpf(in, clientes);
                        case "4" -> listarClientes(clientes);
                        case "5" -> cadastrarCartao(in, cartoes);
                        case "6" -> buscarCartaoPorId(in, cartoes);
                        case "7" -> listarCartoes(cartoes);
                        case "8" -> cadastrarSolicitacao(in, solicitacoes);
                        case "9" -> listarSolicitacoes(solicitacoes);
                        case "0" -> sair = true;
                        default -> System.out.println("Opção inválida.");
                    }
                } catch (IllegalArgumentException ex) {
                    System.out.println("Erro: " + ex.getMessage());
                }
            }
            System.out.println("Até logo.");
        }
    }

    private static void cadastrarCliente(Scanner in, ClienteRepositorio repo) {
        long id = lerLong(in, "ID do cliente: ");
        System.out.print("Nome: ");
        String nome = in.nextLine();
        System.out.print("CPF (11 dígitos): ");
        String cpf = in.nextLine();
        Cliente c = new Cliente(id, nome, cpf);
        System.out.print("E-mail (vazio para omitir): ");
        String email = in.nextLine();
        if (!email.isBlank()) {
            c.setEmail(email);
        }
        System.out.print("Telefone (vazio para omitir): ");
        String tel = in.nextLine();
        if (!tel.isBlank()) {
            c.setTelefone(tel);
        }
        repo.salvar(c);
        System.out.println("Cliente salvo: " + c);
    }

    private static void buscarClientePorId(Scanner in, ClienteRepositorio repo) {
        long id = lerLong(in, "ID: ");
        repo.buscarPorId(id).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Cliente não encontrado.")
        );
    }

    private static void buscarClientePorCpf(Scanner in, ClienteRepositorio repo) {
        System.out.print("CPF: ");
        String cpf = in.nextLine();
        repo.buscarPorCpf(cpf).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Cliente não encontrado.")
        );
    }

    private static void listarClientes(ClienteRepositorio repo) {
        var lista = repo.listarTodos();
        lista.sort(Comparator.comparingLong(Cliente::getId));
        if (lista.isEmpty()) {
            System.out.println("(nenhum cliente)");
            return;
        }
        lista.forEach(System.out::println);
    }

    private static void cadastrarCartao(Scanner in, CartaoRepositorio repo) {
        long id = lerLong(in, "ID do cartão: ");
        long idCliente = lerLong(in, "ID do cliente: ");
        System.out.print("Número mascarado: ");
        String num = in.nextLine();
        System.out.print("Bandeira: ");
        String bandeira = in.nextLine();
        BigDecimal limite = lerBigDecimal(in, "Limite: ");
        StatusCartao status = lerStatusCartao(in);
        Cartao cartao = new Cartao(id, idCliente, num, bandeira, limite, status);
        repo.salvar(cartao);
        System.out.println("Cartão salvo: " + cartao);
    }

    private static void buscarCartaoPorId(Scanner in, CartaoRepositorio repo) {
        long id = lerLong(in, "ID: ");
        repo.buscarPorId(id).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Cartão não encontrado.")
        );
    }

    private static void listarCartoes(CartaoRepositorio repo) {
        var lista = repo.listarTodos();
        lista.sort(Comparator.comparingLong(Cartao::getId));
        if (lista.isEmpty()) {
            System.out.println("(nenhum cartão)");
            return;
        }
        lista.forEach(System.out::println);
    }

    private static void cadastrarSolicitacao(Scanner in, SolicitacaoRepositorio repo) {
        long id = lerLong(in, "ID da solicitação: ");
        long idCartao = lerLong(in, "ID do cartão: ");
        System.out.print("Motivo: ");
        String motivo = in.nextLine();
        LocalDateTime data = lerDataHoraOuAgora(in);
        StatusSolicitacao status = lerStatusSolicitacao(in);
        SolicitacaoCancelamento s = new SolicitacaoCancelamento(id, idCartao, motivo, data, status);
        repo.salvar(s);
        System.out.println("Solicitação salva: " + s);
    }

    private static void listarSolicitacoes(SolicitacaoRepositorio repo) {
        var lista = repo.listarTodos();
        lista.sort(Comparator.comparingLong(SolicitacaoCancelamento::getId));
        if (lista.isEmpty()) {
            System.out.println("(nenhuma solicitação)");
            return;
        }
        lista.forEach(System.out::println);
    }

    private static long lerLong(Scanner in, String prompt) {
        System.out.print(prompt);
        String t = in.nextLine().trim();
        try {
            return Long.parseLong(t);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Número inteiro inválido.");
        }
    }

    private static BigDecimal lerBigDecimal(Scanner in, String prompt) {
        System.out.print(prompt);
        String t = in.nextLine().trim().replace(',', '.');
        try {
            return new BigDecimal(t);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor decimal inválido.");
        }
    }

    private static StatusCartao lerStatusCartao(Scanner in) {
        System.out.print("Status do cartão (ATIVO, BLOQUEADO, CANCELADO): ");
        String t = in.nextLine().trim().toUpperCase();
        try {
            return StatusCartao.valueOf(t);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status de cartão inválido.");
        }
    }

    private static StatusSolicitacao lerStatusSolicitacao(Scanner in) {
        System.out.print("Status (ABERTA, EM_ANALISE, APROVADA, RECUSADA, CONCLUIDA): ");
        String t = in.nextLine().trim().toUpperCase();
        try {
            return StatusSolicitacao.valueOf(t);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status de solicitação inválido.");
        }
    }

    private static LocalDateTime lerDataHoraOuAgora(Scanner in) {
        System.out.print("Data/hora (dd/MM/yyyy HH:mm) ou Enter para agora: ");
        String t = in.nextLine().trim();
        if (t.isEmpty()) {
            return LocalDateTime.now();
        }
        try {
            return LocalDateTime.parse(t, DATA_HORA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Use o formato dd/MM/yyyy HH:mm ou deixe vazio.");
        }
    }
}
