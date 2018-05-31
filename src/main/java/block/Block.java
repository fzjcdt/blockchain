package block;

import log.LogUtil;
import pow.ProofOfWork;
import pow.ValidRst;
import transaction.Transaction;
import util.Sha256Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

public class Block {
    private String hash;
    private String prevHash;
    private String data;
    private long timeStamp;
    private long nonce = 0;
    private List<Transaction> transactions;

    public Block() {
    }

    public Block(String hash, String prevHash, String data, long timeStamp, long nonce) {
        this.hash = hash;
        this.prevHash = prevHash;
        this.data = data;
        this.timeStamp = timeStamp;
        this.nonce = nonce;
        transactions = new ArrayList<Transaction>();
    }

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null || !transaction.processTransaction()) {
            return false;
        }

        transactions.add(transaction);
        return true;
    }

    public static Block genesisBlock() {
        LogUtil.Log(Level.INFO, "Create genesis block");
        return generateNewBlock("", "genesisBlock");
    }

    public static Block generateNewBlock(String prevHash, String data) {
        Block block = new Block("", prevHash, data, new Date().getTime(), 0);
        ProofOfWork pow = ProofOfWork.getProofOfWork(block);
        ValidRst validRst = pow.mining();
        block.setHash(validRst.getHash());
        block.setNonce(validRst.getNonce());

        LogUtil.Log(Level.INFO, "Generate a new block");
        return block;
    }

    public boolean verifyHash() {
        String realHash = Sha256Util.sha256Encryption(
                this.getPrevHash() +
                        Long.toString(this.getTimeStamp()) +
                        this.getData() +
                        Long.toString(this.nonce)
        );

        return realHash.equals(this.getHash());
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
