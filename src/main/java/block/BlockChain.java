package block;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class BlockChain {
    public static List<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 4;

    public static void main(String[] args) {
        blockchain.add(new Block("", "first block"));
        blockchain.get(blockchain.size() - 1).mineBlock(difficulty);
        blockchain.add(new Block(blockchain.get(blockchain.size() - 1).getHash(), "second block"));
        blockchain.get(blockchain.size() - 1).mineBlock(difficulty);
        blockchain.add(new Block(blockchain.get(blockchain.size() - 1).getHash(), "third block"));
        blockchain.get(blockchain.size() - 1).mineBlock(difficulty);

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJson);
    }
}
