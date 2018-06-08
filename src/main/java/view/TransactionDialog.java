package view;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import smartContract.Report;
import util.IPUtil;
import util.KeyUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TransactionDialog {
    private JFrame frame = new JFrame("Transaction");
    private Container c = frame.getContentPane();
    private JTextField senderPublicKey = new JTextField();
    private JPasswordField senderPrivateKey = new JPasswordField();
    private JTextField Ip = new JTextField();
    private JTextField value = new JTextField();
    private JButton ok = new JButton("确定");

    public TransactionDialog() {
        frame.setSize(900, 300);
        c.setLayout(new BorderLayout());
        initFrame();
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
        new TransactionDialog();
    }

    private void initFrame() {
        //顶部
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        titlePanel.add(new JLabel("Transaction"));
        c.add(titlePanel, "North");

        //中部表单
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(null);

        JLabel a1 = new JLabel("Public key:");
        a1.setBounds(50, 20, 150, 30);

        JLabel a2 = new JLabel("Private key:");
        a2.setBounds(50, 60, 150, 30);

        JLabel a3 = new JLabel("Ip:");
        a3.setBounds(50, 100, 150, 30);

        JLabel a4 = new JLabel("Value:");
        a4.setBounds(50, 140, 150, 30);

        fieldPanel.add(a1);
        fieldPanel.add(a2);
        fieldPanel.add(a3);
        fieldPanel.add(a4);

        senderPublicKey.setBounds(230, 20, 600, 30);
        senderPrivateKey.setBounds(230, 60, 600, 30);
        Ip.setBounds(230, 100, 600, 30);
        value.setBounds(230, 140, 600, 30);
        fieldPanel.add(senderPublicKey);
        fieldPanel.add(senderPrivateKey);
        fieldPanel.add(Ip);
        fieldPanel.add(value);
        c.add(fieldPanel, "Center");

        //底部按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(ok);
        c.add(buttonPanel, "South");

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String publicKey = senderPublicKey.getText();
                String privateKey = senderPrivateKey.getText();
                if (KeyUtil.isValidPublicKey(publicKey) && KeyUtil.isValidPrivateKey(privateKey)) {
                    String ipNumber = Ip.getText();
                    if (IPUtil.isIp(ipNumber)) {
                        double v = Double.parseDouble(value.getText());
                        boolean suc = false;
                        if (v >= 0) {
                            suc = Report.reportBlackList(publicKey, privateKey, v, ipNumber);
                        } else {
                            suc = Report.reportBlackList(publicKey, privateKey, v, ipNumber, true);
                        }

                        if (suc) {
                            MyDialog.showMessageDialog("Success",
                                    JOptionPane.PLAIN_MESSAGE,
                                    JOptionPane.OK_OPTION, "", 600, 100);
                        } else {
                            MyDialog.showMessageDialog("Fail",
                                    JOptionPane.PLAIN_MESSAGE,
                                    JOptionPane.OK_OPTION, "", 600, 100);
                        }
                    } else {
                        MyDialog.showMessageDialog("Invalid Ip",
                                JOptionPane.PLAIN_MESSAGE,
                                JOptionPane.OK_OPTION, "", 600, 100);
                    }
                } else {
                    MyDialog.showMessageDialog("Invalid key",
                            JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.OK_OPTION, "", 600, 100);
                }
            }
        });
    }
}