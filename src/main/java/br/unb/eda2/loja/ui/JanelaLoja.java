package br.unb.eda2.loja.ui;

import br.unb.eda2.loja.repositorio.CartaoRepositorio;
import br.unb.eda2.loja.repositorio.ClienteRepositorio;
import br.unb.eda2.loja.repositorio.SolicitacaoRepositorio;
import br.unb.eda2.loja.servico.CancelamentoService;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;

/**
 * Janela principal: abas para clientes, cartões e cancelamento.
 */
public class JanelaLoja extends JFrame {

    public JanelaLoja(ClienteRepositorio clientes, CartaoRepositorio cartoes,
                      SolicitacaoRepositorio solicitacoes, CancelamentoService cancelamento) {
        super("Loja — clientes e cancelamento");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(880, 560);
        setLocationByPlatform(true);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Clientes", new PainelClientes(clientes, cartoes));
        abas.addTab("Cartões", new PainelCartoes(cartoes));
        abas.addTab("Solicitações", new PainelSolicitacoes(solicitacoes, cancelamento));

        getContentPane().add(abas, BorderLayout.CENTER);
    }
}
