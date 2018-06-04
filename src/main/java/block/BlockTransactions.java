package block;

import network.P2P;
import transaction.Transaction;
import transaction.UTXOSet;

import java.util.ArrayList;
import java.util.List;

public class BlockTransactions {

    private static volatile List<Transaction> transactions = new ArrayList<Transaction>();

    public static synchronized boolean addTransaction(Transaction transaction) {
        // 默认不广播出去
        return addTransaction(transaction, false);
    }

    public static synchronized boolean addTransaction(Transaction transaction, boolean dispatch) {
        if (transaction == null) {
            return false;
        }

        if (dispatch) {
            if (!transaction.processTransaction()) {
                return false;
            }
        } else {
            UTXOSet.update(transaction);
        }

        transactions.add(transaction);
        if (dispatch) {
            P2P.getInstance().dispatchToALL(transaction);
        }
        return true;
    }

    public static synchronized List<Transaction> getAndClearTransactions() {
        List<Transaction> temp = transactions;
        transactions = new ArrayList<Transaction>();
        return temp;
    }

    public static synchronized void clear() {
        transactions = new ArrayList<Transaction>();
    }
}
