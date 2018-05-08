package view;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KeyPairDialog {
    public KeyPairDialog(String msg, int width, int height) {
        Clipboard cl = Toolkit.getDefaultToolkit().getSystemClipboard();
        JFrame frame = new JFrame("Key pair");
        frame.setLayout(new BorderLayout());
        frame.setSize(width, height);

        JTextArea textArea = new JTextArea(msg);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.BOLD, 18));
        textArea.setLineWrap(true);// 激活自动换行功能
        textArea.setWrapStyleWord(true);// 激活断行不断字功能
        frame.add(textArea, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));

        JLabel tempLeft = new JLabel();
        buttonPanel.add(tempLeft);
        JButton button = new JButton("Copy");
        button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        StringSelection selection = new StringSelection(msg);
                        cl.setContents(selection, selection);
                    }
                }
        );
        buttonPanel.add(button);
        JLabel tempRight = new JLabel();
        buttonPanel.add(tempRight);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            //设置本属性将改变窗口边框样式定义
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
            UIManager.put("RootPane.setupButtonVisible", false);
            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (Exception e) {
            //TODO exception
        }
        KeyPairDialog k = new KeyPairDialog("Public key:\n" +
                "MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAE1NLvEjUQvOD+NovCMI3nIglErLJ2faNl5gBNHy6kBVe2y3yiN/rkS+DgNWUAlX4l\n" +
                "Private key:\n" +
                "MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAE1NLvEjUQvOD+NovCMI3nIglErLJ2faNl5gBNHy6kBVe2y3yiN/rkS+DgNWUAlX4l", 600, 300);
    }
}
