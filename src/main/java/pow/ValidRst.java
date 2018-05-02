package pow;

public class ValidRst {
    private String hash;
    private long nonce;

    public ValidRst(String hash, long nonce) {
        this.hash = hash;
        this.nonce = nonce;
    }

    public String getHash() {
        return hash;
    }

    public long getNonce() {
        return nonce;
    }
}
