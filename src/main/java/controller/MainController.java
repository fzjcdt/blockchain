package controller;

import block.Block;
import block.BlockChain;
import forum.ForumView;
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

        if (ForumView.mainFrame != null) {
            ForumView.updateBlockChain();
        }
    }

    public static void notifyAddBlock(Block block) {
        if (MainView.mainFrame != null) {
            MainView.addTableRow(block);
            LogUtil.Log(Level.INFO, "Controller: notify add block");
        }

        if (ForumView.mainFrame != null) {
            ForumView.addBlock(block);
        }
    }

    public static void mining(String publicKey) {
        if (BlockChain.blockChain != null) {
            BlockChain.addBlock(publicKey);
            LogUtil.Log(Level.INFO, "Controller: mining");
        }
    }
}
