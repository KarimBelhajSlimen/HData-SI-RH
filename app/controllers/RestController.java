package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import cors.CorsAction;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import security.Auth;
import security.Credentials;
import security.WrongJWTException;

import java.text.ParseException;


@With(CorsAction.class)
public class RestController extends Controller {

    /**
     * Returns JSON inside body of a request
     * Calls different methods epending of "Content-Type" header in the request's body
     */


    JsonNode jsonRequest(){
        JsonNode json = request().body().asJson();
        if(json != null){
            return json;
        }
        else return Json.parse(request().body().asText());
    }

    /**
     * Returns Credentials contained in JWT, contained in the "jwt" header
     */
    Credentials credentials() {
        String jwt = request().getHeader("jwt");
        Auth auth = new Auth();
        Credentials c = null;
        try {
            c = auth.parseJWT(jwt).getCredentials();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (WrongJWTException e) {
            e.printStackTrace();
        }
        return c;
    }

}
