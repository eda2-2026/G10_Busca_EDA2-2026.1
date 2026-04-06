package br.unb.eda2.loja.servico;

import br.unb.eda2.loja.dominio.Cartao;
import br.unb.eda2.loja.dominio.Cliente;
import br.unb.eda2.loja.dominio.StatusCartao;
import br.unb.eda2.loja.dominio.StatusSolicitacao;
import br.unb.eda2.loja.repositorio.memoria.CartaoRepositorioMemoria;
import br.unb.eda2.loja.repositorio.memoria.ClienteRepositorioMemoria;
import br.unb.eda2.loja.repositorio.memoria.HistoricoSolicitacaoRepositorioMemoria;
import br.unb.eda2.loja.repositorio.memoria.SolicitacaoRepositorioMemoria;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RelatorioServiceTest {

    @Test
    void relatorios_deveContarPorStatus_ePorMotivo_eListarPorPeriodo() {
        ClienteRepositorioMemoria clientes = new ClienteRepositorioMemoria();
        CartaoRepositorioMemoria cartoes = new CartaoRepositorioMemoria(clientes);
        SolicitacaoRepositorioMemoria solicitacoes = new SolicitacaoRepositorioMemoria(cartoes);
        CancelamentoService cancelamento = new CancelamentoService(
                clientes, cartoes, solicitacoes, new HistoricoSolicitacaoRepositorioMemoria());
        RelatorioService relatorios = new RelatorioService(solicitacoes);

        clientes.salvar(new Cliente(1, "Cliente Um", "12345678901"));
        cartoes.salvar(new Cartao(10, 1, "**** 0000", "Visa", new BigDecimal("100.00"), StatusCartao.ATIVO));
        cartoes.salvar(new Cartao(11, 1, "**** 1111", "Visa", new BigDecimal("100.00"), StatusCartao.ATIVO));

        LocalDateTime base = LocalDateTime.now().minusDays(10);
        cancelamento.abrirSolicitacao(1, 10, "Perda do cartão", base.plusDays(1)); // ABERTA
        cancelamento.abrirSolicitacao(2, 11, "  PERDA do CARTÃO  ", base.plusDays(2)); // ABERTA (mesmo motivo normalizado)
        cancelamento.enviarParaAnalise(2); // EM_ANALISE
        cancelamento.recusar(2, "x"); // RECUSADA

        Map<StatusSolicitacao, Long> porStatus = relatorios.contarPorStatus();
        assertEquals(1L, porStatus.get(StatusSolicitacao.ABERTA));
        assertEquals(1L, porStatus.get(StatusSolicitacao.RECUSADA));

        Map<String, Long> porMotivo = relatorios.contarPorMotivo();
        assertEquals(2L, porMotivo.get("perda do cartão"));

        List<?> noPeriodo = relatorios.listarPorPeriodo(base.plusDays(1), base.plusDays(1));
        assertEquals(1, noPeriodo.size());
    }
}

