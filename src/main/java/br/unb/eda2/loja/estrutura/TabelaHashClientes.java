package br.unb.eda2.loja.estrutura;

import br.unb.eda2.loja.dominio.Cliente;
import br.unb.eda2.loja.util.Validadores;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Tabela hash com encadeamento separado para {@link Cliente}. Há dois índices
 * (mesmo tamanho): um pela chave numérica {@link Cliente#getId()} e outro pelo
 * CPF normalizado, permitindo busca O(1) média em ambos os casos.
 */

public class TabelaHashClientes {

    private static final int CAPACIDADE_PADRAO = 101;

    private final int capacidade;
    private final List<Cliente>[] bucketsPorId;
    private final List<Cliente>[] bucketsPorCpf;

    @SuppressWarnings("unchecked")
    public TabelaHashClientes(int capacidade) {
        if (capacidade <= 0) {
            throw new IllegalArgumentException("Capacidade deve ser positiva.");
        }
        this.capacidade = capacidade;
        this.bucketsPorId = new List[capacidade];
        this.bucketsPorCpf = new List[capacidade];
        for (int i = 0; i < capacidade; i++) {
            bucketsPorId[i] = new ArrayList<>();
            bucketsPorCpf[i] = new ArrayList<>();
        }
    }

    public TabelaHashClientes() {
        this(CAPACIDADE_PADRAO);
    }

    public int getCapacidade() {
        return capacidade;
    }

    /**
     * Insere ou substitui o cliente. Se já existir o mesmo {@code id}, o registro
     * anterior é removido dos dois índices antes da inserção do novo valor.
     */
    public void inserir(Cliente cliente) {
        Validadores.requerNaoNulo(cliente, "Cliente");
        buscarPorId(cliente.getId()).ifPresent(this::removerDasListas);
        adicionarNasListas(cliente);
    }

    public Optional<Cliente> buscarPorId(long id) {
        int idx = indiceId(id);
        for (Cliente c : bucketsPorId[idx]) {
            if (c.getId() == id) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public Optional<Cliente> buscarPorCpf(String cpf) {
        String normalizado = Validadores.normalizarCpf(cpf);
        int idx = indiceCpf(normalizado);
        for (Cliente c : bucketsPorCpf[idx]) {
            if (normalizado.equals(c.getCpf())) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    /**
     * Remove o cliente com o id informado dos dois índices.
     *
     * @return {@code true} se havia registro com esse id
     */
    public boolean remover(long id) {
        Optional<Cliente> opt = buscarPorId(id);
        if (opt.isEmpty()) {
            return false;
        }
        removerDasListas(opt.get());
        return true;
    }

    /**
     * Todos os clientes distintos (percorre apenas o índice por id).
     */
    public List<Cliente> valores() {
        List<Cliente> saida = new ArrayList<>();
        for (List<Cliente> bucket : bucketsPorId) {
            saida.addAll(bucket);
        }
        return saida;
    }

    private void adicionarNasListas(Cliente cliente) {
        bucketsPorId[indiceId(cliente.getId())].add(cliente);
        bucketsPorCpf[indiceCpf(cliente.getCpf())].add(cliente);
    }

    private void removerDasListas(Cliente cliente) {
        removerPorIdNaLista(bucketsPorId[indiceId(cliente.getId())], cliente.getId());
        removerPorCpfNaLista(bucketsPorCpf[indiceCpf(cliente.getCpf())], cliente.getCpf());
    }

    private static void removerPorIdNaLista(List<Cliente> lista, long id) {
        Iterator<Cliente> it = lista.iterator();
        while (it.hasNext()) {
            if (it.next().getId() == id) {
                it.remove();
                return;
            }
        }
    }

    private static void removerPorCpfNaLista(List<Cliente> lista, String cpf) {
        Iterator<Cliente> it = lista.iterator();
        while (it.hasNext()) {
            if (cpf.equals(it.next().getCpf())) {
                it.remove();
                return;
            }
        }
    }

    private int indiceId(long id) {
        return Math.floorMod(Long.hashCode(id), capacidade);
    }

    private int indiceCpf(String cpfNormalizado) {
        return Math.floorMod(cpfNormalizado.hashCode(), capacidade);
    }
}
