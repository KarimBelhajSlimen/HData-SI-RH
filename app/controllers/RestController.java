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


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@With(CorsAction.class)
public class RestController extends Controller {

    JsonNode jsonRequest(){
        JsonNode json = request().body().asJson();
        if(json != null){
            return json;
        }
        else return Json.parse(request().body().asText());
    }

    Credentials credentials() throws ParseException, WrongJWTException {
        String jwt = request().getHeader("jwt");
        Auth auth = new Auth();
        return auth.parseJWT(jwt).getCredentials();
    }
}
