package br.unb.eda2.loja.estrutura;

import br.unb.eda2.loja.dominio.Cartao;
import br.unb.eda2.loja.util.Validadores;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Tabela hash com encadeamento separado para {@link Cartao}, chave {@link Cartao#getId()}.
 */

public class TabelaHashCartoes {

    private static final int CAPACIDADE_PADRAO = 101;

    private final int capacidade;
    private final List<Cartao>[] buckets;

    @SuppressWarnings("unchecked")
    public TabelaHashCartoes(int capacidade) {
        if (capacidade <= 0) {
            throw new IllegalArgumentException("Capacidade deve ser positiva.");
        }
        this.capacidade = capacidade;
        this.buckets = new List[capacidade];
        for (int i = 0; i < capacidade; i++) {
            buckets[i] = new ArrayList<>();
        }
    }

    public TabelaHashCartoes() {
        this(CAPACIDADE_PADRAO);
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void inserir(Cartao cartao) {
        Validadores.requerNaoNulo(cartao, "Cartão");
        buscar(cartao.getId()).ifPresent(c -> removerDaLista(indice(c.getId()), c.getId()));
        buckets[indice(cartao.getId())].add(cartao);
    }

    public Optional<Cartao> buscar(long idCartao) {
        int idx = indice(idCartao);
        for (Cartao c : buckets[idx]) {
            if (c.getId() == idCartao) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public boolean remover(long idCartao) {
        return removerDaLista(indice(idCartao), idCartao);
    }

    public List<Cartao> valores() {
        List<Cartao> saida = new ArrayList<>();
        for (List<Cartao> bucket : buckets) {
            saida.addAll(bucket);
        }
        return saida;
    }

    private boolean removerDaLista(int idx, long idCartao) {
        Iterator<Cartao> it = buckets[idx].iterator();
        while (it.hasNext()) {
            if (it.next().getId() == idCartao) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    private int indice(long id) {
        return Math.floorMod(Long.hashCode(id), capacidade);
    }
}
