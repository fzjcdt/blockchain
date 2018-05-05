package block;

import network.P2P;
import pow.ProofOfWork;
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
        if (bc.size() >= blockChain.size() && isValidBlockChain(bc)) {
            blockChain = bc;
        }
    }

    private static Block getLastBlock() {
        return blockChain.get(blockChain.size() - 1);
    }

    private static boolean isValidBlock(Block block) {
        return isValidBlock(ProofOfWork.generateTarget(), getLastBlock().getHash(), block);
    }

    private static boolean isValidBlock(String target, String prevHash, Block block) {
        // 自己的哈希值计算正确
        if (!block.verifyHash()) {
            return false;
        }

        // prevHash正确
        if (!prevHash.equals(block.getPrevHash())) {
            return false;
        }

        // hash值符合挖矿条件
        String subHash = block.getHash().substring(0, target.length());
        if (!subHash.equals(target)) {
            return false;
        }

        return true;
    }

    private static boolean isValidBlockChain(List<Block> blockChain) {
        if (blockChain == null || blockChain.size() == 0) {
            return false;
        }

        Block curBlock;
        String preBlockHash = blockChain.get(0).getPrevHash();
        String target = ProofOfWork.generateTarget();

        for (int i = 0, len = blockChain.size(); i < len; i++) {
            curBlock = blockChain.get(i);
            if (!isValidBlock(target, preBlockHash, curBlock)) {
                return false;
            }
            preBlockHash = curBlock.getHash();
        }

        return true;
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
        if (isValidBlock(block)) {
            blockChain.add(block);
        }
    }

    public void printBlock() {
        for (Block block : blockChain) {
            System.out.println(JsonUtil.toJson(block));
        }
    }

    public static void main(String[] args) {
        BlockChain blockChain = BlockChain.newBlockChain();
        blockChain.addBlock("first");
        blockChain.addBlock("second");
        blockChain.printBlock();
        System.out.println(isValidBlockChain(BlockChain.blockChain));
    }
}
