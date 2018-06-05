package transaction;

import util.Sha256Util;
import wallet.SignatureUtil;

import java.util.List;

public class Transaction {
    private String transactionId;
    private String sender;
    private String receiver;
    private String data;
    private boolean dataFlag;
    private double value;
    private byte[] signature;

    public List<TXInput> inputs;
    public List<TXOutput> outputs;

    private static volatile long sequence = 0;

    public Transaction() {
    }

    public Transaction(String sender, String receiver, double value) {
        this(sender, receiver, value, "", false);
    }

    public Transaction(String sender, String receiver, double value, String data) {
        this(sender, receiver, value, data, false);
    }

    public Transaction(String sender, String receiver, double value, String data, boolean dataFlag) {
        setSender(sender);
        setReceiver(receiver);
        setValue(value);
        setData(data);
        setDataFlag(dataFlag);
        setTransactionId();
    }

    private String calulateHash() {
        sequence++;
        return Sha256Util.sha256Encryption(sender + receiver + Double.toString(value)
                + data + Boolean.toString(dataFlag) + Long.toString(sequence));
    }

    public void generateSignature(String privateKey) {
        try {
            this.signature = SignatureUtil.applySignature(privateKey,
                    sender + receiver + Double.toString(value)
                            + data + Boolean.toString(dataFlag));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean verifySignature() throws Exception {
        String data = sender + receiver + Double.toString(value) + this.data + Boolean.toString(dataFlag);
        return SignatureUtil.verifySignature(sender, data, signature);
    }

    public double getInputsValue() {
        double total = 0.0;
        for (TXInput input : inputs) {
            total += input.getUTXO().getValue();
        }

        return total;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setTransactionId() {
        this.transactionId = calulateHash();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isDataFlag() {
        return dataFlag;
    }

    public void setDataFlag(boolean dataFlag) {
        this.dataFlag = dataFlag;
    }

    public void setInputs(List<TXInput> inputs) {
        this.inputs = inputs;
    }

    public void setOutputs(List<TXOutput> outputs) {
        this.outputs = outputs;
    }
}
