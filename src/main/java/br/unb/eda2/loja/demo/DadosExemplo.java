package br.unb.eda2.loja.demo;

import br.unb.eda2.loja.dominio.Cartao;
import br.unb.eda2.loja.dominio.Cliente;
import br.unb.eda2.loja.dominio.StatusCartao;
import br.unb.eda2.loja.repositorio.CartaoRepositorio;
import br.unb.eda2.loja.repositorio.ClienteRepositorio;
import br.unb.eda2.loja.servico.CancelamentoService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Cenário fixo para testar telas e apresentação. Não substitui testes unitários.
 * <p>
 * Inclui vários clientes (um inativo), cartões em estados diferentes e solicitações
 * cobrindo ABERTA, EM_ANALISE, CONCLUIDA e RECUSADA.
 */
public final class DadosExemplo {

    private DadosExemplo() {
    }

    /** Passar {@code --sem-demo} nos argumentos do {@code main} para não carregar nada. */
    public static boolean deveCarregar(String[] args) {
        if (args == null) {
            return true;
        }
        for (String a : args) {
            if (a != null && "--sem-demo".equalsIgnoreCase(a.trim())) {
                return false;
            }
        }
        return true;
    }

    public static void popular(ClienteRepositorio clientes, CartaoRepositorio cartoes, CancelamentoService cancelamento) {
        LocalDateTime agora = LocalDateTime.now();

        Cliente c1 = new Cliente(1, "Ana Costa", "12345678901");
        c1.setEmail("ana@email.com");
        c1.setTelefone("61999991001");
        Cliente c2 = new Cliente(2, "Bruno Lima", "98765432100");
        c2.setEmail("bruno@email.com");
        Cliente c3 = new Cliente(3, "Carlos Souza", "11122233344");
        Cliente c4 = new Cliente(4, "Daniela Rocha", "22233344455");
        c4.setEmail("dani@email.com");
        Cliente c5 = new Cliente(5, "Eduardo Santos", "33344455566");
        c5.setTelefone("61988887777");
        Cliente c6 = new Cliente(6, "Fernanda Dias", "44455566677");
        Cliente c7 = new Cliente(7, "Gustavo Pires", "55566677788");
        c7.setAtivo(false);

        clientes.salvar(c1);
        clientes.salvar(c2);
        clientes.salvar(c3);
        clientes.salvar(c4);
        clientes.salvar(c5);
        clientes.salvar(c6);
        clientes.salvar(c7);

        cartoes.salvar(new Cartao(101, 1, "**** **** **** 1001", "Visa",
                new BigDecimal("1500.00"), StatusCartao.ATIVO));
        cartoes.salvar(new Cartao(102, 2, "**** **** **** 2002", "Master",
                new BigDecimal("3200.50"), StatusCartao.ATIVO));
        cartoes.salvar(new Cartao(103, 3, "**** **** **** 3003", "Elo",
                new BigDecimal("800.00"), StatusCartao.BLOQUEADO));
        cartoes.salvar(new Cartao(104, 1, "**** **** **** 4004", "Visa",
                new BigDecimal("500.00"), StatusCartao.ATIVO));
        cartoes.salvar(new Cartao(105, 4, "**** **** **** 5005", "Master",
                new BigDecimal("2100.00"), StatusCartao.ATIVO));
        cartoes.salvar(new Cartao(106, 5, "**** **** **** 6006", "Visa",
                new BigDecimal("980.00"), StatusCartao.ATIVO));
        cartoes.salvar(new Cartao(107, 6, "**** **** **** 7007", "Elo",
                new BigDecimal("450.00"), StatusCartao.ATIVO));
        cartoes.salvar(new Cartao(108, 4, "**** **** **** 8008", "Visa",
                new BigDecimal("12000.00"), StatusCartao.ATIVO));
        cartoes.salvar(new Cartao(109, 5, "**** **** **** 9009", "Master",
                new BigDecimal("300.00"), StatusCartao.BLOQUEADO));
        cartoes.salvar(new Cartao(110, 6, "**** **** **** 1010", "Visa",
                new BigDecimal("750.00"), StatusCartao.ATIVO));
        cartoes.salvar(new Cartao(111, 7, "**** **** **** 1111", "Elo",
                new BigDecimal("200.00"), StatusCartao.ATIVO));

        /* Bloco original + estados variados */
        cancelamento.abrirSolicitacao(1, 101, "Perda do cartão físico", agora.minusDays(5));
        cancelamento.abrirSolicitacao(2, 102, "Contestação de compra não reconhecida", agora.minusDays(2));
        cancelamento.enviarParaAnalise(2);

        cancelamento.abrirSolicitacao(3, 104, "Encerramento de conta na loja", agora.minusDays(1));
        cancelamento.enviarParaAnalise(3);
        cancelamento.aprovar(3);

        cancelamento.abrirSolicitacao(4, 103, "Pedido de baixa do cartão bloqueado", agora.minusHours(6));
        cancelamento.enviarParaAnalise(4);
        cancelamento.recusar(4, "Falta comprovante de identidade.");

        /* Mais volume: abertas, em análise, concluída e recusada */
        cancelamento.abrirSolicitacao(5, 105, "Roubo — registro de boletim", agora.minusHours(10));
        cancelamento.abrirSolicitacao(6, 106, "Desacordo no valor da fatura", agora.minusHours(8));
        cancelamento.enviarParaAnalise(6);

        cancelamento.abrirSolicitacao(7, 107, "Cancelamento por mudança de cidade", agora.minusHours(4));
        cancelamento.enviarParaAnalise(7);
        cancelamento.aprovar(7);

        cancelamento.abrirSolicitacao(8, 108, "Solicitação genérica de cancelamento", agora.minusHours(3));

        cancelamento.abrirSolicitacao(9, 109, "Desbloqueio e depois cancelar", agora.minusHours(2));
        cancelamento.enviarParaAnalise(9);
        cancelamento.recusar(9, "Cartão bloqueado por fraude; análise manual.");

        cancelamento.abrirSolicitacao(10, 110, "Troca de titularidade", agora.minusMinutes(90));
    }
}
