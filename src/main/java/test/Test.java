package test;

import block.BlockChain;
import block.BlockTransactions;
import transaction.TXOutput;
import transaction.Transaction;
import transaction.TransactionUtil;
import transaction.UTXOSet;
import wallet.Wallet;

public class Test {

    public static void main(String[] args) {
        BlockChain blockChain = BlockChain.newBlockChain();

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

        Transaction transaction1 = TransactionUtil.sendFunds(w2.getPublicKey(), w2.getPrivateKey(), w1.getPublicKey(), 300);
        BlockTransactions.addTransaction(transaction1);
        System.out.println(TransactionUtil.getBalance(w1.getPublicKey()));
        System.out.println(TransactionUtil.getBalance(w2.getPublicKey()));
        BlockChain.addBlock();
        System.out.println(TransactionUtil.getBalance(w1.getPublicKey()));
        System.out.println(TransactionUtil.getBalance(w2.getPublicKey()));
    }
}
