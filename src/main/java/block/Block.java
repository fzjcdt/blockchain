package block;

import log.LogUtil;
import pow.ProofOfWork;
import pow.ValidRst;
import transaction.TXOutput;
import transaction.Transaction;
import transaction.UTXOSet;
import util.Sha256Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

public class Block {
    private String hash;
    private String prevHash;
    private String merkleRoot;
    private long timeStamp;
    private long nonce = 0;
    private List<Transaction> transactions;
    private Transaction coinbaseTransaction;

    public Block() {
    }

    public Block(String hash, String prevHash, long timeStamp, long nonce, List<Transaction> transactions) {
        this.hash = hash;
        this.prevHash = prevHash;
        this.timeStamp = timeStamp;
        this.nonce = nonce;
        this.transactions = transactions;
    }

    public static Block genesisBlock() {
        LogUtil.Log(Level.INFO, "Create genesis block");
        return generateNewBlock("", null, "");
    }

    public static Block generateNewBlock(String prevHash, List<Transaction> transactions, String publicKey) {
        Block block = new Block("", prevHash, new Date().getTime(), 0, transactions);
        block.setMerkleRoot(MerkleRootUtil.getMerkleRoot(block.transactions));

        ProofOfWork pow = ProofOfWork.getProofOfWork(block);
        ValidRst validRst = pow.mining();
        block.setHash(validRst.getHash());
        block.setNonce(validRst.getNonce());

        block.addCoinBase(publicKey);
        block.processTransaction();

        LogUtil.Log(Level.INFO, "Generate a new block");
        return block;
    }

    public void processTransaction() {
        if (this.transactions != null) {
            for (Transaction transaction : this.transactions) {
                UTXOSet.update(transaction);
            }
        }

        UTXOSet.update(this.coinbaseTransaction);
    }

    public void addCoinBase(String minerWallet) {
        Transaction coinbaseTransaction = new Transaction("coinbase", minerWallet, 100, null);
        coinbaseTransaction.setOutputs(new ArrayList<TXOutput>());
        coinbaseTransaction.outputs.add(new TXOutput(coinbaseTransaction.getReceiver(),
                coinbaseTransaction.getValue(), coinbaseTransaction.getTransactionId()));
        setCoinbaseTransaction(coinbaseTransaction);
    }

    public boolean verifyHash() {
        String realHash = Sha256Util.sha256Encryption(
                this.getPrevHash() +
                        Long.toString(this.getTimeStamp()) +
                        this.merkleRoot +
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public Transaction getCoinbaseTransaction() {
        return coinbaseTransaction;
    }

    public void setCoinbaseTransaction(Transaction coinbaseTransaction) {
        this.coinbaseTransaction = coinbaseTransaction;
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }
}
