package view;

import javax.swing.*;

public class InputDialog {

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
}
