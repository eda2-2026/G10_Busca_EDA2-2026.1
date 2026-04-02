package br.unb.eda2.loja.repositorio;

import br.unb.eda2.loja.dominio.Cartao;

import java.util.List;
import java.util.Optional;

/**
 * Persistência de {@link Cartao}.
 */

public interface CartaoRepositorio {
    void salvar(Cartao cartao);
    Optional<Cartao> buscarPorId(long id);
    List<Cartao> listarPorIdCliente(long idCliente);
    List<Cartao> listarTodos();
    boolean remover(long id);
}
