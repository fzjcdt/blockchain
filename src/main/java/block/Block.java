package block;

import util.Sha256Util;

import java.util.Date;

public class Block {
    private String hash;
    private String prevHash;
    private String data;
    private long timeStamp;
    private long nonce = 0;

    public Block(String prevHash, String data) {
        setPrevHash(prevHash);
        setData(data);
        setTimeStamp();
        setHash();
    }

    private String generateHash() {
        String hash = Sha256Util.sha256Encryption(
                prevHash +
                        Long.toString(timeStamp) +
                        Long.toString(nonce) +
                        data
        );

        return hash;
    }

    public void mineBlock(int difficulty) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < difficulty; i++) {
            sb.append('0');
        }

        String target = sb.toString();
        while (!hash.substring(0, difficulty).equals(target)) {
            this.nonce++;
            setHash();
        }
    }

    public String getHash() {
        return hash;
    }

    private void setHash() {
        this.hash = generateHash();
    }

    public String getPrevHash() {
        return prevHash;
    }

    private void setPrevHash(String prevHash) {
        this.prevHash = prevHash;
    }

    public String getData() {
        return data;
    }

    private void setData(String data) {
        this.data = data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    private void setTimeStamp() {
        this.timeStamp = new Date().getTime();
    }
}
