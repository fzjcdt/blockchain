package block;

import pow.ProofOfWork;
import pow.ValidRst;

import java.util.Date;

public class Block {
    private String hash;
    private String prevHash;
    private String data;
    private long timeStamp;
    private long nonce = 0;

    public Block(String hash, String prevHash, String data, long timeStamp, long nonce) {
        this.hash = hash;
        this.prevHash = prevHash;
        this.data = data;
        this.timeStamp = timeStamp;
        this.nonce = nonce;
    }

    public static Block genesisBlock() {
        return generateNewBlock("", "genesisBlock");
    }

    public static Block generateNewBlock(String prevHash, String data) {
        Block block = new Block("", prevHash, data, new Date().getTime(), 0);
        ProofOfWork pow = ProofOfWork.getProofOfWork(block);
        ValidRst validRst = pow.mining();
        block.setHash(validRst.getHash());
        block.setNonce(validRst.getNonce());

        return block;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public String getData() {
        return data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }
}
