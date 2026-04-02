package br.unb.eda2.loja.repositorio;

import br.unb.eda2.loja.dominio.SolicitacaoCancelamento;

import java.util.List;
import java.util.Optional;

/**
 * Persistência de {@link SolicitacaoCancelamento}.
 */

public interface SolicitacaoRepositorio {
    void salvar(SolicitacaoCancelamento solicitacao);
    Optional<SolicitacaoCancelamento> buscarPorId(long id);
    List<SolicitacaoCancelamento> listarTodos();
    boolean remover(long id);
}