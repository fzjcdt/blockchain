package block;

public class BlockChain {
    public static void main(String[] args) {
        Block b1 = new Block("", "first block");
        Block b2 = new Block(b1.getHash(), "second block");
        Block b3 = new Block(b2.getHash(), "first block");

        System.out.println(b1.getHash());
        System.out.println(b2.getHash());
        System.out.println(b3.getHash());
    }
}
