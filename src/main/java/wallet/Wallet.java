package wallet;

import util.StringUtil;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Wallet() {
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

    public String getPublicKey() {
        return StringUtil.key2String(this.publicKey);
    }

    public String getPrivateKey() {
        return StringUtil.key2String(this.privateKey);
    }

    public static void main(String[] args) {
        Wallet w = new Wallet();
        System.out.println(w.privateKey);
        System.out.println(w.getPrivateKey());
        System.out.println(w.publicKey);
        System.out.println(w.getPublicKey());
    }
}
