package transaction;

import java.util.HashMap;
import java.util.Map;

public class UTXOSet {
    public static volatile Map<String, TXOutput> UTXOs = new HashMap<String, TXOutput>();

    public static synchronized void update(Transaction transaction) {
        for (TXOutput output : transaction.outputs) {
            UTXOSet.UTXOs.put(output.getId(), output);
        }

        for (TXInput input : transaction.inputs) {
            if (input.getUTXO() == null) {
                continue;
            }

            UTXOSet.UTXOs.remove(input.getUTXO().getId());
        }
    }
}
