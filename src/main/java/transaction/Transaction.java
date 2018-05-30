package transaction;

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

    public double getInputsValue() {
        double total = 0.0;

        for (TXInput input : inputs) {
            // TO DO
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
