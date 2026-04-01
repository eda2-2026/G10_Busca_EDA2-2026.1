package br.unb.eda2.loja.util;

import java.util.Objects;

/**
 * Validações básicas usadas pelas entidades. Unicidade de id em coleções fica no repositório.
 */
public final class Validadores {

    private Validadores() {
    }

    public static void requerIdPositivo(long id, String nomeCampo) {
        if (id <= 0) {
            throw new IllegalArgumentException(nomeCampo + " deve ser um identificador positivo.");
        }
    }

    public static void requerNaoVazio(String valor, String nomeCampo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(nomeCampo + " não pode ser vazio.");
        }
    }

    /**
     * CPF: apenas dígitos, 11 posições (sem pontuação na armazenagem).
     */
    public static String normalizarCpf(String cpf) {
        requerNaoVazio(cpf, "CPF");
        String soDigitos = cpf.replaceAll("\\D", "");
        if (soDigitos.length() != 11) {
            throw new IllegalArgumentException("CPF deve ter 11 dígitos.");
        }
        return soDigitos;
    }

    public static void requerEmailSimples(String email) {
        requerNaoVazio(email, "E-mail");
        if (!email.contains("@") || email.indexOf('@') == 0 || email.endsWith("@")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
    }

    public static <T> T requerNaoNulo(T objeto, String nomeCampo) {
        return Objects.requireNonNull(objeto, nomeCampo + " não pode ser nulo.");
    }
}
