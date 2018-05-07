package controller;

import block.BlockChain;

public class MainController {
    public static void updataBlockChain() {
        BlockChain.updataBlockChainFromOtherNodes();
    }
}
