package test;

import block.BlockChain;
import forum.ForumView;

public class ForumViewTest {
    public static void main(String[] args) {
        //LogUtil.turnOn();
        BlockChain blockChain = BlockChain.newBlockChain();
        new ForumView();
    }
}
