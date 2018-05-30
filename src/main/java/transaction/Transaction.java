package transaction;

import util.Sha256Util;
import wallet.SignatureUtil;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private String transactionId;
    private String sender;
    private String receiver;
    private double value;
    private byte[] signature;

    public List<TXInput> inputs;
    public List<TXOutput> outputs;

    public Transaction(String sender, String receiver, double value, ArrayList<TXInput> inputs) {
        setSender(sender);
        setReceiver(receiver);
        setValue(value);
        this.inputs = inputs;
        outputs = new ArrayList<TXOutput>();
    }

    public boolean processTransaction() {
        try {
            if (!verifySignature()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        for (TXInput input : inputs) {
            input.setUTXO(UTXOSet.UTXOs.get(input.getOutputId()));
        }

        double leftOver = getInputsValue() - value;
        transactionId = calulateHash();
        outputs.add(new TXOutput(this.receiver, value, transactionId));
        if (leftOver > 0) {
            outputs.add(new TXOutput(this.sender, leftOver, transactionId));
        }

        for (TXOutput output : outputs) {
            UTXOSet.UTXOs.put(output.getId(), output);
        }

        for (TXInput input : inputs) {
            if (input.getUTXO() == null) {
                continue;
            }

            UTXOSet.UTXOs.remove(input.getUTXO().getId());
        }

        return true;
    }

    private String calulateHash() {
        return Sha256Util.sha256Encryption(sender + receiver + Double.toString(value));
    }

    public void generateSignature(String privateKey) {
        try {
            this.signature = SignatureUtil.applySignature(privateKey,
                    sender + receiver + Double.toString(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean verifySignature() throws Exception {
        String data = sender + receiver + Double.toString(value);
        return SignatureUtil.verifySignature(sender, data, signature);
    }

    public double getInputsValue() {
        double total = 0.0;
        for (TXInput input : inputs) {
            total += input.getUTXO().getValue();
        }

        return total;
    }

    public double getOutputValue() {
        double total = 0.0;
        for (TXOutput output : outputs) {
            total += output.getValue();
        }

        return total;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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
}
