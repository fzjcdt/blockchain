package test;

import block.BlockChain;

public class BlockChainTest {
    public static void main(String[] args) {
        BlockChain blockChain = BlockChain.newBlockChain();
        blockChain.addBlock("first block");
        blockChain.addBlock("second block");
        blockChain.addBlock("third block");

        blockChain.printBlock();
    }
}
