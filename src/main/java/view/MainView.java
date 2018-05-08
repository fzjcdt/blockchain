package view;

import block.Block;
import block.BlockChain;
import controller.MainController;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import sun.swing.table.DefaultTableCellHeaderRenderer;
import util.JsonUtil;
import wallet.Wallet;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainView {
    public static JFrame mainFrame = null;
    static DefaultTableModel blockModel;
    static JTable blockTable;
    static JPopupMenu popupMenu;
    static int RowNum = 0;
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

        mainFrame = new JFrame("");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setJMenuBar(getMainMenuBar());
        mainFrame.add(getMainTablePane());

        mainFrame.setSize(1400, 800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    private static void setBlockModel() {
        String[] headerNames = {"prevHash", "hash", "data", "nonce", "timeStamp"};
        if (BlockChain.blockChain == null) {
            blockModel = new DefaultTableModel(headerNames, 0);
            return;
        }

        blockModel = new DefaultTableModel(headerNames, BlockChain.blockChain.size());

        for (int i = 0, len = BlockChain.blockChain.size(); i < len; i++) {
            Block block = BlockChain.blockChain.get(i);

            blockModel.setValueAt(block.getPrevHash(), i, 0);
            blockModel.setValueAt(block.getHash(), i, 1);
            blockModel.setValueAt(block.getData(), i, 2);
            blockModel.setValueAt(String.valueOf(block.getNonce()), i, 3);
            blockModel.setValueAt(String.valueOf(block.getTimeStamp()), i, 4);
        }
    }

    private static void createPopupMenu() {
        popupMenu = new JPopupMenu();
        JMenuItem menItem = new JMenuItem();
        menItem.setText("Detail");
        menItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        MyDialog.showMessageDialog(JsonUtil.toJson(BlockChain.blockChain.get(RowNum)),
                                JOptionPane.PLAIN_MESSAGE,
                                JOptionPane.OK_OPTION, "", 600, 240);
                    }
                }
        );

        popupMenu.add(menItem);
    }

    private static void mouseRightButtonClick(MouseEvent evt) {
        //判断是否为鼠标的BUTTON3按钮，BUTTON3为鼠标右键
        if (evt.getButton() == MouseEvent.BUTTON3) {
            //通过点击位置找到点击为表格中的行
            int focusedRowIndex = blockTable.rowAtPoint(evt.getPoint());
            if (focusedRowIndex == -1) {
                return;
            }

            RowNum = focusedRowIndex;
            //将表格所选项设为当前右键点击的行
            blockTable.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
            //弹出菜单
            popupMenu.show(blockTable, evt.getX(), evt.getY());
        }

    }

    private static void setBlockTable() {
        setBlockModel();
        blockTable = new JTable(blockModel);
        blockTable.setFont(new Font("Monospaced", Font.PLAIN, 20));
        blockTable.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 22));
        blockTable.setRowHeight(24);

        blockTable.getColumnModel().getColumn(0).setPreferredWidth(240);
        blockTable.getColumnModel().getColumn(1).setPreferredWidth(240);
        blockTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        blockTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        blockTable.getColumnModel().getColumn(4).setPreferredWidth(80);

        // 数据居中
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        blockTable.setDefaultRenderer(Object.class, render);

        // 表头居中
        DefaultTableCellHeaderRenderer hr = new DefaultTableCellHeaderRenderer();
        hr.setHorizontalAlignment(JLabel.CENTER);
        blockTable.getTableHeader().setDefaultRenderer(hr);

        createPopupMenu();
        blockTable.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //super.mouseClicked(e);
                        mouseRightButtonClick(e);
                    }
                }
        );
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
        tabbedPane.addTab("Mining", null, getMinePanel(), "Panel for mining");

        return tabbedPane;
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
        menu.add(getupdateMenuItem());

        return menu;
    }

    private JMenu getMiningMenu() {
        JMenu menu = new JMenu("Mine");
        menu.setFont(new Font("Monospaced", Font.BOLD, 14));

        menu.add(getMiningMenuItem());

        return menu;
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
                            mainFrame.setTitle(pubKey.toString());
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
                        JProgressBar.generateProgressBar(5, "Download blockchain...");
                    }
                }
        );

        return joinItem;
    }

    private JMenuItem getupdateMenuItem() {
        JMenuItem updateItem = new JMenuItem("Reload");
        updateItem.setFont(new Font("Monospaced", Font.BOLD, 12));
        updateItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JProgressBar.generateProgressBar(2, "Update...");
                        updateBlockChain();
                    }
                }
        );

        return updateItem;
    }

    private JMenuItem getMiningMenuItem() {
        JMenuItem miningItem = new JMenuItem("Mining");
        miningItem.setFont(new Font("Monospaced", Font.BOLD, 12));
        miningItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Object data = MyDialog.showInputDialog("Input the data:", JOptionPane.PLAIN_MESSAGE,
                                JOptionPane.OK_CANCEL_OPTION, "", 500, 180);
                        MainController.mining(data.toString());
                        JProgressBar.generateProgressBar(5, "Mining...");
                    }
                }
        );

        return miningItem;
    }

    public JMenuBar getMainMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(getLoginMenu());
        menuBar.add(getUpdateMenu());
        menuBar.add(getMiningMenu());

        return menuBar;
    }

    public static void addTableRow(Block block) {
        String[] newCells = new String[BLOCK_COLUMN];

        newCells[0] = block.getPrevHash();
        newCells[1] = block.getHash();
        newCells[2] = block.getData();
        newCells[3] = String.valueOf(block.getNonce());
        newCells[4] = String.valueOf(block.getTimeStamp());

        blockModel.addRow(newCells);
    }

    public static void updateBlockChain() {
        while (blockModel.getRowCount() != 0) {
            blockModel.removeRow(0);
        }

        if (BlockChain.blockChain != null) {
            for (Block block : BlockChain.blockChain) {
                addTableRow(block);
            }
        }
    }

    public static void main(String[] args) {
        new MainView().init();
    }
}
