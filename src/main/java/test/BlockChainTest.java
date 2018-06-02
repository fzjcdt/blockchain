package test;

import block.BlockChain;
import network.P2P;
import view.MainView;

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
        String data = "";
        P2P.getInstance();

        while (true) {
            System.out.println("*** quit:-1, add block:0, print:1 ***");
            type = input.nextInt();
            if (type == -1) {
                break;
            }
            switch (type) {
                case 0:
                    data = readData();
                    blockChain.addBlock("");
                    break;
                case 1:
                    blockChain.printBlock();
                    break;
                case 2:
                    new MainView().init();
            }
        }
    }
}
