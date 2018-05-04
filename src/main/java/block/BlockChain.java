package block;

import network.P2P;
import util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public class BlockChain {
    public static List<Block> blockChain;

    private BlockChain() {
        blockChain = new ArrayList<Block>();
        blockChain.add(Block.genesisBlock());
        updataBlockChainFromOtherNodes();
    }

    public static BlockChain newBlockChain() {
        return new BlockChain();
    }

    public static void receiveBlockChainHandle(List<Block> bc) {
        if (blockChain == null || bc.size() >= blockChain.size()) {
            blockChain = bc;
        }
    }

    public static void updataBlockChainFromOtherNodes() {
        // need to improve
        P2P.getInstance().dispatchToALL("give me the blockchain");
    }

    public void addBlock(String data) {
        String prevHash = blockChain.get(blockChain.size() - 1).getHash();
        Block newBlock = Block.generateNewBlock(prevHash, data);
        blockChain.add(newBlock);
        P2P.getInstance().dispatchToALL(newBlock);
    }

    public static void addBlock(Block block) {
        if (block.getHash().equals(blockChain.get(blockChain.size() - 1).getHash())) {
            return;
        }
        blockChain.add(block);
    }

    public void printBlock() {
        for (Block block : blockChain) {
            System.out.println(JsonUtil.toJson(block));
        }
    }
}
