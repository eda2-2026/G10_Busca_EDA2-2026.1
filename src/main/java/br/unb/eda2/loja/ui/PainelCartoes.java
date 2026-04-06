package br.unb.eda2.loja.ui;

import br.unb.eda2.loja.dominio.Cartao;
import br.unb.eda2.loja.dominio.StatusCartao;
import br.unb.eda2.loja.repositorio.CartaoRepositorio;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * Cartões: cadastro e listagens (tabela hash no repositório).
 */
public class PainelCartoes extends JPanel {

    private final CartaoRepositorio repo;

    private final JTextField campoId = new JTextField(8);
    private final JTextField campoIdCliente = new JTextField(8);
    private final JTextField campoNum = new JTextField(14);
    private final JTextField campoBandeira = new JTextField(12);
    private final JTextField campoLimite = new JTextField(10);
    private final JComboBox<StatusCartao> comboStatus = new JComboBox<>(StatusCartao.values());

    private final JTextField buscaId = new JTextField(8);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[] { "id", "idCliente", "número", "bandeira", "limite", "status" }, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modelo);

    public PainelCartoes(CartaoRepositorio repo) {
        this.repo = repo;
        setLayout(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel form = new JPanel(new GridLayout(0, 2, 4, 4));
        form.setBorder(BorderFactory.createTitledBorder("Novo cartão"));
        form.add(new JLabel("ID cartão:"));
        form.add(campoId);
        form.add(new JLabel("ID cliente:"));
        form.add(campoIdCliente);
        form.add(new JLabel("Número mascarado:"));
        form.add(campoNum);
        form.add(new JLabel("Bandeira:"));
        form.add(campoBandeira);
        form.add(new JLabel("Limite:"));
        form.add(campoLimite);
        form.add(new JLabel("Status:"));
        form.add(comboStatus);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton salvar = new JButton("Salvar");
        salvar.addActionListener(e -> salvar());
        JButton listar = new JButton("Listar todos");
        listar.addActionListener(e -> listarTodos());
        botoes.add(salvar);
        botoes.add(listar);

        JPanel linhaBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        linhaBusca.add(new JLabel("Buscar ID:"));
        linhaBusca.add(buscaId);
        JButton bBusca = new JButton("Buscar");
        bBusca.addActionListener(e -> buscarId());
        linhaBusca.add(bBusca);

        JPanel norte = new JPanel(new BorderLayout(0, 6));
        norte.add(form, BorderLayout.CENTER);
        norte.add(botoes, BorderLayout.SOUTH);
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.add(norte, BorderLayout.NORTH);
        wrap.add(linhaBusca, BorderLayout.SOUTH);

        add(wrap, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        atualizarTabelaInicial();
    }

    public void atualizarTabelaInicial() {
        listarTodos();
    }

    private void salvar() {
        try {
            long id = Long.parseLong(campoId.getText().trim());
            long idCli = Long.parseLong(campoIdCliente.getText().trim());
            String num = campoNum.getText().trim();
            String band = campoBandeira.getText().trim();
            String limTxt = campoLimite.getText().trim().replace(',', '.');
            BigDecimal lim = new BigDecimal(limTxt);
            StatusCartao st = (StatusCartao) comboStatus.getSelectedItem();
            Cartao c = new Cartao(id, idCli, num, band, lim, st);
            repo.salvar(c);
            UiAjuda.info("Cartão salvo.");
            listarTodos();
        } catch (Exception ex) {
            UiAjuda.erro(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        }
    }

    private void buscarId() {
        try {
            long id = Long.parseLong(buscaId.getText().trim());
            repo.buscarPorId(id).ifPresentOrElse(
                    this::mostrarUm,
                    () -> UiAjuda.info("Não encontrado.")
            );
        } catch (NumberFormatException e) {
            UiAjuda.erro("ID inválido.");
        }
    }

    private void mostrarUm(Cartao c) {
        modelo.setRowCount(0);
        addLinha(c);
    }

    private void addLinha(Cartao c) {
        modelo.addRow(new Object[] {
                c.getId(), c.getIdCliente(), c.getNumeroMascarado(), c.getBandeira(),
                c.getLimite().toPlainString(), c.getStatus().name()
        });
    }

    private void listarTodos() {
        List<Cartao> lista = repo.listarTodos();
        lista.sort(Comparator.comparingLong(Cartao::getId));
        modelo.setRowCount(0);
        for (Cartao c : lista) {
            addLinha(c);
        }
    }
}
