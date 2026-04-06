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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CancelamentoServiceTest {

    @Test
    void abrirSolicitacao_deveFalhar_quandoClienteNaoEncontrado() {
        ClienteRepositorioMemoria clientes = new ClienteRepositorioMemoria();
        CartaoRepositorioMemoria cartoes = new CartaoRepositorioMemoria(clientes);
        SolicitacaoRepositorioMemoria solicitacoes = new SolicitacaoRepositorioMemoria(cartoes);
        HistoricoSolicitacaoRepositorioMemoria historico = new HistoricoSolicitacaoRepositorioMemoria();
        CancelamentoService service = new CancelamentoService(clientes, cartoes, solicitacoes, historico);

        Cliente c = new Cliente(1, "Cliente Um", "12345678901");
        clientes.salvar(c);
        cartoes.salvar(new Cartao(10, 1, "**** 0000", "Visa", new BigDecimal("100.00"), StatusCartao.ATIVO));
        clientes.remover(1); // deixa o cartão "órfão" para simular cliente inexistente

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.abrirSolicitacao(1, 10, "Perda", LocalDateTime.now()));
        assertEquals("Cliente do cartão não encontrado.", ex.getMessage());
    }

    @Test
    void abrirSolicitacao_deveFalhar_quandoCartaoJaCancelado() {
        ClienteRepositorioMemoria clientes = new ClienteRepositorioMemoria();
        CartaoRepositorioMemoria cartoes = new CartaoRepositorioMemoria(clientes);
        SolicitacaoRepositorioMemoria solicitacoes = new SolicitacaoRepositorioMemoria(cartoes);
        HistoricoSolicitacaoRepositorioMemoria historico = new HistoricoSolicitacaoRepositorioMemoria();
        CancelamentoService service = new CancelamentoService(clientes, cartoes, solicitacoes, historico);

        Cliente c = new Cliente(1, "Cliente Um", "12345678901");
        clientes.salvar(c);
        cartoes.salvar(new Cartao(10, 1, "**** 0000", "Visa", new BigDecimal("100.00"), StatusCartao.CANCELADO));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.abrirSolicitacao(1, 10, "Perda", LocalDateTime.now()));
        assertEquals("O cartão já está cancelado.", ex.getMessage());
    }

    @Test
    void fluxo_aprovar_deveCancelarCartao_eConcluirSolicitacao() {
        ClienteRepositorioMemoria clientes = new ClienteRepositorioMemoria();
        CartaoRepositorioMemoria cartoes = new CartaoRepositorioMemoria(clientes);
        SolicitacaoRepositorioMemoria solicitacoes = new SolicitacaoRepositorioMemoria(cartoes);
        HistoricoSolicitacaoRepositorioMemoria historico = new HistoricoSolicitacaoRepositorioMemoria();
        CancelamentoService service = new CancelamentoService(clientes, cartoes, solicitacoes, historico);

        clientes.salvar(new Cliente(1, "Cliente Um", "12345678901"));
        cartoes.salvar(new Cartao(10, 1, "**** 0000", "Visa", new BigDecimal("100.00"), StatusCartao.ATIVO));

        service.abrirSolicitacao(1, 10, "Perda", LocalDateTime.now().minusDays(1));
        service.enviarParaAnalise(1);
        service.aprovar(1);

        assertEquals(StatusSolicitacao.CONCLUIDA, service.buscarPorId(1).getStatus());
        assertEquals(StatusCartao.CANCELADO, cartoes.buscarPorId(10).orElseThrow().getStatus());
    }

    @Test
    void fluxo_recusar_deveManterCartaoAtivo_eSolicitacaoRecusada() {
        ClienteRepositorioMemoria clientes = new ClienteRepositorioMemoria();
        CartaoRepositorioMemoria cartoes = new CartaoRepositorioMemoria(clientes);
        SolicitacaoRepositorioMemoria solicitacoes = new SolicitacaoRepositorioMemoria(cartoes);
        HistoricoSolicitacaoRepositorioMemoria historico = new HistoricoSolicitacaoRepositorioMemoria();
        CancelamentoService service = new CancelamentoService(clientes, cartoes, solicitacoes, historico);

        clientes.salvar(new Cliente(1, "Cliente Um", "12345678901"));
        cartoes.salvar(new Cartao(10, 1, "**** 0000", "Visa", new BigDecimal("100.00"), StatusCartao.ATIVO));

        service.abrirSolicitacao(1, 10, "Perda", LocalDateTime.now().minusHours(2));
        service.enviarParaAnalise(1);
        service.recusar(1, "Sem documento");

        assertEquals(StatusSolicitacao.RECUSADA, service.buscarPorId(1).getStatus());
        assertEquals(StatusCartao.ATIVO, cartoes.buscarPorId(10).orElseThrow().getStatus());
    }
}

