package test;

import block.BlockChain;
import view.MainView;

public class BlockChainGUITest {
    public static void main(String[] args) {
        //LogUtil.turnOn();
        BlockChain blockChain = BlockChain.newBlockChain();
        new MainView().init();
    }
}
