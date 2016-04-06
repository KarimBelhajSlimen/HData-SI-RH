package security;

import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.CompletionStage;

/**
 * Created by root on 23/03/16.
 */
public class SecureAction extends play.mvc.Action<Secure>{

    /**
     * Checks JWT signature and user rights
     */
    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        //redirect if it's not secure
        String JWT = ctx.request().getHeader("jwt");
        Auth auth = new Auth();
        String[] roles = configuration.value();
        JWT jwt;
        try {
            jwt = auth.parseJWT(JWT);
        } catch (ParseException e) {
            return F.Promise.pure( unauthorized("parse_exception") );
        } catch (WrongJWTException e) {
            return F.Promise.pure( unauthorized("wrong_jwt") );
        } catch (NullPointerException e) {
            return F.Promise.pure( unauthorized("unauthentified") );
        }

        Date now = new Date();
        if (now.after( jwt.getExpirationDate() ) ) {
            return F.Promise.pure( unauthorized("expired_token") );
        }

        for(String s : roles){
            if ( jwt.getCredentials().getRoles().contains(s) ) return delegate.call(ctx);
        }
        return F.Promise.pure( unauthorized("unauthorized_access") );
    }
}