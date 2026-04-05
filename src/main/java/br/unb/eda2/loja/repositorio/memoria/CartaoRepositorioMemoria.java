package br.unb.eda2.loja.repositorio.memoria;

import br.unb.eda2.loja.dominio.Cartao;
import br.unb.eda2.loja.estrutura.TabelaHashCartoes;
import br.unb.eda2.loja.repositorio.CartaoRepositorio;
import br.unb.eda2.loja.repositorio.ClienteRepositorio;
import br.unb.eda2.loja.util.Validadores;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação em memória para testes iniciais.
 */

public class CartaoRepositorioMemoria implements CartaoRepositorio {

    private final ClienteRepositorio clientes;
    private final TabelaHashCartoes tabela = new TabelaHashCartoes();

    public CartaoRepositorioMemoria(ClienteRepositorio clientes) {
        this.clientes = Validadores.requerNaoNulo(clientes, "ClienteRepositorio");
    }

    @Override
    public void salvar(Cartao cartao) {
        Validadores.requerNaoNulo(cartao, "Cartão");
        if (clientes.buscarPorId(cartao.getIdCliente()).isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado para idCliente=" + cartao.getIdCliente());
        }
        tabela.inserir(cartao);
    }

    @Override
    public Optional<Cartao> buscarPorId(long id) {
        return tabela.buscar(id);
    }

    @Override
    public List<Cartao> listarPorIdCliente(long idCliente) {
        return tabela.valores().stream()
                .filter(c -> c.getIdCliente() == idCliente)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Cartao> listarTodos() {
        return new ArrayList<>(tabela.valores());
    }

    @Override
    public boolean remover(long id) {
        return tabela.remover(id);
    }
}