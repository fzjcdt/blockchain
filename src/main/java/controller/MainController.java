package controller;

import block.Block;
import block.BlockChain;
import view.MainView;

public class MainController {
    public static void updataBlockChain() {
        if (BlockChain.blockChain != null) {
            BlockChain.updataBlockChainFromOtherNodes();
        }
    }

    public static void notifyUpdateBlockChain() {
        if (MainView.mainFrame != null) {
            MainView.updateBlockChain();
        }
    }

    public static void notifyAddBlock(Block block) {
        if (MainView.mainFrame != null) {
            MainView.addTableRow(block);
        }
    }

    public static void mining(String data) {
        if (BlockChain.blockChain != null) {
            BlockChain.addBlock(data);
        }
    }
}
