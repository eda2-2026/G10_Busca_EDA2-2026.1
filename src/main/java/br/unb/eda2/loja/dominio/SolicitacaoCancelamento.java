package br.unb.eda2.loja.dominio;

import br.unb.eda2.loja.util.Validadores;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Pedido de cancelamento de cartão.
 */
public class SolicitacaoCancelamento {

    private final long id;
    private final long idCartao;
    private String motivo;
    private LocalDateTime dataSolicitacao;
    private StatusSolicitacao status;

    public SolicitacaoCancelamento(long id, long idCartao, String motivo,
                                   LocalDateTime dataSolicitacao, StatusSolicitacao status) {
        Validadores.requerIdPositivo(id, "id");
        Validadores.requerIdPositivo(idCartao, "idCartao");
        Validadores.requerNaoVazio(motivo, "Motivo");
        Validadores.requerNaoNulo(dataSolicitacao, "Data da solicitação");
        Validadores.requerNaoNulo(status, "Status");

        this.id = id;
        this.idCartao = idCartao;
        this.motivo = motivo.trim();
        this.dataSolicitacao = dataSolicitacao;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public long getIdCartao() {
        return idCartao;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        Validadores.requerNaoVazio(motivo, "Motivo");
        this.motivo = motivo.trim();
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = Validadores.requerNaoNulo(dataSolicitacao, "Data da solicitação");
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public void setStatus(StatusSolicitacao status) {
        this.status = Validadores.requerNaoNulo(status, "Status");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SolicitacaoCancelamento that = (SolicitacaoCancelamento) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SolicitacaoCancelamento{id=" + id + ", idCartao=" + idCartao
                + ", status=" + status + ", dataSolicitacao=" + dataSolicitacao + "}";
    }
}
