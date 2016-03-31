package security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import model.User;
import net.minidev.json.JSONObject;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by root on 23/03/16.
 */
public class Auth {

    //Check password
    public boolean authentify(User user, String password){
        return true;
    }

    //Generate JWT using user credentials
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
                .claim("user",user)
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

    //Parse JWT, throw exception when the signature is wrong
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
        /*User u = new User( (String)JSONUser.get("username"),
                (String)JSONUser.get("hash"),
                (List<String>)JSONUser.get("roles") );*/
        User u = new User();
        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        return new JWT(u,expirationDate);
    }

}
