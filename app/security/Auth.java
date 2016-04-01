package security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dao.UserDAO;
import model.User;
import net.minidev.json.JSONObject;
import utils.HashUtil;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Handles JWT generation and parsing
 */
public class Auth {

    /**
     *
     * @param user User data stored in the DB
     * @param password Password given bu the user
     * @return true if the password is right
     */
    public boolean authentify(User user, String password){
        HashUtil hashUtil = new HashUtil();
        return (user.getPasswordHash()).equals( hashUtil.hash(password) );
    }

    /**
     *
     * @param user User data
     * @return new JWT token containing username and roles
     */
    public String generateJWT(User user){
        KeyFactory keyFactory = new KeyFactory();
        RSAPrivateKey privateKey = keyFactory.getPrivateKey();

        // Create RSA-signer with the private key
        JWSSigner signer = new RSASSASigner(privateKey);

        // Prepare JWT with claims set
        ArrayList<String> roles = new ArrayList<String>();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("user")
                .issuer("Finaxys Academy")
                .expirationTime(new Date(new Date().getTime() + 48 * 60 * 60 * 1000))
                .claim("user", new Credentials(user.getEmail(),user.getRoles()))
                .build();
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.RS256),
                claimsSet);

        // Compute the RSA signature
        try {
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        // To serialize to compact form
        String s = signedJWT.serialize();
        return s;
    }

    /**
     *
     * @param s JWT as String
     * @return Credentials and Expiration Date
     * @throws ParseException JWT wasn't correctly parsed
     * @throws WrongJWTException Signature is wrong
     * @throws NullPointerException
     */
    public JWT parseJWT(String s) throws ParseException, WrongJWTException, NullPointerException {
        KeyFactory keyFactory = new KeyFactory();
        RSAPublicKey publicKey = keyFactory.getPublicKey();
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(s);
        } catch (ParseException e) {
            throw new WrongJWTException();
        }

        JWSVerifier verifier = new RSASSAVerifier(publicKey);
        boolean verify=false;
        try {
            verify = signedJWT.verify(verifier);
        } catch (JOSEException e) {
            throw new WrongJWTException();
        }
        if (verify == false) throw new WrongJWTException();

        JSONObject JSONUser =  (JSONObject) signedJWT.getJWTClaimsSet().getClaim("user");
        Credentials c = new Credentials( JSONUser);
        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        return new JWT(c,expirationDate);
    }

}
