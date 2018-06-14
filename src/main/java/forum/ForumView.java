package forum;

import block.Block;
import block.BlockChain;
import controller.MainController;
import log.LogUtil;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import transaction.Transaction;
import transaction.TransactionUtil;
import util.KeyUtil;
import view.JProgressBar;
import view.KeyPairDialog;
import view.MyDialog;
import wallet.Wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;


public class ForumView {

    //UI Stuff
    public static JFrame mainFrame = null;
    private static JPanel toppanel, centerpanel;
    private static JTextField textfield;
    private static JButton sendbutton;
    private static JTextArea textArea;
    private static String privateKey = "";

    public ForumView() {
        try {
            //设置本属性将改变窗口边框样式定义
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
            UIManager.put("RootPane.setupButtonVisible", false);
            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (Exception e) {
            //TODO exception
        }
        mainFrame = new JFrame();
        init_UI();
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            //设置本属性将改变窗口边框样式定义
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
            UIManager.put("RootPane.setupButtonVisible", false);
            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (Exception e) {
            //TODO exception
        }
        ForumView window = new ForumView();
    }

    private static String getKeyText(Wallet wallet) {
        StringBuilder sb = new StringBuilder();
        sb.append("Public key:\n");
        sb.append(wallet.getPublicKey());
        sb.append("\n");
        sb.append("Private key:\n");
        sb.append(wallet.getPrivateKey());

        return sb.toString();
    }

    private static void rcv_data(String line) {
        if (line != null) {
            textArea.append(line + "\n");
            scroll_down();
        }
    }

    private static void scroll_down() {
        textArea.selectAll();//Sirve para mirar abajo
    }

    public static void addBlock(Block block) {
        if (block.getTransactions() != null) {
            for (Transaction transaction : block.getTransactions()) {
                String sender = transaction.getSender();
                rcv_data(sender.substring(sender.length() - 10, sender.length()) + ":");
                rcv_data(transaction.getData() + "\n");
            }
        }
    }

    public static void updateBlockChain() {
        for (Block block : BlockChain.blockChain) {
            addBlock(block);
        }
    }

    private void init_UI() {
        mainFrame.setBounds(200, 200, 800, 500);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container content = mainFrame.getContentPane();
        content.setLayout(new BorderLayout());

        mainFrame.setJMenuBar(getMainMenuBar());
        toppanel = new JPanel();
        toppanel.setLayout(new FlowLayout());
        textfield = new JTextField(110);

        sendbutton = new JButton("Send");
        sendbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String publicKey = mainFrame.getTitle();
                if ("".equals(publicKey)) {
                    MyDialog.showMessageDialog("请先登录",
                            JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.OK_OPTION, "", 600, 100);
                } else if (TransactionUtil.sendFunds(publicKey, privateKey, "", 5, textfield.getText())) {
                    textfield.setText("");
                    MyDialog.showMessageDialog("发送成功, 等待区块挖出",
                            JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.OK_OPTION, "", 600, 100);
                } else {
                    MyDialog.showMessageDialog("发送失败",
                            JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.OK_OPTION, "", 600, 100);
                }
            }
        });

        toppanel.add(textfield);
        toppanel.add(sendbutton);

        centerpanel = new JPanel();
        centerpanel.setLayout(new BorderLayout());
        centerpanel.setBorder(
                BorderFactory.createLineBorder(Color.gray));
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("", Font.PLAIN, 20));
        textArea.setBackground(Color.white);
        JScrollPane scrollpane = new JScrollPane(textArea);

        centerpanel.add(scrollpane, BorderLayout.CENTER);

        content.add(toppanel, BorderLayout.SOUTH);
        content.add(centerpanel, BorderLayout.CENTER);
    }

    public JMenuBar getMainMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(getLoginMenu());
        menuBar.add(getUpdateMenu());
        menuBar.add(getMiningMenu());

        return menuBar;
    }

    private JMenu getLoginMenu() {
        JMenu menu = new JMenu("Login");
        menu.setFont(new Font("Monospaced", Font.BOLD, 14));

        menu.add(getRegisterMenuItem());
        menu.add(getLoginMenuItem());

        return menu;
    }

    private JMenu getUpdateMenu() {
        JMenu menu = new JMenu("Update");
        menu.setFont(new Font("Monospaced", Font.BOLD, 14));

        menu.add(getJoinBLockChainMenuItem());

        return menu;
    }

    private JMenu getMiningMenu() {
        JMenu menu = new JMenu("Mine");
        menu.setFont(new Font("Monospaced", Font.BOLD, 14));

        menu.add(getMiningMenuItem());

        return menu;
    }

    private JMenuItem getRegisterMenuItem() {
        JMenuItem registerItem = new JMenuItem("Register");
        registerItem.setFont(new Font("Monospaced", Font.BOLD, 12));
        registerItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Wallet w = new Wallet();
                        String keyString = getKeyText(w);
                        KeyPairDialog keyPairDialog = new KeyPairDialog(keyString, 600, 300);
                        LogUtil.Log(Level.INFO, "View: register");
                    }
                }
        );
        return registerItem;
    }

    private JMenuItem getLoginMenuItem() {
        JMenuItem loginItem = new JMenuItem("Login");
        loginItem.setFont(new Font("Monospaced", Font.BOLD, 12));
        loginItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Object pubKey = MyDialog.showInputDialog("Enter public key", JOptionPane.PLAIN_MESSAGE,
                                JOptionPane.OK_CANCEL_OPTION, "", 500, 180);
                        if (pubKey != null) {
                            if (KeyUtil.isValidPublicKey(pubKey.toString())) {
                                mainFrame.setTitle(pubKey.toString());
                            } else {
                                MyDialog.showMessageDialog("Invalid public key",
                                        JOptionPane.PLAIN_MESSAGE,
                                        JOptionPane.OK_OPTION, "", 600, 100);
                            }
                        }

                        Object priKey = MyDialog.showInputDialog("Enter private key", JOptionPane.PLAIN_MESSAGE,
                                JOptionPane.OK_CANCEL_OPTION, "", 500, 180);
                        if (priKey != null) {
                            privateKey = priKey.toString();
                            if (!KeyUtil.isValidPrivateKey(privateKey)) {
                                MyDialog.showMessageDialog("Invalid private key",
                                        JOptionPane.PLAIN_MESSAGE,
                                        JOptionPane.OK_OPTION, "", 600, 100);
                            }
                        }
                    }
                }
        );

        return loginItem;
    }

    private JMenuItem getJoinBLockChainMenuItem() {
        JMenuItem joinItem = new JMenuItem("Join chain");
        joinItem.setFont(new Font("Monospaced", Font.BOLD, 12));
        joinItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        MainController.updataBlockChain();
                        view.JProgressBar.generateProgressBar(5, "Download blockchain...");
                        LogUtil.Log(Level.INFO, "View: join blockchain");
                    }
                }
        );

        return joinItem;
    }

    private JMenuItem getMiningMenuItem() {
        JMenuItem miningItem = new JMenuItem("Mining");
        miningItem.setFont(new Font("Monospaced", Font.BOLD, 12));
        miningItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Object data = MyDialog.showInputDialog("Input the data:", JOptionPane.PLAIN_MESSAGE,
                        //        JOptionPane.OK_CANCEL_OPTION, "", 500, 180);
                        String publicKey = mainFrame.getTitle();
                        if (!"".equals(publicKey) && publicKey != null && KeyUtil.isValidPublicKey(publicKey)) {
                            JProgressBar.generateProgressBar(5, "Mining...");
                            MainController.mining(publicKey);
                            LogUtil.Log(Level.INFO, "View: mining");
                        } else {
                            MyDialog.showMessageDialog("请先登录",
                                    JOptionPane.PLAIN_MESSAGE,
                                    JOptionPane.OK_OPTION, "", 600, 100);
                        }
                    }
                }
        );

        return miningItem;
    }
}