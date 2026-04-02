package br.unb.eda2.loja.repositorio.memoria;

import br.unb.eda2.loja.dominio.Cliente;
import br.unb.eda2.loja.repositorio.ClienteRepositorio;
import br.unb.eda2.loja.util.Validadores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementação em memória para testes iniciais.
 */

public class ClienteRepositorioMemoria implements ClienteRepositorio {

    private final Map<Long, Cliente> porId = new HashMap<>();
    private final Map<String, Long> idPorCpf = new HashMap<>();

    @Override
    public void salvar(Cliente cliente) {
        Validadores.requerNaoNulo(cliente, "Cliente");
        long id = cliente.getId();
        String cpf = cliente.getCpf();

        Optional<Cliente> outroComMesmoCpf = buscarPorCpf(cpf);
        if (outroComMesmoCpf.isPresent() && outroComMesmoCpf.get().getId() != id) {
            throw new IllegalArgumentException("CPF já cadastrado para outro cliente.");
        }

        Cliente anterior = porId.get(id);
        if (anterior != null && !anterior.getCpf().equals(cpf)) {
            idPorCpf.remove(anterior.getCpf());
        }

        porId.put(id, cliente);
        idPorCpf.put(cpf, id);
    }

    @Override
    public Optional<Cliente> buscarPorId(long id) {
        return Optional.ofNullable(porId.get(id));
    }

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        String normalizado = Validadores.normalizarCpf(cpf);
        Long id = idPorCpf.get(normalizado);
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(porId.get(id));
    }

    @Override
    public List<Cliente> listarTodos() {
        return new ArrayList<>(porId.values());
    }

    @Override
    public boolean remover(long id) {
        Cliente removido = porId.remove(id);
        if (removido == null) {
            return false;
        }
        idPorCpf.remove(removido.getCpf());
        return true;
    }
}