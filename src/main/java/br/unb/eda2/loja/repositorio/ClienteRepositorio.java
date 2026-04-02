package br.unb.eda2.loja.repositorio;

import br.unb.eda2.loja.dominio.Cliente;

import java.util.List;
import java.util.Optional;

/**
 * Persistência de {@link Cliente}. Garante unicidade de id e de CPF.
 */

public interface ClienteRepositorio {
    void salvar(Cliente cliente);
    Optional<Cliente> buscarPorId(long id);
    Optional<Cliente> buscarPorCpf(String cpf);
    List<Cliente> listarTodos();
    boolean remover(long id);
}
