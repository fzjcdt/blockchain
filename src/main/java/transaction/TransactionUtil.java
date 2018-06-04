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
            if (UTXO.isReceiver(sender)) {
                total += UTXO.getValue();
            }
        }

        return total;
    }

    public static boolean sendFunds(String sender, String priKey, String receiver,
                                    double value) {
        return sendFunds(sender, priKey, receiver, value, "");
    }

    public static boolean sendFunds(String sender, String priKey, String receiver,
                                    double value, String data) {
        return sendFunds(sender, priKey, receiver, value, data, false);
    }

    public static boolean sendFunds(String sender, String priKey, String receiver,
                                    double value, String data, boolean dataFlag) {
        if (getBalance(sender) < value) {
            return false;
        }

        List<TXInput> inputs = new ArrayList<TXInput>();
        double total = 0;
        Iterator<Map.Entry<String, TXOutput>> it = UTXOSet.UTXOs.entrySet().iterator();
        TXOutput UTXO;

        while (it.hasNext()) {
            UTXO = it.next().getValue();
            if (UTXO.isReceiver(sender)) {
                total += UTXO.getValue();
                inputs.add(new TXInput(UTXO.getId()));
            }

            if (total >= value) {
                break;
            }
        }

        Transaction transaction = new Transaction(sender, receiver, value, inputs, data, dataFlag);
        transaction.generateSignature(priKey);

        return BlockTransactions.addTransaction(transaction, true);
    }
}
