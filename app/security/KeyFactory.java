package security;

import play.Play;
import utils.PlayUtil;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created by root on 23/03/16.
 */
public class KeyFactory {

    //reads Private Key
    public RSAPrivateKey getPrivateKey(){
        java.security.KeyFactory fact = null;
        try {
            fact = java.security.KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        PlayUtil playUtil = new PlayUtil();
        BigInteger m = new BigInteger(playUtil.getProperty("keys.private.modulus"));
        BigInteger exp = new BigInteger(playUtil.getProperty("keys.private.exponent"));
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, exp);
        RSAPrivateKey privateKey = null;
        try {
            privateKey = (RSAPrivateKey) fact.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e1) {
            e1.printStackTrace();
        }
        return privateKey;
    }

    //reads Public Key
    public RSAPublicKey getPublicKey(){
        java.security.KeyFactory fact = null;
        try {
            fact = java.security.KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        PlayUtil playUtil = new PlayUtil();
        BigInteger m = new BigInteger(playUtil.getProperty("keys.public.modulus"));
        BigInteger exp = new BigInteger(playUtil.getProperty("keys.public.exponent"));
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, exp);
        RSAPublicKey publicKey = null;
        try {
            publicKey = (RSAPublicKey) fact.generatePublic(keySpec);
        } catch (InvalidKeySpecException e1) {
            e1.printStackTrace();
        }
        return publicKey;
    }
}
