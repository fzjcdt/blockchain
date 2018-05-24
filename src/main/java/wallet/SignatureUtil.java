package wallet;

import util.KeyUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class SignatureUtil {

    public static byte[] applySignature(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] output = new byte[0];
        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    public static byte[] applySignature(String privateKey, String input) throws Exception {
        return applySignature(KeyUtil.getPrivateKey(privateKey), input);
    }

    public static boolean verifySignature(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifySignature(String publicKey, String data, byte[] signature) throws Exception {
        return verifySignature(KeyUtil.getPublicKey(publicKey), data, signature);
    }

    public static void main(String[] args) {
    }
}
