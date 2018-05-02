package block;

import util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public class BlockChain {
    public List<Block> blockChain;

    private BlockChain() {
        blockChain = new ArrayList<Block>();
        blockChain.add(Block.genesisBlock());
    }

    public static BlockChain newBlockChain() {
        return new BlockChain();
    }

    public void addBlock(String data) {
        String prevHash = blockChain.get(blockChain.size() - 1).getHash();
        blockChain.add(Block.generateNewBlock(prevHash, data));
    }

    public void printBlock() {
        for (Block block : blockChain) {
            System.out.println(JsonUtil.toJson(block));
        }
    }
}
