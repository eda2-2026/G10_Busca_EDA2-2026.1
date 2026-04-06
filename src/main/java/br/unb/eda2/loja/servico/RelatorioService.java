package br.unb.eda2.loja.servico;

import br.unb.eda2.loja.dominio.SolicitacaoCancelamento;
import br.unb.eda2.loja.dominio.StatusSolicitacao;
import br.unb.eda2.loja.repositorio.SolicitacaoRepositorio;
import br.unb.eda2.loja.util.Validadores;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Geração de relatórios a partir das solicitações registradas.
 */

public class RelatorioService {

    private final SolicitacaoRepositorio solicitacoes;

    public RelatorioService(SolicitacaoRepositorio solicitacoes) {
        this.solicitacoes = Validadores.requerNaoNulo(solicitacoes, "SolicitacaoRepositorio");
    }

    public Map<StatusSolicitacao, Long> contarPorStatus() {
        EnumMap<StatusSolicitacao, Long> contagens = new EnumMap<>(StatusSolicitacao.class);
        for (StatusSolicitacao s : StatusSolicitacao.values()) {
            contagens.put(s, 0L);
        }
        for (SolicitacaoCancelamento sol : solicitacoes.listarTodos()) {
            contagens.merge(sol.getStatus(), 1L, Long::sum);
        }
        return contagens;
    }

    /**
     * Contagem por motivo (normalizado por trim e lower-case pt-BR).
     * Mantém ordenação decrescente por quantidade e, em empate, alfabética.
     */
    
    public Map<String, Long> contarPorMotivo() {
        return contarPorMotivo(solicitacoes.listarTodos());
    }

    public List<SolicitacaoCancelamento> listarPorPeriodo(LocalDateTime inicioInclusivo, LocalDateTime fimInclusivo) {
        Validadores.requerNaoNulo(inicioInclusivo, "Início do período");
        Validadores.requerNaoNulo(fimInclusivo, "Fim do período");
        if (fimInclusivo.isBefore(inicioInclusivo)) {
            throw new IllegalArgumentException("Fim do período não pode ser anterior ao início.");
        }
        return solicitacoes.listarTodos().stream()
                .filter(s -> !s.getDataSolicitacao().isBefore(inicioInclusivo))
                .filter(s -> !s.getDataSolicitacao().isAfter(fimInclusivo))
                .sorted(Comparator.comparing(SolicitacaoCancelamento::getDataSolicitacao)
                        .thenComparingLong(SolicitacaoCancelamento::getId))
                .collect(Collectors.toList());
    }

    public Map<String, Long> contarPorMotivoNoPeriodo(LocalDateTime inicioInclusivo, LocalDateTime fimInclusivo) {
        return contarPorMotivo(listarPorPeriodo(inicioInclusivo, fimInclusivo));
    }

    private Map<String, Long> contarPorMotivo(List<SolicitacaoCancelamento> lista) {
        Map<String, Long> bruto = lista.stream()
                .collect(Collectors.groupingBy(
                        s -> normalizarMotivo(s.getMotivo()),
                        Collectors.counting()
                ));

        return bruto.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    private String normalizarMotivo(String motivo) {
        if (motivo == null) {
            return "";
        }
        return motivo.trim().toLowerCase(new Locale("pt", "BR"));
    }
}

