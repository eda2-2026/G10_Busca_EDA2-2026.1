package br.unb.eda2.loja.dominio;

import br.unb.eda2.loja.util.Validadores;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Registro de mudança de status em uma solicitação de cancelamento.
 */
public class HistoricoAlteracaoSolicitacao {

    private final long id;
    private final long idSolicitacao;
    private final LocalDateTime dataHora;
    private final StatusSolicitacao statusAnterior;
    private final StatusSolicitacao statusNovo;
    private final String detalhe;

    public HistoricoAlteracaoSolicitacao(long id, long idSolicitacao, LocalDateTime dataHora,
                                         StatusSolicitacao statusAnterior, StatusSolicitacao statusNovo,
                                         String detalhe) {
        Validadores.requerIdPositivo(id, "id");
        Validadores.requerIdPositivo(idSolicitacao, "idSolicitacao");
        Validadores.requerNaoNulo(dataHora, "Data/hora");
        Validadores.requerNaoNulo(statusNovo, "Status novo");
        this.id = id;
        this.idSolicitacao = idSolicitacao;
        this.dataHora = dataHora;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.detalhe = detalhe == null || detalhe.isBlank() ? null : detalhe.trim();
    }

    public long getId() {
        return id;
    }

    public long getIdSolicitacao() {
        return idSolicitacao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public StatusSolicitacao getStatusAnterior() {
        return statusAnterior;
    }

    public StatusSolicitacao getStatusNovo() {
        return statusNovo;
    }

    public String getDetalhe() {
        return detalhe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HistoricoAlteracaoSolicitacao that = (HistoricoAlteracaoSolicitacao) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "HistoricoAlteracaoSolicitacao{id=" + id + ", idSolicitacao=" + idSolicitacao
                + ", dataHora=" + dataHora + ", " + statusAnterior + " -> " + statusNovo
                + (detalhe != null ? ", detalhe='" + detalhe + "'" : "") + "}";
    }
}
