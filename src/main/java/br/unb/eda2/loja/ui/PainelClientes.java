package br.unb.eda2.loja.ui;

import br.unb.eda2.loja.dominio.Cartao;
import br.unb.eda2.loja.dominio.Cliente;
import br.unb.eda2.loja.repositorio.CartaoRepositorio;
import br.unb.eda2.loja.repositorio.ClienteRepositorio;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Comparator;
import java.util.List;

/**
 * Cadastro e buscas de clientes (hash + árvore por trás do repositório).
 * Abaixo da tabela de clientes: cartões da pessoa selecionada (status, limite, etc.).
 */
public class PainelClientes extends JPanel {

    private final ClienteRepositorio repo;
    private final CartaoRepositorio repoCartoes;

    private final JLabel rotuloCartoes = new JLabel("Selecione um cliente na tabela acima para ver os cartões.");

    private final JTextField campoId = new JTextField(8);
    private final JTextField campoNome = new JTextField(18);
    private final JTextField campoCpf = new JTextField(14);
    private final JTextField campoEmail = new JTextField(16);
    private final JTextField campoTel = new JTextField(12);
    private final JCheckBox chkAtivo = new JCheckBox("Ativo", true);

    private final JTextField buscaId = new JTextField(8);
    private final JTextField buscaCpf = new JTextField(14);
    private final JTextField arvoreNome = new JTextField(14);
    private final JTextField arvoreId = new JTextField(8);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[] { "id", "nome", "cpf", "ativo", "email" }, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modelo);

    private final DefaultTableModel modeloCartoes = new DefaultTableModel(
            new Object[] { "id cartão", "número", "bandeira", "limite", "status" }, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabelaCartoes = new JTable(modeloCartoes);

    public PainelClientes(ClienteRepositorio repo, CartaoRepositorio cartoes) {
        this.repo = repo;
        this.repoCartoes = cartoes;
        setLayout(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel form = new JPanel(new GridLayout(0, 2, 4, 4));
        form.setBorder(BorderFactory.createTitledBorder("Cadastrar / alterar cliente"));
        form.add(new JLabel("ID:"));
        form.add(campoId);
        form.add(new JLabel("Nome:"));
        form.add(campoNome);
        form.add(new JLabel("CPF (11 dígitos):"));
        form.add(campoCpf);
        form.add(new JLabel("E-mail (opcional):"));
        form.add(campoEmail);
        form.add(new JLabel("Telefone (opcional):"));
        form.add(campoTel);
        form.add(new JLabel(""));
        form.add(chkAtivo);

        JPanel botoesCad = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton salvar = new JButton("Salvar");
        salvar.addActionListener(e -> salvar());
        botoesCad.add(salvar);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(form, BorderLayout.CENTER);
        norte.add(botoesCad, BorderLayout.SOUTH);

        JPanel buscas = new JPanel(new GridLayout(0, 2, 4, 4));
        buscas.setBorder(BorderFactory.createTitledBorder("Buscas"));
        buscaId.setToolTipText("id numérico");
        buscaCpf.setToolTipText("só números, 11 dígitos");
        JButton bId = new JButton("Buscar por ID");
        bId.addActionListener(e -> buscarId());
        JButton bCpf = new JButton("Buscar por CPF");
        bCpf.addActionListener(e -> buscarCpf());
        JButton bTodos = new JButton("Listar todos");
        bTodos.addActionListener(e -> listarTodos());
        JButton bOrd = new JButton("Ordenados por nome (árvore)");
        bOrd.addActionListener(e -> listarPorNomeArvore());
        buscas.add(new JLabel("ID p/ busca:"));
        JPanel linhaId = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        linhaId.add(buscaId);
        linhaId.add(bId);
        buscas.add(linhaId);
        buscas.add(new JLabel("CPF p/ busca:"));
        JPanel linhaCpf = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        linhaCpf.add(buscaCpf);
        linhaCpf.add(bCpf);
        buscas.add(linhaCpf);
        buscas.add(bTodos);
        buscas.add(bOrd);

        buscas.add(new JLabel("Nome + ID (árvore):"));
        JPanel linhaArv = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        linhaArv.add(arvoreNome);
        linhaArv.add(new JLabel("id:"));
        linhaArv.add(arvoreId);
        JButton bArv = new JButton("Buscar");
        bArv.addActionListener(e -> buscarArvore());
        linhaArv.add(bArv);
        buscas.add(linhaArv);

        JPanel centro = new JPanel(new BorderLayout(0, 8));
        centro.add(norte, BorderLayout.NORTH);
        centro.add(buscas, BorderLayout.CENTER);

        tabela.setFillsViewportHeight(true);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(this::aoMudarSelecaoCliente);

        JPanel sulCartoes = new JPanel(new BorderLayout(4, 4));
        sulCartoes.setBorder(BorderFactory.createTitledBorder("Cartões deste cliente"));
        JPanel topoCartoes = new JPanel(new BorderLayout());
        topoCartoes.add(rotuloCartoes, BorderLayout.WEST);
        sulCartoes.add(topoCartoes, BorderLayout.NORTH);
        tabelaCartoes.setFillsViewportHeight(true);
        sulCartoes.add(new JScrollPane(tabelaCartoes), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(tabela), sulCartoes);
        split.setResizeWeight(0.42);
        split.setOneTouchExpandable(true);

        add(centro, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);

        atualizarTabelaInicial();
    }

    private void aoMudarSelecaoCliente(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        int row = tabela.getSelectedRow();
        if (row < 0) {
            limparCartoes();
            return;
        }
        try {
            long id = Long.parseLong(modelo.getValueAt(row, 0).toString());
            String nome = modelo.getValueAt(row, 1).toString();
            preencherCartoes(id, nome);
        } catch (Exception ex) {
            limparCartoes();
        }
    }

    /** Recarrega a grade com tudo que está no repositório (útil após carregar dados de exemplo). */
    public void atualizarTabelaInicial() {
        listarTodos();
    }

    private void salvar() {
        try {
            long id = Long.parseLong(campoId.getText().trim());
            String nome = campoNome.getText();
            String cpf = campoCpf.getText();
            Cliente c = new Cliente(id, nome, cpf);
            String em = campoEmail.getText().trim();
            if (!em.isEmpty()) {
                c.setEmail(em);
            }
            String tel = campoTel.getText().trim();
            if (!tel.isEmpty()) {
                c.setTelefone(tel);
            }
            c.setAtivo(chkAtivo.isSelected());
            repo.salvar(c);
            UiAjuda.info("Cliente salvo.");
            listarTodos();
            selecionarClientePorId(id);
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

    private void buscarCpf() {
        try {
            repo.buscarPorCpf(buscaCpf.getText().trim()).ifPresentOrElse(
                    this::mostrarUm,
                    () -> UiAjuda.info("Não encontrado.")
            );
        } catch (Exception ex) {
            UiAjuda.erro(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        }
    }

    private void mostrarUm(Cliente c) {
        modelo.setRowCount(0);
        addLinha(c);
        selecionarClientePorId(c.getId());
    }

    /** Seleciona a linha do cliente para disparar a lista de cartões (sem depender do clique manual). */
    private void selecionarClientePorId(long idCliente) {
        for (int r = 0; r < modelo.getRowCount(); r++) {
            if (Long.parseLong(modelo.getValueAt(r, 0).toString()) == idCliente) {
                tabela.setRowSelectionInterval(r, r);
                return;
            }
        }
    }

    private void addLinha(Cliente c) {
        modelo.addRow(new Object[] {
                c.getId(), c.getNome(), c.getCpf(), c.isAtivo() ? "sim" : "não",
                c.getEmail() != null ? c.getEmail() : ""
        });
    }

    private void listarTodos() {
        try {
            List<Cliente> lista = repo.listarTodos();
            lista.sort(Comparator.comparingLong(Cliente::getId));
            modelo.setRowCount(0);
            for (Cliente c : lista) {
                addLinha(c);
            }
            limparCartoes();
        } catch (Exception ex) {
            UiAjuda.erro(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        }
    }

    private void listarPorNomeArvore() {
        try {
            List<Cliente> lista = repo.listarOrdenadosPorNome();
            modelo.setRowCount(0);
            for (Cliente c : lista) {
                addLinha(c);
            }
            limparCartoes();
        } catch (Exception ex) {
            UiAjuda.erro(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        }
    }

    private void limparCartoes() {
        tabela.clearSelection();
        modeloCartoes.setRowCount(0);
        rotuloCartoes.setText("Selecione um cliente na tabela acima para ver os cartões.");
    }

    private void preencherCartoes(long idCliente, String nomeCliente) {
        List<Cartao> lista = repoCartoes.listarPorIdCliente(idCliente);
        lista.sort(Comparator.comparingLong(Cartao::getId));
        modeloCartoes.setRowCount(0);
        rotuloCartoes.setText("Cliente id=" + idCliente + " — " + nomeCliente + "  ·  "
                + lista.size() + " cartão(ões)");
        for (Cartao z : lista) {
            modeloCartoes.addRow(new Object[] {
                    z.getId(), z.getNumeroMascarado(), z.getBandeira(),
                    z.getLimite().toPlainString(), z.getStatus().name()
            });
        }
    }

    private void buscarArvore() {
        try {
            String nome = arvoreNome.getText().trim();
            long id = Long.parseLong(arvoreId.getText().trim());
            repo.buscarPorNomeEId(nome, id).ifPresentOrElse(
                    this::mostrarUm,
                    () -> UiAjuda.info("Combinação nome + id não encontrada na árvore.")
            );
        } catch (NumberFormatException e) {
            UiAjuda.erro("ID inválido.");
        } catch (Exception ex) {
            UiAjuda.erro(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        }
    }
}
