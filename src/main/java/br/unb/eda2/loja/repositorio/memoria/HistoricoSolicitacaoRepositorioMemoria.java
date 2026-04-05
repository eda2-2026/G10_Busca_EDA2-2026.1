package br.unb.eda2.loja.repositorio.memoria;

import br.unb.eda2.loja.dominio.HistoricoAlteracaoSolicitacao;
import br.unb.eda2.loja.dominio.StatusSolicitacao;
import br.unb.eda2.loja.repositorio.HistoricoSolicitacaoRepositorio;
import br.unb.eda2.loja.util.Validadores;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Implementação em memória do histórico de solicitações.
 */
public class HistoricoSolicitacaoRepositorioMemoria implements HistoricoSolicitacaoRepositorio {

    private final List<HistoricoAlteracaoSolicitacao> registros = new CopyOnWriteArrayList<>();
    private final AtomicLong proximoId = new AtomicLong(1L);

    @Override
    public void salvar(HistoricoAlteracaoSolicitacao registro) {
        Validadores.requerNaoNulo(registro, "Registro de histórico");
        registros.add(registro);
    }

    @Override
    public void registrarTransicao(long idSolicitacao, StatusSolicitacao statusAnterior,
                                   StatusSolicitacao statusNovo, LocalDateTime dataHora, String detalhe) {
        Validadores.requerIdPositivo(idSolicitacao, "idSolicitacao");
        Validadores.requerNaoNulo(statusNovo, "Status novo");
        Validadores.requerNaoNulo(dataHora, "Data/hora");
        long id = proximoId.getAndIncrement();
        salvar(new HistoricoAlteracaoSolicitacao(id, idSolicitacao, dataHora, statusAnterior, statusNovo, detalhe));
    }

    @Override
    public List<HistoricoAlteracaoSolicitacao> listarPorIdSolicitacao(long idSolicitacao) {
        return registros.stream()
                .filter(h -> h.getIdSolicitacao() == idSolicitacao)
                .sorted(Comparator.comparing(HistoricoAlteracaoSolicitacao::getDataHora)
                        .thenComparingLong(HistoricoAlteracaoSolicitacao::getId))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
