package br.unb.eda2.loja.repositorio;

import br.unb.eda2.loja.dominio.SolicitacaoCancelamento;
import br.unb.eda2.loja.dominio.StatusSolicitacao;

import java.util.List;
import java.util.Optional;

/**
 * Persistência de {@link SolicitacaoCancelamento}.
 */

public interface SolicitacaoRepositorio {
    void salvar(SolicitacaoCancelamento solicitacao);
    Optional<SolicitacaoCancelamento> buscarPorId(long id);
    List<SolicitacaoCancelamento> listarTodos();
    List<SolicitacaoCancelamento> listarPorStatus(StatusSolicitacao status);
    /** Solicitações ainda não finalizadas para o cartão (ABERTA ou EM_ANALISE). */
    List<SolicitacaoCancelamento> listarPendentesPorIdCartao(long idCartao);
    boolean remover(long id);
}