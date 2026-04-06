package br.unb.eda2.loja.ui;

import br.unb.eda2.loja.dominio.HistoricoAlteracaoSolicitacao;
import br.unb.eda2.loja.dominio.SolicitacaoCancelamento;
import br.unb.eda2.loja.dominio.StatusSolicitacao;
import br.unb.eda2.loja.repositorio.SolicitacaoRepositorio;
import br.unb.eda2.loja.servico.CancelamentoService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;

/**
 * Fluxo de cancelamento: abrir solicitação, filtrar, análise e histórico.
 */
public class PainelSolicitacoes extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final SolicitacaoRepositorio solicitacoes;
    private final CancelamentoService cancelamento;

    private final JTextField novaId = new JTextField(6);
    private final JTextField novaCartao = new JTextField(6);
    private final JTextField novaMotivo = new JTextField(24);
    private final JTextField novaData = new JTextField(16);

    private final JComboBox<StatusSolicitacao> filtroStatus = new JComboBox<>(StatusSolicitacao.values());

    private final JTextField idAnalise = new JTextField(6);
    private final JTextField motivoRecusa = new JTextField(20);

    private final JTextField idHistorico = new JTextField(6);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[] { "id", "idCartão", "status", "motivo", "data" }, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modelo);

    private final JTextArea areaHistorico = new JTextArea(8, 40);

    public PainelSolicitacoes(SolicitacaoRepositorio solicitacoes, CancelamentoService cancelamento) {
        this.solicitacoes = solicitacoes;
        this.cancelamento = cancelamento;
        setLayout(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel abrir = new JPanel(new GridLayout(0, 2, 4, 4));
        abrir.setBorder(BorderFactory.createTitledBorder("Nova solicitação"));
        novaData.setToolTipText("dd/MM/yyyy HH:mm ou vazio = agora");
        abrir.add(new JLabel("ID solicitação:"));
        abrir.add(novaId);
        abrir.add(new JLabel("ID cartão:"));
        abrir.add(novaCartao);
        abrir.add(new JLabel("Motivo:"));
        abrir.add(novaMotivo);
        abrir.add(new JLabel("Data (opcional):"));
        abrir.add(novaData);
        JButton bAbrir = new JButton("Registrar");
        bAbrir.addActionListener(e -> abrirSolicitacao());
        abrir.add(new JLabel(""));
        abrir.add(bAbrir);

        JPanel lista = new JPanel(new BorderLayout(4, 4));
        lista.setBorder(BorderFactory.createTitledBorder("Listagem"));
        JPanel linhaFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaFiltro.add(new JLabel("Filtrar status:"));
        linhaFiltro.add(filtroStatus);
        JButton bFiltrar = new JButton("Aplicar filtro");
        bFiltrar.addActionListener(e -> listarFiltrado());
        JButton bTodos = new JButton("Todas");
        bTodos.addActionListener(e -> listarTodas());
        linhaFiltro.add(bFiltrar);
        linhaFiltro.add(bTodos);
        lista.add(linhaFiltro, BorderLayout.NORTH);

        JPanel analise = new JPanel(new GridLayout(0, 2, 4, 4));
        analise.setBorder(BorderFactory.createTitledBorder("Análise (use ID da solicitação)"));
        analise.add(new JLabel("ID solicitação:"));
        analise.add(idAnalise);
        analise.add(new JLabel("Motivo recusa (se recusar):"));
        analise.add(motivoRecusa);
        JPanel botoesAn = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton bEnviar = new JButton("Enviar p/ análise");
        bEnviar.addActionListener(e -> enviarAnalise());
        JButton bAprovar = new JButton("Aprovar");
        bAprovar.addActionListener(e -> aprovar());
        JButton bRecusar = new JButton("Recusar");
        bRecusar.addActionListener(e -> recusar());
        botoesAn.add(bEnviar);
        botoesAn.add(bAprovar);
        botoesAn.add(bRecusar);
        analise.add(new JLabel(""));
        analise.add(botoesAn);

        JPanel hist = new JPanel(new BorderLayout(4, 4));
        hist.setBorder(BorderFactory.createTitledBorder("Histórico"));
        JPanel linhaH = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaH.add(new JLabel("ID solicitação:"));
        linhaH.add(idHistorico);
        JButton bHist = new JButton("Carregar");
        bHist.addActionListener(e -> carregarHistorico());
        linhaH.add(bHist);
        hist.add(linhaH, BorderLayout.NORTH);
        areaHistorico.setEditable(false);
        areaHistorico.setFont(areaHistorico.getFont().deriveFont(12f));
        hist.add(new JScrollPane(areaHistorico), BorderLayout.CENTER);

        JPanel colDir = new JPanel(new BorderLayout(0, 8));
        colDir.add(abrir, BorderLayout.NORTH);
        colDir.add(analise, BorderLayout.CENTER);
        colDir.add(hist, BorderLayout.SOUTH);

        JPanel topo = new JPanel(new BorderLayout(8, 0));
        topo.add(lista, BorderLayout.CENTER);
        topo.add(colDir, BorderLayout.EAST);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        atualizarTabelaInicial();
    }

    public void atualizarTabelaInicial() {
        listarTodas();
    }

    private void abrirSolicitacao() {
        try {
            long id = Long.parseLong(novaId.getText().trim());
            long idCartao = Long.parseLong(novaCartao.getText().trim());
            String motivo = novaMotivo.getText().trim();
            LocalDateTime data = parseDataOuAgora(novaData.getText().trim());
            cancelamento.abrirSolicitacao(id, idCartao, motivo, data);
            UiAjuda.info("Solicitação registrada.");
            listarTodas();
        } catch (Exception ex) {
            UiAjuda.erro(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        }
    }

    private LocalDateTime parseDataOuAgora(String t) {
        if (t.isEmpty()) {
            return LocalDateTime.now();
        }
        try {
            return LocalDateTime.parse(t, FMT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data: use dd/MM/yyyy HH:mm ou deixe vazio.");
        }
    }

    private void listarTodas() {
        List<SolicitacaoCancelamento> lista = solicitacoes.listarTodos();
        lista.sort(Comparator.comparingLong(SolicitacaoCancelamento::getId));
        preencherTabela(lista);
    }

    private void listarFiltrado() {
        StatusSolicitacao st = (StatusSolicitacao) filtroStatus.getSelectedItem();
        List<SolicitacaoCancelamento> lista = cancelamento.listarPorStatus(st);
        lista.sort(Comparator.comparingLong(SolicitacaoCancelamento::getId));
        preencherTabela(lista);
    }

    private void preencherTabela(List<SolicitacaoCancelamento> lista) {
        modelo.setRowCount(0);
        for (SolicitacaoCancelamento s : lista) {
            modelo.addRow(new Object[] {
                    s.getId(), s.getIdCartao(), s.getStatus().name(), s.getMotivo(),
                    FMT.format(s.getDataSolicitacao())
            });
        }
    }

    private void enviarAnalise() {
        try {
            long id = Long.parseLong(idAnalise.getText().trim());
            cancelamento.enviarParaAnalise(id);
            UiAjuda.info("Enviada para análise.");
            listarTodas();
        } catch (Exception ex) {
            UiAjuda.erro(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        }
    }

    private void aprovar() {
        try {
            long id = Long.parseLong(idAnalise.getText().trim());
            cancelamento.aprovar(id);
            UiAjuda.info("Aprovada; cartão cancelado.");
            listarTodas();
        } catch (Exception ex) {
            UiAjuda.erro(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        }
    }

    private void recusar() {
        try {
            long id = Long.parseLong(idAnalise.getText().trim());
            cancelamento.recusar(id, motivoRecusa.getText().trim());
            UiAjuda.info("Recusada.");
            listarTodas();
        } catch (Exception ex) {
            UiAjuda.erro(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        }
    }

    private void carregarHistorico() {
        try {
            long id = Long.parseLong(idHistorico.getText().trim());
            List<HistoricoAlteracaoSolicitacao> h = cancelamento.listarHistorico(id);
            StringBuilder sb = new StringBuilder();
            for (HistoricoAlteracaoSolicitacao x : h) {
                sb.append(x.toString()).append('\n');
            }
            if (sb.length() == 0) {
                sb.append("(vazio)");
            }
            areaHistorico.setText(sb.toString());
        } catch (Exception ex) {
            UiAjuda.erro(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        }
    }
}
