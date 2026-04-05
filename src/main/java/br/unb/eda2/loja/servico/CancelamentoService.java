package br.unb.eda2.loja.servico;

import br.unb.eda2.loja.dominio.Cartao;
import br.unb.eda2.loja.dominio.Cliente;
import br.unb.eda2.loja.dominio.HistoricoAlteracaoSolicitacao;
import br.unb.eda2.loja.dominio.SolicitacaoCancelamento;
import br.unb.eda2.loja.dominio.StatusCartao;
import br.unb.eda2.loja.dominio.StatusSolicitacao;
import br.unb.eda2.loja.repositorio.CartaoRepositorio;
import br.unb.eda2.loja.repositorio.ClienteRepositorio;
import br.unb.eda2.loja.repositorio.HistoricoSolicitacaoRepositorio;
import br.unb.eda2.loja.repositorio.SolicitacaoRepositorio;
import br.unb.eda2.loja.util.Validadores;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Regras de negócio do fluxo de cancelamento de cartões.
 */
public class CancelamentoService {

    private final ClienteRepositorio clientes;
    private final CartaoRepositorio cartoes;
    private final SolicitacaoRepositorio solicitacoes;
    private final HistoricoSolicitacaoRepositorio historico;

    public CancelamentoService(ClienteRepositorio clientes, CartaoRepositorio cartoes,
                               SolicitacaoRepositorio solicitacoes, HistoricoSolicitacaoRepositorio historico) {
        this.clientes = Validadores.requerNaoNulo(clientes, "ClienteRepositorio");
        this.cartoes = Validadores.requerNaoNulo(cartoes, "CartaoRepositorio");
        this.solicitacoes = Validadores.requerNaoNulo(solicitacoes, "SolicitacaoRepositorio");
        this.historico = Validadores.requerNaoNulo(historico, "HistoricoSolicitacaoRepositorio");
    }

    /**
     * Abre uma nova solicitação em status {@link StatusSolicitacao#ABERTA}.
     */
    public SolicitacaoCancelamento abrirSolicitacao(long idSolicitacao, long idCartao, String motivo,
                                                    LocalDateTime dataSolicitacao) {
        Validadores.requerNaoNulo(dataSolicitacao, "Data da solicitação");
        Cartao cartao = cartoes.buscarPorId(idCartao)
                .orElseThrow(() -> new IllegalArgumentException("Cartão não encontrado para id=" + idCartao));
        if (cartao.getStatus() == StatusCartao.CANCELADO) {
            throw new IllegalArgumentException("O cartão já está cancelado.");
        }
        Cliente cliente = clientes.buscarPorId(cartao.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente do cartão não encontrado."));
        if (!cliente.isAtivo()) {
            throw new IllegalArgumentException("Cliente inativo: não é possível abrir solicitação de cancelamento.");
        }
        if (!solicitacoes.listarPendentesPorIdCartao(idCartao).isEmpty()) {
            throw new IllegalArgumentException("Já existe solicitação em aberto ou em análise para este cartão.");
        }

        SolicitacaoCancelamento s = new SolicitacaoCancelamento(
                idSolicitacao, idCartao, motivo, dataSolicitacao, StatusSolicitacao.ABERTA);
        solicitacoes.salvar(s);
        historico.registrarTransicao(idSolicitacao, null, StatusSolicitacao.ABERTA, dataSolicitacao,
                "Solicitação aberta");
        return s;
    }

    /** ABERTA → EM_ANALISE */
    public void enviarParaAnalise(long idSolicitacao) {
        SolicitacaoCancelamento s = buscarObrigatorio(idSolicitacao);
        if (s.getStatus() != StatusSolicitacao.ABERTA) {
            throw new IllegalArgumentException("Só é possível enviar para análise solicitações em status ABERTA.");
        }
        LocalDateTime agora = LocalDateTime.now();
        s.setStatus(StatusSolicitacao.EM_ANALISE);
        solicitacoes.salvar(s);
        historico.registrarTransicao(idSolicitacao, StatusSolicitacao.ABERTA, StatusSolicitacao.EM_ANALISE, agora,
                "Em análise");
    }

    /** EM_ANALISE → aprovação: cartão cancelado e solicitação concluída. */
    public void aprovar(long idSolicitacao) {
        SolicitacaoCancelamento s = buscarObrigatorio(idSolicitacao);
        if (s.getStatus() != StatusSolicitacao.EM_ANALISE) {
            throw new IllegalArgumentException("Só é possível aprovar solicitações em status EM_ANALISE.");
        }
        Cartao cartao = cartoes.buscarPorId(s.getIdCartao())
                .orElseThrow(() -> new IllegalStateException("Cartão da solicitação não encontrado."));
        LocalDateTime agora = LocalDateTime.now();

        s.setStatus(StatusSolicitacao.APROVADA);
        solicitacoes.salvar(s);
        historico.registrarTransicao(idSolicitacao, StatusSolicitacao.EM_ANALISE, StatusSolicitacao.APROVADA, agora,
                "Solicitação aprovada");

        cartao.setStatus(StatusCartao.CANCELADO);
        cartoes.salvar(cartao);

        s.setStatus(StatusSolicitacao.CONCLUIDA);
        solicitacoes.salvar(s);
        historico.registrarTransicao(idSolicitacao, StatusSolicitacao.APROVADA, StatusSolicitacao.CONCLUIDA, agora,
                "Cancelamento do cartão concluído");
    }

    /** EM_ANALISE → RECUSADA */
    public void recusar(long idSolicitacao, String motivoRecusa) {
        SolicitacaoCancelamento s = buscarObrigatorio(idSolicitacao);
        if (s.getStatus() != StatusSolicitacao.EM_ANALISE) {
            throw new IllegalArgumentException("Só é possível recusar solicitações em status EM_ANALISE.");
        }
        LocalDateTime agora = LocalDateTime.now();
        s.setStatus(StatusSolicitacao.RECUSADA);
        solicitacoes.salvar(s);
        String detalhe = motivoRecusa == null || motivoRecusa.isBlank()
                ? "Solicitação recusada"
                : "Recusada: " + motivoRecusa.trim();
        historico.registrarTransicao(idSolicitacao, StatusSolicitacao.EM_ANALISE, StatusSolicitacao.RECUSADA, agora,
                detalhe);
    }

    public List<SolicitacaoCancelamento> listarPorStatus(StatusSolicitacao status) {
        Validadores.requerNaoNulo(status, "Status");
        return solicitacoes.listarPorStatus(status);
    }

    public List<HistoricoAlteracaoSolicitacao> listarHistorico(long idSolicitacao) {
        Validadores.requerIdPositivo(idSolicitacao, "idSolicitacao");
        return historico.listarPorIdSolicitacao(idSolicitacao);
    }

    public SolicitacaoCancelamento buscarPorId(long id) {
        return solicitacoes.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada para id=" + id));
    }

    private SolicitacaoCancelamento buscarObrigatorio(long idSolicitacao) {
        return solicitacoes.buscarPorId(idSolicitacao)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada para id=" + idSolicitacao));
    }
}
