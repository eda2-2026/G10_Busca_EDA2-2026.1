package br.unb.eda2.loja.ui;

import javax.swing.JOptionPane;

/**
 * Mensagens simples para não repetir JOptionPane em todo canto.
 */
public final class UiAjuda {

    private UiAjuda() {
    }

    public static void erro(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
