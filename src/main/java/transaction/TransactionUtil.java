package transaction;

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

    public static Transaction sendFunds(String sender, String priKey, String receiver, double value) {
        if (getBalance(sender) < value) {
            return null;
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

        Transaction transaction = new Transaction(sender, receiver, value, inputs);
        transaction.generateSignature(priKey);

        return transaction;
    }
}
