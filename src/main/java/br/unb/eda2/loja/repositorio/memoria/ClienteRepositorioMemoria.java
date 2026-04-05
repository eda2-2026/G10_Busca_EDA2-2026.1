package br.unb.eda2.loja.repositorio.memoria;

import br.unb.eda2.loja.dominio.Cliente;
import br.unb.eda2.loja.estrutura.ArvoreClientesPorNome;
import br.unb.eda2.loja.estrutura.TabelaHashClientes;
import br.unb.eda2.loja.repositorio.ClienteRepositorio;
import br.unb.eda2.loja.util.Validadores;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação em memória para testes iniciais.
 */

public class ClienteRepositorioMemoria implements ClienteRepositorio {

    private final TabelaHashClientes tabela = new TabelaHashClientes();
    private final ArvoreClientesPorNome arvorePorNome = new ArvoreClientesPorNome();

    @Override
    public void salvar(Cliente cliente) {
        Validadores.requerNaoNulo(cliente, "Cliente");
        long id = cliente.getId();
        String cpf = cliente.getCpf();

        Optional<Cliente> outroComMesmoCpf = buscarPorCpf(cpf);
        if (outroComMesmoCpf.isPresent() && outroComMesmoCpf.get().getId() != id) {
            throw new IllegalArgumentException("CPF já cadastrado para outro cliente.");
        }

        Cliente anterior = tabela.buscarPorId(id).orElse(null);

        if (anterior != null) {
            arvorePorNome.remover(anterior.getNome(), anterior.getId());
        }

        tabela.inserir(cliente);
        arvorePorNome.inserir(cliente);
    }

    @Override
    public Optional<Cliente> buscarPorId(long id) {
        return tabela.buscarPorId(id);
    }

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return tabela.buscarPorCpf(cpf);
    }

    @Override
    public List<Cliente> listarTodos() {
        return new ArrayList<>(tabela.valores());
    }
    @Override
    public List<Cliente> listarOrdenadosPorNome() {
        return arvorePorNome.percorrerInOrder();
    }

    @Override
    public Optional<Cliente> buscarPorNomeEId(String nome, long id) {
        return arvorePorNome.buscar(nome, id);
    }

    @Override
    public boolean remover(long id) {
        Optional<Cliente> opt = tabela.buscarPorId(id);
        if (opt.isEmpty()) {
            return false;
        }
        Cliente removido = opt.get();
        tabela.remover(id);
        arvorePorNome.remover(removido.getNome(), removido.getId());
        return true;
    }
}