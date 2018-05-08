package controller;

import block.Block;
import block.BlockChain;
import log.LogUtil;
import view.MainView;

import java.util.logging.Level;

public class MainController {
    public static void updataBlockChain() {
        if (BlockChain.blockChain != null) {
            BlockChain.updataBlockChainFromOtherNodes();
            LogUtil.Log(Level.INFO, "Controller: download blockchain");
        }
    }

    public static void notifyUpdateBlockChain() {
        if (MainView.mainFrame != null) {
            MainView.updateBlockChain();
            LogUtil.Log(Level.INFO, "Controller: notify update blockchain");
        }
    }

    public static void notifyAddBlock(Block block) {
        if (MainView.mainFrame != null) {
            MainView.addTableRow(block);
            LogUtil.Log(Level.INFO, "Controller: notify add block");
        }
    }

    public static void mining(String data) {
        if (BlockChain.blockChain != null) {
            BlockChain.addBlock(data);
            LogUtil.Log(Level.INFO, "Controller: mining");
        }
    }
}
