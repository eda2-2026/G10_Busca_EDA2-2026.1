package br.unb.eda2.loja.dominio;

import br.unb.eda2.loja.util.Validadores;

import java.util.Objects;

/**
 * Cliente da loja. O repositório garante unicidade de {@link #id} e de CPF.
 */
public class Cliente {

    private final long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;

    public Cliente(long id, String nome, String cpf) {
        Validadores.requerIdPositivo(id, "id");
        Validadores.requerNaoVazio(nome, "Nome");
        this.id = id;
        this.nome = nome.trim();
        this.cpf = Validadores.normalizarCpf(cpf);
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        Validadores.requerNaoVazio(nome, "Nome");
        this.nome = nome.trim();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = Validadores.normalizarCpf(cpf);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            this.email = null;
            return;
        }
        Validadores.requerEmailSimples(email.trim());
        this.email = email.trim();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone == null || telefone.isBlank()) {
            this.telefone = null;
            return;
        }
        this.telefone = telefone.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cliente cliente = (Cliente) o;
        return id == cliente.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", nome='" + nome + "', cpf='" + cpf + "'}";
    }
}
