package br.unb.eda2.loja.repositorio.memoria;

import br.unb.eda2.loja.dominio.SolicitacaoCancelamento;
import br.unb.eda2.loja.repositorio.CartaoRepositorio;
import br.unb.eda2.loja.repositorio.SolicitacaoRepositorio;
import br.unb.eda2.loja.util.Validadores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementação em memória para testes iniciais.
 */

public class SolicitacaoRepositorioMemoria implements SolicitacaoRepositorio {

    private final CartaoRepositorio cartoes;
    private final Map<Long, SolicitacaoCancelamento> porId = new HashMap<>();

    public SolicitacaoRepositorioMemoria(CartaoRepositorio cartoes) {
        this.cartoes = Validadores.requerNaoNulo(cartoes, "CartaoRepositorio");
    }

    @Override
    public void salvar(SolicitacaoCancelamento solicitacao) {
        Validadores.requerNaoNulo(solicitacao, "Solicitação");
        if (cartoes.buscarPorId(solicitacao.getIdCartao()).isEmpty()) {
            throw new IllegalArgumentException("Cartão não encontrado para idCartao=" + solicitacao.getIdCartao());
        }
        porId.put(solicitacao.getId(), solicitacao);
    }

    @Override
    public Optional<SolicitacaoCancelamento> buscarPorId(long id) {
        return Optional.ofNullable(porId.get(id));
    }

    @Override
    public List<SolicitacaoCancelamento> listarTodos() {
        return new ArrayList<>(porId.values());
    }

    @Override
    public boolean remover(long id) {
        return porId.remove(id) != null;
    }
}
