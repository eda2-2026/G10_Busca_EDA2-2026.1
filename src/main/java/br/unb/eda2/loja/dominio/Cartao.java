package br.unb.eda2.loja.dominio;

import br.unb.eda2.loja.util.Validadores;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Cartão de crédito/loja vinculado a um cliente.
 */
public class Cartao {

    private final long id;
    private final long idCliente;
    private String numeroMascarado;
    private String bandeira;
    private BigDecimal limite;
    private StatusCartao status;

    public Cartao(long id, long idCliente, String numeroMascarado, String bandeira,
                  BigDecimal limite, StatusCartao status) {
        Validadores.requerIdPositivo(id, "id");
        Validadores.requerIdPositivo(idCliente, "idCliente");
        Validadores.requerNaoVazio(numeroMascarado, "Número mascarado");
        Validadores.requerNaoVazio(bandeira, "Bandeira");
        Validadores.requerNaoNulo(limite, "Limite");
        Validadores.requerNaoNulo(status, "Status");

        if (limite.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Limite não pode ser negativo.");
        }

        this.id = id;
        this.idCliente = idCliente;
        this.numeroMascarado = numeroMascarado.trim();
        this.bandeira = bandeira.trim();
        this.limite = limite;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public long getIdCliente() {
        return idCliente;
    }

    public String getNumeroMascarado() {
        return numeroMascarado;
    }

    public void setNumeroMascarado(String numeroMascarado) {
        Validadores.requerNaoVazio(numeroMascarado, "Número mascarado");
        this.numeroMascarado = numeroMascarado.trim();
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        Validadores.requerNaoVazio(bandeira, "Bandeira");
        this.bandeira = bandeira.trim();
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        Validadores.requerNaoNulo(limite, "Limite");
        if (limite.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Limite não pode ser negativo.");
        }
        this.limite = limite;
    }

    public StatusCartao getStatus() {
        return status;
    }

    public void setStatus(StatusCartao status) {
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
        Cartao cartao = (Cartao) o;
        return id == cartao.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cartao{id=" + id + ", idCliente=" + idCliente + ", bandeira='" + bandeira
                + "', status=" + status + "}";
    }
}
