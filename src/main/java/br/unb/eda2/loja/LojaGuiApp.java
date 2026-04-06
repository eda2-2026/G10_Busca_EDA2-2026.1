package br.unb.eda2.loja;

import br.unb.eda2.loja.repositorio.CartaoRepositorio;
import br.unb.eda2.loja.repositorio.ClienteRepositorio;
import br.unb.eda2.loja.repositorio.SolicitacaoRepositorio;
import br.unb.eda2.loja.repositorio.memoria.CartaoRepositorioMemoria;
import br.unb.eda2.loja.repositorio.memoria.ClienteRepositorioMemoria;
import br.unb.eda2.loja.demo.DadosExemplo;
import br.unb.eda2.loja.repositorio.memoria.HistoricoSolicitacaoRepositorioMemoria;
import br.unb.eda2.loja.repositorio.memoria.SolicitacaoRepositorioMemoria;
import br.unb.eda2.loja.servico.CancelamentoService;
import br.unb.eda2.loja.ui.JanelaLoja;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Mesma composição de repositórios que {@link LojaApp}; interface gráfica para demonstração.
 */
public final class LojaGuiApp {

    private LojaGuiApp() {
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // mantém o LAF padrão
        }

        ClienteRepositorio clientes = new ClienteRepositorioMemoria();
        CartaoRepositorio cartoes = new CartaoRepositorioMemoria(clientes);
        SolicitacaoRepositorio solicitacoes = new SolicitacaoRepositorioMemoria(cartoes);
        HistoricoSolicitacaoRepositorioMemoria historicoRepo = new HistoricoSolicitacaoRepositorioMemoria();
        CancelamentoService cancelamento = new CancelamentoService(clientes, cartoes, solicitacoes, historicoRepo);

        if (DadosExemplo.deveCarregar(args)) {
            DadosExemplo.popular(clientes, cartoes, cancelamento);
        }

        SwingUtilities.invokeLater(() -> {
            JanelaLoja j = new JanelaLoja(clientes, cartoes, solicitacoes, cancelamento);
            j.setVisible(true);
        });
    }
}
