package transaction;

import util.Sha256Util;

public class TXOutput {
    private String id;
    private String receiver;
    private double value;
    private String parentTXId;

    public TXOutput() {
    }
    public TXOutput(String receiver, double value, String parentTXId) {
        setReceiver(receiver);
        setValue(value);
        setParentTXId(parentTXId);
        setId(generateId());
    }

    public boolean isReceiver(String r) {
        return receiver.equals(r);
    }

    public String getId() {
        return id;
    }

    private String generateId() {
        return Sha256Util.sha256Encryption(receiver + Double.toString(value) + parentTXId);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getParentTXId() {
        return parentTXId;
    }

    public void setParentTXId(String parentTXId) {
        this.parentTXId = parentTXId;
    }
}
