import model.User;
import org.junit.Test;
import play.Configuration;
import security.Auth;
import security.JWT;
import security.WrongJWTException;

import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;
import static play.test.Helpers.*;
import static play.test.Helpers.HTMLUNIT;

public class SecurityTest {



    @Test
    public void validJWT() {
        User u = new User();
        u.setEmail("email@email.com");
        Auth auth = new Auth();
        String s = auth.generateJWT(u);
        //JWT can be parsed
        JWT jwt=null;
        boolean validJWT = true;
        try {
            jwt = auth.parseJWT(s);
        } catch (ParseException e) {
            validJWT = false;
        } catch (WrongJWTException e) {
            validJWT = false;
        }
        assertTrue(validJWT);
    }

    @Test
    public void expirationDate() {
        User u = new User();
        u.setEmail("email@email.com");
        Auth auth = new Auth();
        String s = auth.generateJWT(u);
        //JWT can be parsed
        JWT jwt=null;
        boolean validJWT = true;
        try {
            jwt = auth.parseJWT(s);
        } catch (ParseException e) {
            validJWT = false;
        } catch (WrongJWTException e) {
            validJWT = false;
        }
        //Detect Expiration date
        assertTrue( jwt.getExpirationDate().before(  new Date(new Date().getTime() + 48 * 60 * 60 * 1000)) );


    }

    @Test
    public void changeJWT() throws ParseException, WrongJWTException {
        User u = new User();
        u.setEmail("email@email.com");
        Auth auth = new Auth();
        String s = auth.generateJWT(u);
        JWT jwt= auth.parseJWT(s);
        //Changes in JWT are detected
        boolean detectEditedJWT = false;
        char c = s.charAt( 5  );
        s = s.replace(c,'?');
        try {
            jwt = auth.parseJWT(s);
        } catch (ParseException e) {
            detectEditedJWT = true;
        } catch (WrongJWTException e) {
            detectEditedJWT = true;
        }
        assertTrue(detectEditedJWT);
    }
}
