package block;

import controller.MainController;
import log.LogUtil;
import network.P2P;
import pow.ProofOfWork;
import transaction.UTXOSet;
import util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class BlockChain {
    public static List<Block> blockChain;

    private BlockChain() {
        LogUtil.Log(Level.INFO, "Init blockchain");
        blockChain = new ArrayList<Block>();
        blockChain.add(Block.genesisBlock());
        //updataBlockChainFromOtherNodes();
    }

    public static BlockChain newBlockChain() {
        return new BlockChain();
    }

    public static synchronized void receiveBlockChainHandle(List<Block> bc) {
        if (bc.size() > blockChain.size() && isValidBlockChain(bc)) {
            LogUtil.Log(Level.INFO, "Find a valid and longer blockchain from other node");
            blockChain = bc;
            BlockTransactions.clear();
            UTXOSet.clear();
            for (Block block : blockChain) {
                block.processTransaction();
            }
            LogUtil.Log(Level.INFO, "Updated to a valid and longer chain");
            MainController.notifyUpdateBlockChain();
        } else {
            LogUtil.Log(Level.INFO, "Invalid blockchain");
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
        LogUtil.Log(Level.INFO, "Send download blockchain request");
        P2P.getInstance().dispatchToALL("give me the blockchain");
    }

    public static void addBlock(String publicKey) {
        String prevHash = blockChain.get(blockChain.size() - 1).getHash();
        Block newBlock = Block.generateNewBlock(prevHash, BlockTransactions.getAndClearTransactions(), publicKey);
        blockChain.add(newBlock);
        LogUtil.Log(Level.INFO, "Found a new block\n" + JsonUtil.toJson(newBlock));
        MainController.notifyAddBlock(newBlock);
        LogUtil.Log(Level.INFO, "Broadcast new block to other nodes");
        P2P.getInstance().dispatchToALL(newBlock);
    }

    public static void addBlock(Block block) {
        if (isValidBlock(block)) {
            blockChain.add(block);
            block.processTransaction();
            BlockTransactions.clear();
            MainController.notifyAddBlock(block);
            LogUtil.Log(Level.INFO, "Add a legal block discovered by other node\n"
                    + JsonUtil.toJson(block));
        }
    }

    public void printBlock() {
        for (Block block : blockChain) {
            System.out.println(JsonUtil.toJson(block));
        }
    }
}
