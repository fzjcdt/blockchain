package pow;

import block.Block;
import log.LogUtil;
import util.Sha256Util;

import java.util.logging.Level;

public class ProofOfWork {
    private static int difficulty = 5;
    private String target;
    private long nonce;
    private Block block;

    private ProofOfWork(Block block) {
        this.block = block;
        this.target = generateTarget();
        this.nonce = block.getNonce();
    }

    public static ProofOfWork getProofOfWork(Block block) {
        LogUtil.Log(Level.INFO, "Generate a new POW");
        return new ProofOfWork(block);
    }

    public static String generateTarget() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < difficulty; i++) {
            sb.append('0');
        }

        return sb.toString();
    }

    private String generateHash() {
        String hash = Sha256Util.sha256Encryption(
                block.getPrevHash() +
                        Long.toString(block.getTimeStamp()) +
                        block.getData() +
                        Long.toString(nonce)
        );

        return hash;
    }

    private boolean isValidRst(String hash) {
        return hash.substring(0, difficulty).equals(this.target);
    }

    public ValidRst mining() {
        String hash = generateHash();
        LogUtil.Log(Level.INFO, "Mining...");
        while (!isValidRst(hash) && nonce < Long.MAX_VALUE) {
            nonce++;
            hash = generateHash();
        }

        LogUtil.Log(Level.INFO, "Found a valid hash");
        return new ValidRst(hash, nonce);
    }
}
