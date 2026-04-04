package br.unb.eda2.loja.estrutura;

import br.unb.eda2.loja.dominio.Cliente;
import br.unb.eda2.loja.util.Validadores;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Árvore binária de busca de {@link Cliente} ordenada por nome (lexicográfico,
 * sem diferenciar maiúsculas/minúsculas). Em caso de nomes iguais, desempate por {@link Cliente#getId()}.
 */

public class ArvoreClientesPorNome {

    private static final class No {
        private Cliente cliente;
        private No esquerda;
        private No direita;

        private No(Cliente cliente) {
            this.cliente = cliente;
        }
    }

    private No raiz;

    /**
     * Insere ou atualiza o cliente na árvore (mesma chave nome+id substitui o valor).
     */
    
    public void inserir(Cliente cliente) {
        Validadores.requerNaoNulo(cliente, "Cliente");
        raiz = inserir(raiz, cliente);
    }

    private No inserir(No no, Cliente cliente) {
        if (no == null) {
            return new No(cliente);
        }
        int cmp = comparar(cliente, no.cliente);
        if (cmp == 0) {
            no.cliente = cliente;
            return no;
        }
        if (cmp < 0) {
            no.esquerda = inserir(no.esquerda, cliente);
        } else {
            no.direita = inserir(no.direita, cliente);
        }
        return no;
    }

    /**
     * Busca na árvore pela chave ordenada (nome exibido + id).
     */

    public Optional<Cliente> buscar(String nome, long id) {
        Validadores.requerNaoVazio(nome, "Nome");
        No no = buscarNo(raiz, nome.trim(), id);
        return no == null ? Optional.empty() : Optional.of(no.cliente);
    }

    private No buscarNo(No no, String nome, long id) {
        if (no == null) {
            return null;
        }
        int cmp = compararChave(nome, id, no.cliente);
        if (cmp == 0) {
            return no;
        }
        if (cmp < 0) {
            return buscarNo(no.esquerda, nome, id);
        }
        return buscarNo(no.direita, nome, id);
    }

    /**
     * Remove o nó com a chave dada, se existir.
     *
     * @return {@code true} se removeu algum nó
     */

    public boolean remover(String nome, long id) {
        Validadores.requerNaoVazio(nome, "Nome");
        AtomicBoolean removido = new AtomicBoolean(false);
        raiz = remover(raiz, nome.trim(), id, removido);
        return removido.get();
    }

    private No remover(No no, String nome, long id, AtomicBoolean removido) {
        if (no == null) {
            return null;
        }
        int cmp = compararChave(nome, id, no.cliente);
        if (cmp < 0) {
            no.esquerda = remover(no.esquerda, nome, id, removido);
            return no;
        }
        if (cmp > 0) {
            no.direita = remover(no.direita, nome, id, removido);
            return no;
        }
        removido.set(true);
        if (no.esquerda == null) {
            return no.direita;
        }
        if (no.direita == null) {
            return no.esquerda;
        }
        No sucessor = minimo(no.direita);
        no.cliente = sucessor.cliente;
        no.direita = remover(no.direita, sucessor.cliente.getNome(), sucessor.cliente.getId(), new AtomicBoolean());
        return no;
    }

    private static No minimo(No no) {
        No atual = no;
        while (atual.esquerda != null) {
            atual = atual.esquerda;
        }
        return atual;
    }

    /**
     * Percurso em ordem (esquerda, raiz, direita): lista clientes em ordem crescente de chave (nome, id).
     */
    
    public List<Cliente> percorrerInOrder() {
        List<Cliente> saida = new ArrayList<>();
        inOrder(raiz, saida);
        return saida;
    }

    private void inOrder(No no, List<Cliente> saida) {
        if (no == null) {
            return;
        }
        inOrder(no.esquerda, saida);
        saida.add(no.cliente);
        inOrder(no.direita, saida);
    }

    private static int comparar(Cliente a, Cliente b) {
        int cmp = a.getNome().compareToIgnoreCase(b.getNome());
        if (cmp != 0) {
            return cmp;
        }
        return Long.compare(a.getId(), b.getId());
    }

    private static int compararChave(String nome, long id, Cliente c) {
        int cmp = nome.compareToIgnoreCase(c.getNome());
        if (cmp != 0) {
            return cmp;
        }
        return Long.compare(id, c.getId());
    }
}
