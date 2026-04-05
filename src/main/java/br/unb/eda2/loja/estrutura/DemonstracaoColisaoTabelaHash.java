package br.unb.eda2.loja.estrutura;

import br.unb.eda2.loja.dominio.Cartao;
import br.unb.eda2.loja.dominio.Cliente;
import br.unb.eda2.loja.dominio.StatusCartao;

import java.math.BigDecimal;

/**
 * Demonstração intencional de colisões: capacidade pequena força vários elementos
 * no mesmo balde; o encadeamento mantém busca correta por id ou CPF.
 * Executar: {@code java br.unb.eda2.loja.estrutura.DemonstracaoColisaoTabelaHash}
 */

public final class DemonstracaoColisaoTabelaHash {

    private DemonstracaoColisaoTabelaHash() {
    }

    public static void main(String[] args) {
        final int m = 5;
        TabelaHashClientes thc = new TabelaHashClientes(m);
        TabelaHashCartoes thz = new TabelaHashCartoes(m);

        System.out.println("=== TabelaHashClientes (m=" + m + ") — índices por id ===");
        for (int i = 1; i <= 8; i++) {
            String cpf = String.format("%011d", 10000000000L + i);
            Cliente c = new Cliente(i, "Cliente " + i, cpf);
            thc.inserir(c);
            int idx = Math.floorMod(Long.hashCode(i), m);
            System.out.println("inserido id=" + i + " -> balde " + idx + " (colisões no mesmo balde = lista encadeada)");
        }
        System.out.println("buscar id=3: " + thc.buscarPorId(3));
        System.out.println("buscar CPF 10000000004: " + thc.buscarPorCpf("10000000004"));

        System.out.println();
        System.out.println("=== TabelaHashCartoes (m=" + m + ") ===");
        for (long id = 1; id <= 6; id++) {
            Cartao z = new Cartao(id, 1L, "****" + id, "VISA", BigDecimal.valueOf(1000), StatusCartao.ATIVO);
            thz.inserir(z);
        }
        System.out.println("buscar cartão id=4: " + thz.buscar(4));
        System.out.println("Total cartões armazenados: " + thz.valores().size());
    }
}
