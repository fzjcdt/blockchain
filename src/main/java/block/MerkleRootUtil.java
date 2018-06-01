package block;

import transaction.Transaction;
import util.Sha256Util;

import java.util.ArrayList;
import java.util.List;

public class MerkleRootUtil {

    public static String getMerkleRoot(List<Transaction> transactions) {
        if (transactions == null) {
            return "";
        }
        int count = transactions.size();

        List<String> previousTreeLayer = new ArrayList<String>();
        for (Transaction transaction : transactions) {
            previousTreeLayer.add(transaction.getTransactionId());
        }
        List<String> treeLayer = previousTreeLayer;

        while (count > 1) {
            treeLayer = new ArrayList<String>();
            for (int i = 1; i < previousTreeLayer.size(); i += 2) {
                treeLayer.add(Sha256Util.sha256Encryption(previousTreeLayer.get(i - 1) + previousTreeLayer.get(i)));
            }
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }

        String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
        return merkleRoot;
    }
}
