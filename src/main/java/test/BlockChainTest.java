package test;

import block.BlockChain;
import network.P2P;
import transaction.TransactionUtil;
import wallet.Wallet;

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
            System.out.println("-1: quit\n0: add block\n1: print\n2: print pubkey\n3: add transaction\n4: print balance");
            type = input.nextInt();
            if (type == -1) {
                break;
            }
            switch (type) {
                case 0:
                    blockChain.addBlock(wallet.getPublicKey());
                    break;
                case 1:
                    blockChain.printBlock();
                    break;
                case 2:
                    System.out.println(wallet.getPublicKey());
                    break;
                // new MainView().init();
                case 3:
                    TransactionUtil.sendFunds(wallet.getPublicKey(), wallet.getPrivateKey(), readData(), 10);
                    break;
                case 4:
                    System.out.println(TransactionUtil.getBalance(wallet.getPublicKey()));
            }
        }
    }
}
