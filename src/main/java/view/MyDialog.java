package view;

import javax.swing.*;
import java.awt.*;

public class MyDialog {

    public static Object showInputDialog(Object message, int messageType, int optionType, String title, int width, int height) {
        JOptionPane pane = new JOptionPane(message, messageType, optionType);
        pane.setWantsInput(true);
        JDialog dialog = pane.createDialog(title);
        dialog.setSize(width, height);
        dialog.show();
        dialog.dispose();
        Object value = pane.getInputValue();

        if (value == JOptionPane.UNINITIALIZED_VALUE) {
            return null;
        }
        return value;
    }

    public static void showMessageDialog(Object message, int messageType, int optionType, String title, int width, int height) {
        JOptionPane pane = new JOptionPane(message, messageType, optionType);
        pane.setWantsInput(false);
        JDialog dialog = pane.createDialog(title);
        dialog.setSize(width, height);
        dialog.setFont(new Font("Monospaced", Font.BOLD, 20));

        dialog.show();
        dialog.dispose();
    }
}
