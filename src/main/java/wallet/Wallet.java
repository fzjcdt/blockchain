package wallet;

import log.LogUtil;
import transaction.TXInput;
import transaction.TXOutput;
import transaction.Transaction;
import transaction.UTXOSet;
import util.StringUtil;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Wallet() {
        LogUtil.Log(Level.INFO, "Init a new wallet");
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static double getBalance(String sender) {
        double total = 0.0;
        Iterator<Map.Entry<String, TXOutput>> it = UTXOSet.UTXOs.entrySet().iterator();
        TXOutput UTXO;

        while (it.hasNext()) {
            UTXO = it.next().getValue();
            if (UTXO.isReceiver(sender)) {
                total += UTXO.getValue();
            }
        }

        return total;
    }

    public static Transaction sendFunds(String sender, String priKey, String receiver, double value) {
        if (getBalance(sender) < value) {
            return null;
        }

        List<TXInput> inputs = new ArrayList<TXInput>();
        double total = 0;
        Iterator<Map.Entry<String, TXOutput>> it = UTXOSet.UTXOs.entrySet().iterator();
        TXOutput UTXO;

        while (it.hasNext()) {
            UTXO = it.next().getValue();
            if (UTXO.isReceiver(sender)) {
                total += UTXO.getValue();
                inputs.add(new TXInput(UTXO.getId()));
            }

            if (total >= value) {
                break;
            }
        }

        Transaction transaction = new Transaction(sender, receiver, value, inputs);
        transaction.generateSignature(priKey);

        return transaction;
    }

    public String getPublicKey() {
        return StringUtil.key2String(this.publicKey);
    }

    public String getPrivateKey() {
        return StringUtil.key2String(this.privateKey);
    }

    public static void main(String[] args) {
        Wallet w = new Wallet();

        String s = "Hello World";
        byte[] signature = SignatureUtil.applySignature(w.privateKey, s);
        System.out.println(SignatureUtil.verifySignature(w.publicKey, s, signature));

        String pubK = w.getPublicKey();
        String priK = w.getPrivateKey();
        System.out.println(pubK);
        System.out.println(priK);

        try {
            signature = SignatureUtil.applySignature(priK, s);
            System.out.println(SignatureUtil.verifySignature(pubK, s, signature));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
