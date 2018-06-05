package test;

import block.BlockChain;
import transaction.TransactionUtil;
import wallet.Wallet;

public class Test {

    public static void main(String[] args) {
        BlockChain blockChain = BlockChain.newBlockChain();

        Wallet w1 = new Wallet();
        Wallet w2 = new Wallet();

        System.out.println(w1.getPublicKey());
        System.out.println(w2.getPublicKey());

        // w2挖矿获得100金币
        BlockChain.addBlock(w2.getPublicKey());
        System.out.println("w1 " + TransactionUtil.getBalance(w1.getPublicKey()));
        System.out.println("w2 " + TransactionUtil.getBalance(w2.getPublicKey()));
        System.out.println();

        // w2送w1 30金币, w1挖矿获得100, 总共w1 130, w2 70
        TransactionUtil.sendFunds(w2.getPublicKey(), w2.getPrivateKey(), w1.getPublicKey(), 30, "w2 give w1 30");
        BlockChain.addBlock(w1.getPublicKey());
        System.out.println("w1 " + TransactionUtil.getBalance(w1.getPublicKey()));
        System.out.println("w2 " + TransactionUtil.getBalance(w2.getPublicKey()));
        System.out.println();

        // w2送w1 30金币, 总共w1 160, w2 140
        TransactionUtil.sendFunds(w2.getPublicKey(), w2.getPrivateKey(), w1.getPublicKey(), 30, "w2 give w1 30");
        BlockChain.addBlock(w2.getPublicKey());
        System.out.println("w1 " + TransactionUtil.getBalance(w1.getPublicKey()));
        System.out.println("w2 " + TransactionUtil.getBalance(w2.getPublicKey()));
        System.out.println();

        // w1 220, w2 180
        TransactionUtil.sendFunds(w2.getPublicKey(), w2.getPrivateKey(), w1.getPublicKey(), 30, "w2 give w1 30");
        TransactionUtil.sendFunds(w2.getPublicKey(), w2.getPrivateKey(), w1.getPublicKey(), 30, "w2 give w1 30");
        BlockChain.addBlock(w2.getPublicKey());
        System.out.println("w1 " + TransactionUtil.getBalance(w1.getPublicKey()));
        System.out.println("w2 " + TransactionUtil.getBalance(w2.getPublicKey()));
        System.out.println();
    }
}
