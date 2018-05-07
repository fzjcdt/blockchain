package view;

import block.Block;
import block.BlockChain;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView {
    JFrame mainFrame = new JFrame("");
    DefaultTableModel blockModel;
    JTable blockTable;
    private static final int BLOCK_COLUMN = 5;

    public void init() {
        try {
            //设置本属性将改变窗口边框样式定义
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
            UIManager.put("RootPane.setupButtonVisible", false);
            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (Exception e) {
            //TODO exception
        }
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setJMenuBar(getMainMenuBar());
        mainFrame.add(getMainTablePane());
        JButton b = new JButton("update");
        b.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        //mainFrame.add(b, BorderLayout.SOUTH);

        mainFrame.setSize(1400, 800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    private void setBlockModel() {
        String[] headerNames = {"prevHash", "hash", "data", "nonce", "timeStamp"};
        blockModel = new DefaultTableModel(headerNames, BlockChain.blockChain.size());

        /*
        List<Block> blockChain = new ArrayList<Block>();
        blockChain.add(Block.genesisBlock());
        blockChain.add(Block.genesisBlock());
        blockChain.add(Block.genesisBlock());
        blockChain.add(Block.genesisBlock());
        blockChain.add(Block.genesisBlock());

        */
        for (int len = BlockChain.blockChain.size() - 1, i = len; i >= 0; i--) {
            Block block = BlockChain.blockChain.get(i);

            blockModel.setValueAt(block.getPrevHash(), len - i, 0);
            blockModel.setValueAt(block.getHash(), len - i, 1);
            blockModel.setValueAt(block.getData(), len - i, 2);
            blockModel.setValueAt(String.valueOf(block.getNonce()), len - i, 3);
            blockModel.setValueAt(String.valueOf(block.getTimeStamp()), len - i, 4);
        }
    }

    private void setBlockTable() {
        setBlockModel();
        blockTable = new JTable(blockModel);
        blockTable.setFont(new Font("Monospaced", Font.PLAIN, 20));
        blockTable.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 22));
        blockTable.setRowHeight(24);

        // 数据居中
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        blockTable.setDefaultRenderer(Object.class, render);

        // 表头居中
        DefaultTableCellHeaderRenderer hr = new DefaultTableCellHeaderRenderer();
        hr.setHorizontalAlignment(JLabel.CENTER);
        blockTable.getTableHeader().setDefaultRenderer(hr);
    }

    private JPanel getBlockPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        setBlockTable();
        //panel.add(new JScrollPane(blockTable), BorderLayout.CENTER);
        panel.add(new JScrollPane(blockTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel getIpPanel() {
        JPanel panel = new JPanel();

        return panel;
    }

    private JPanel getUserPanel() {
        JPanel panel = new JPanel();

        return panel;
    }

    private JPanel getMinePanel() {
        JPanel panel = new JPanel();

        return panel;
    }


    private JTabbedPane getMainTablePane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Monospaced", Font.PLAIN, 15));

        tabbedPane.addTab("Block", null, getBlockPanel(), "Block information");
        tabbedPane.addTab(" Ip  ", null, getIpPanel(), "IP maliciousness");
        tabbedPane.addTab("User ", null, getUserPanel(), "User's ranking");
        tabbedPane.addTab("Mine ", null, getMinePanel(), "Panel for mining");

        /*
        JPanel panel2 = new JPanel();
        JButton button = new JButton("button");
        button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        InputDialog.showInputDialog("Enter public key", JOptionPane.PLAIN_MESSAGE,
                                JOptionPane.OK_CANCEL_OPTION, "", 500, 180);
                    }
                }
        );

        button.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        panel2.add(button);
        tabbedPane.addTab("Tab two", null, panel2, "panel two");
        */

        return tabbedPane;
    }

    private JMenu getMenu() {
        JMenu menu = new JMenu("Option");
        menu.setFont(new Font("Monospaced", Font.BOLD, 14));

        menu.add(getRegisterMenuItem());
        menu.add(getLoginMenuIten());

        return menu;
    }

    private JMenuItem getRegisterMenuItem() {
        JMenuItem registerItem = new JMenuItem("Register");
        registerItem.setFont(new Font("Monospaced", Font.BOLD, 12));
        registerItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null, "wait");
                    }
                }
        );

        return registerItem;
    }

    private JMenuItem getLoginMenuIten() {
        JMenuItem loginItem = new JMenuItem("Login");
        loginItem.setFont(new Font("Monospaced", Font.BOLD, 12));
        loginItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        InputDialog.showInputDialog("Enter public key", JOptionPane.PLAIN_MESSAGE,
                                JOptionPane.OK_CANCEL_OPTION, "", 500, 180);
                    }
                }
        );

        return loginItem;
    }

    public JMenuBar getMainMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(getMenu());

        return menuBar;
    }


    public MainView() {
        /*
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu tableMenu = new JMenu("管理");
        menuBar.add(tableMenu);
        JMenuItem hideColumnsItem = new JMenuItem("隐藏选中列");
        tableMenu.add(hideColumnsItem);

        JTabbedPane tabbedPane = new JTabbedPane();

        JLabel label1 = new JLabel("panel one", SwingConstants.CENTER);
        JPanel panel1 = new JPanel();
        panel1.add(label1);
        tabbedPane.addTab("Tab one", null, panel1, "First Panl");

        JPanel panel2 = new JPanel();
        JButton button = new JButton("button");
        button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        InputDialog.showInputDialog("Enter public key", JOptionPane.PLAIN_MESSAGE,
                                JOptionPane.OK_CANCEL_OPTION, "", 500, 180);
                    }
                }
        );

        button.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        panel2.add(button);
        tabbedPane.addTab("Tab two", null, panel2, "panel two");

        add(tabbedPane);
        */
    }

    public static void main(String[] args) {
        /*
        MainView frame = new MainView();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);
        frame.setVisible(true);
        */

        new MainView().init();
    }
}
