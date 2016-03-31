package controllers;

import cors.CorsAction;
import model.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import security.Secure;

/**
 * Created by root on 23/03/16.
 */

@Secure({"user"})
public class UserController extends RestController {

    public Result addProfile() {
        System.out.println(Json.prettyPrint(jsonRequest()) );
        User u = Json.fromJson(jsonRequest(),User.class);
        return ok( u.toString() );
    }
}
