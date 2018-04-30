package block;

import util.Sha256Util;

import java.util.Date;

public class Block {
    private String hash;
    private String prevHash;
    private String data;
    private long timeStamp;

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
                        data
        );

        return hash;
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
