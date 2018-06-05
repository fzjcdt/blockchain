package test;

import block.BlockChain;
import network.P2P;
import smartContract.BlackListRst;
import smartContract.SmartContract;
import transaction.TransactionUtil;
import wallet.Wallet;

import java.util.List;
import java.util.Scanner;

public class BlockChainTest {
    static Scanner input = new Scanner(System.in);

    public static String readData() {
        String rst = input.nextLine();
        while (rst == "\n" || rst.replace(" ", "").equals("")) {
            rst = input.nextLine();
        }

        return rst;
    }

    public static void main(String[] args) {
        //LogUtil.turnOn();
        BlockChain blockChain = BlockChain.newBlockChain();
        BlockChain.updataBlockChainFromOtherNodes();
        int type = 0;
        P2P.getInstance();
        Wallet wallet = new Wallet();

        while (true) {
            System.out.println("" +
                    "-1: quit\n" +
                    "0: add block\n" +
                    "1: print\n" +
                    "2: print pubkey\n3: add transaction\n4: print balance\n" +
                    "5: print balance with public key\n" +
                    "6: print black list");
            type = input.nextInt();
            if (type == -1) {
                break;
            }
            switch (type) {
                case 0:
                    blockChain.addBlock(wallet.getPublicKey());
                    break;
                case 1:
                    // blockChain.printBlock();
                    System.out.println(BlockChain.blockChain.size());
                    break;
                case 2:
                    System.out.println(wallet.getPublicKey());
                    break;
                case 3:
                    TransactionUtil.sendFunds(wallet.getPublicKey(), wallet.getPrivateKey(), readData(), 10, readData());
                    break;
                case 4:
                    System.out.println(TransactionUtil.getBalance(wallet.getPublicKey()));
                    break;
                case 5:
                    System.out.println(TransactionUtil.getBalance(readData()));
                    break;
                case 6:
                    List<BlackListRst> list = SmartContract.getBlackList();
                    for (BlackListRst blackListRst : list) {
                        System.out.println(blackListRst);
                    }
                    break;
            }
        }
    }
}
