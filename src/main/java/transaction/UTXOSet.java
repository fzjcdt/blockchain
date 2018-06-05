package transaction;

import java.util.HashMap;
import java.util.Map;

public class UTXOSet {
    public static volatile Map<String, TXOutput> UTXOs = new HashMap<String, TXOutput>();

    public static synchronized void update(Transaction transaction) {
        for (TXOutput output : transaction.outputs) {
            if (output == null) {
                continue;
            }

            UTXOSet.UTXOs.put(output.getId(), output);
        }

        if (transaction.inputs != null) {
            for (TXInput input : transaction.inputs) {
                if (input.getUTXO() == null) {
                    continue;
                }

                UTXOSet.UTXOs.remove(input.getOutputId());
            }
        }
    }

    public static boolean inUTXOs(String UTXOId) {
        return UTXOs.containsKey(UTXOId);
    }

    public static void clear() {
        UTXOs.clear();
    }
}
