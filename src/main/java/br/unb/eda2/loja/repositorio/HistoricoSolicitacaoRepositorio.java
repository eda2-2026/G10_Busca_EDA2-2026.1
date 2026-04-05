package br.unb.eda2.loja.repositorio;

import br.unb.eda2.loja.dominio.HistoricoAlteracaoSolicitacao;
import br.unb.eda2.loja.dominio.StatusSolicitacao;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Histórico de transições de status das solicitações de cancelamento.
 */
public interface HistoricoSolicitacaoRepositorio {

    void salvar(HistoricoAlteracaoSolicitacao registro);

    /**
     * Registra uma transição; gera identificador do registro de histórico.
     */
    void registrarTransicao(long idSolicitacao, StatusSolicitacao statusAnterior,
                            StatusSolicitacao statusNovo, LocalDateTime dataHora, String detalhe);

    List<HistoricoAlteracaoSolicitacao> listarPorIdSolicitacao(long idSolicitacao);
}
