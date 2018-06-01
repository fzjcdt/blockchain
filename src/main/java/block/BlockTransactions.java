package block;

import transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public class BlockTransactions {

    private static volatile List<Transaction> transactions = new ArrayList<Transaction>();

    public static synchronized boolean addTransaction(Transaction transaction) {
        if (transaction == null || !transaction.processTransaction()) {
            return false;
        }

        transactions.add(transaction);
        return true;
    }

    public static synchronized List<Transaction> getAndClearTransactions() {
        List<Transaction> temp = transactions;
        transactions = new ArrayList<Transaction>();
        return temp;
    }
}
