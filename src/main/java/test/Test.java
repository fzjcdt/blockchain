package test;

import block.BlockChain;
import smartContract.BlackListRst;
import smartContract.SmartContract;
import transaction.TransactionUtil;
import wallet.Wallet;

import java.util.List;

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
        TransactionUtil.sendFunds(w2.getPublicKey(), w2.getPrivateKey(), w1.getPublicKey(), 30, "13.108.111.23");
        BlockChain.addBlock(w1.getPublicKey());
        System.out.println("w1 " + TransactionUtil.getBalance(w1.getPublicKey()));
        System.out.println("w2 " + TransactionUtil.getBalance(w2.getPublicKey()));
        System.out.println();

        // w2送w1 30金币, 总共w1 160, w2 140
        TransactionUtil.sendFunds(w2.getPublicKey(), w2.getPrivateKey(), w1.getPublicKey(), 30, "13.108.111.23");
        BlockChain.addBlock(w2.getPublicKey());
        System.out.println("w1 " + TransactionUtil.getBalance(w1.getPublicKey()));
        System.out.println("w2 " + TransactionUtil.getBalance(w2.getPublicKey()));
        System.out.println();

        // w1 220, w2 180
        TransactionUtil.sendFunds(w2.getPublicKey(), w2.getPrivateKey(), w1.getPublicKey(), 30, "34.12.3.3");
        TransactionUtil.sendFunds(w2.getPublicKey(), w2.getPrivateKey(), w1.getPublicKey(), 30, "1.1.1.1");
        BlockChain.addBlock(w2.getPublicKey());
        System.out.println("w1 " + TransactionUtil.getBalance(w1.getPublicKey()));
        System.out.println("w2 " + TransactionUtil.getBalance(w2.getPublicKey()));
        System.out.println();

        List<BlackListRst> list = SmartContract.getBlackList();
        for (BlackListRst blackListRst : list) {
            System.out.println(blackListRst);
        }
    }
}
