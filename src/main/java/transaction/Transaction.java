package transaction;

import util.Sha256Util;
import wallet.SignatureUtil;
import wallet.Wallet;

import java.util.ArrayList;
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

    public Transaction(String sender, String receiver, double value, List<TXInput> inputs) {
        this(sender, receiver, value, inputs, "", false);
    }

    public Transaction(String sender, String receiver, double value, List<TXInput> inputs, String data) {
        this(sender, receiver, value, inputs, data, false);
    }

    public Transaction(String sender, String receiver, double value, List<TXInput> inputs, String data, boolean dataFlag) {
        setSender(sender);
        setReceiver(receiver);
        setValue(value);
        setData(data);
        setDataFlag(dataFlag);
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

        TXOutput output;
        for (TXInput input : inputs) {
            output = UTXOSet.UTXOs.get(input.getOutputId());
            if (output != null) {
                input.setUTXO(output);
            } else {
                return false;
            }
        }

        double leftOver = getInputsValue() - value;
        setTransactionId();
        outputs.add(new TXOutput(this.receiver, value, transactionId));
        if (leftOver > 0) {
            outputs.add(new TXOutput(this.sender, leftOver, transactionId));
        }

        UTXOSet.update(this);

        return true;
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

    public static void main(String[] args) {
        Wallet w1 = new Wallet();
        Wallet w2 = new Wallet();

        // 假设w2有100金币
        Transaction initTransaction = new Transaction(w1.getPublicKey(), w2.getPublicKey(), 100, null);
        initTransaction.generateSignature(w1.getPrivateKey());
        initTransaction.setTransactionId("0");
        initTransaction.outputs.add(new TXOutput(initTransaction.getReceiver(), initTransaction.getValue(), initTransaction.getTransactionId()));
        UTXOSet.UTXOs.put(initTransaction.outputs.get(0).getId(), initTransaction.outputs.get(0));

        System.out.println(TransactionUtil.getBalance(w1.getPublicKey()));
        System.out.println(TransactionUtil.getBalance(w2.getPublicKey()));

        TransactionUtil.sendFunds(w2.getPublicKey(), w2.getPrivateKey(), w1.getPublicKey(), 30);
        System.out.println(TransactionUtil.getBalance(w1.getPublicKey()));
        System.out.println(TransactionUtil.getBalance(w2.getPublicKey()));
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
}
