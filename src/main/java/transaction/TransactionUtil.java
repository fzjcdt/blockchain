package transaction;

import block.BlockTransactions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TransactionUtil {

    public static double getBalance(String sender) {
        double total = 0.0;
        Iterator<Map.Entry<String, TXOutput>> it = UTXOSet.UTXOs.entrySet().iterator();
        TXOutput UTXO;

        while (it.hasNext()) {
            UTXO = it.next().getValue();
            if (UTXO.isReceiver(sender) && !BlockTransactions.inTransaction(UTXO.getId())) {
                total += UTXO.getValue();
            }
        }

        return total;
    }

    private static List<TXInput> getInput(String sender, double value) {
        List<TXInput> inputs = new ArrayList<TXInput>();
        double total = 0;
        Iterator<Map.Entry<String, TXOutput>> it = UTXOSet.UTXOs.entrySet().iterator();
        TXOutput UTXO;

        while (it.hasNext()) {
            UTXO = it.next().getValue();
            if (UTXO.isReceiver(sender) && !BlockTransactions.inTransaction(UTXO.getId())) {
                total += UTXO.getValue();
                // input直接加上UTXO, 不用在process里添加了
                inputs.add(new TXInput(UTXO.getId(), UTXO));
            }

            // 查找到发送者有足够多金币
            if (total >= value) {
                break;
            }
        }

        return inputs;
    }

    public static double getInputsValue(List<TXInput> inputs) {
        double total = 0.0;
        for (TXInput input : inputs) {
            total += input.getUTXO().getValue();
        }

        return total;
    }

    public static List<TXOutput> getOutput(String sender, String receiver,
                                           String transactionId, double total, double value) {
        List<TXOutput> outputs = new ArrayList<TXOutput>();
        double leftOver = total - value;

        outputs.add(new TXOutput(receiver, value, transactionId));
        // 多的钱发送回发送者
        if (leftOver > 0) {
            outputs.add(new TXOutput(sender, leftOver, transactionId));
        }

        return outputs;
    }

    public static Transaction generateTransaction(String sender, String priKey, String receiver,
                                                  double value, String data, boolean dataFlag) {
        // 发送者有足够金币
        if (getBalance(sender) < value) {
            return null;
        }

        // 生成交易，并签名
        Transaction transaction = new Transaction(sender, receiver, value, data, dataFlag);
        transaction.generateSignature(priKey);

        // 填入input和output
        List<TXInput> inputs = getInput(sender, value);
        List<TXOutput> outputs = getOutput(sender, receiver,
                transaction.getTransactionId(), getInputsValue(inputs), value);

        transaction.setInputs(inputs);
        transaction.setOutputs(outputs);

        return transaction;
    }

    public static synchronized boolean sendFunds(String sender, String priKey, String receiver,
                                                 double value) {
        return sendFunds(sender, priKey, receiver, value, "");
    }

    public static synchronized boolean sendFunds(String sender, String priKey, String receiver,
                                                 double value, String data) {
        return sendFunds(sender, priKey, receiver, value, data, false);
    }

    public static synchronized boolean sendFunds(String sender, String priKey, String receiver,
                                                 double value, String data, boolean dataFlag) {

        Transaction transaction = generateTransaction(sender,
                priKey, receiver, value, data, dataFlag);

        return transaction != null && BlockTransactions.addTransaction(transaction, true);
    }

    public static boolean isValidTransaction(Transaction transaction) {
        try {
            if (!transaction.verifySignature()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        if (getInputsValue(transaction.inputs) < transaction.getValue()) {
            return false;
        }

        for (TXInput inputs : transaction.inputs) {
            if (!UTXOSet.inUTXOs(inputs.getOutputId())) {
                return false;
            }
        }

        return true;
    }
}
