package block;

import network.P2P;
import transaction.TXInput;
import transaction.Transaction;
import transaction.TransactionUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockTransactions {

    public static volatile List<Transaction> transactions = new ArrayList<Transaction>();
    public static volatile Set<String> lockUTXOs = new HashSet<String>();
    public static volatile Set<String> historyTransaction = new HashSet<String>();

    public static synchronized boolean addTransaction(Transaction transaction) {
        // 默认不广播出去
        return addTransaction(transaction, false);
    }

    public static synchronized boolean addTransaction(Transaction transaction, boolean dispatch) {
        if (transaction == null) {
            return false;
        }

        // 合法的交易，并且不是自己重复发过来的
        if (TransactionUtil.isValidTransaction(transaction)
                && !historyTransaction.contains(transaction.getTransactionId())) {

            // 先把UTXOset里的交易锁住，防止多节点同时交易，导致的数据不同步问题
            for (TXInput input : transaction.inputs) {
                lockUTXOs.add(input.getOutputId());
            }
            // 加入历史id，防止挖矿时，清空了lockUTXOs，但没有在UTXOset处理时，可能出现了重复使用情况
            // 实际上不用清空lockUTXOs就能避免这个情况，
            // 但可能会导致在当前节点网络极差，几乎失去连接的情况下，锁住账户的问题
            historyTransaction.add(transaction.getTransactionId());
            transactions.add(transaction);
            if (dispatch) {
                P2P.getInstance().dispatchToALL(transaction);
            }

            return true;
        } else {
            return false;
        }
    }

    public static synchronized List<Transaction> getAndClearTransactions() {
        List<Transaction> rst = transactions;
        transactions = new ArrayList<Transaction>();
        lockUTXOs.clear();
        return rst;
    }

    public static synchronized void clear() {
        transactions.clear();
    }

    public static boolean inTransaction(String UTXOId) {
        return lockUTXOs.contains(UTXOId);
    }
}
