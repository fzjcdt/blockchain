package block;

import store.RocksDBUtil;
import util.JsonUtil;

public class BlockChain {
    public static int difficulty = 4;
    private static String lastBlockHash = "";

    public void addBlock(String data) throws Exception {
        addBlock(new Block(lastBlockHash, data));
    }

    public void addBlock(Block block) throws Exception {
        block.mineBlock(difficulty);
        RocksDBUtil.getInstance().putLastBlockHash(block.getHash());
        RocksDBUtil.getInstance().putBlock(block);
        lastBlockHash = block.getHash();
        System.out.println(JsonUtil.toJson(block));
    }

    public static void main(String[] args) {
        BlockChain blockChain = new BlockChain();
        try {
            blockChain.addBlock("first block");
            System.out.println(RocksDBUtil.getInstance().getLastBlockHash());
            blockChain.addBlock("second block");
            System.out.println(RocksDBUtil.getInstance().getLastBlockHash());
            blockChain.addBlock("third block");
            System.out.println(RocksDBUtil.getInstance().getLastBlockHash());
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
