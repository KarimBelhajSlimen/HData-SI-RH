package controllers;

import cors.CorsAction;
import dao.UserDAO;
import model.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import security.Secure;

import java.io.IOException;

/**
 * Created by root on 23/03/16.
 */

@Secure({"user"})
public class UserController extends RestController {

    public Result addProfile() throws IOException {
        System.out.println(Json.prettyPrint(jsonRequest().findPath("user")) );
        User u = Json.fromJson(jsonRequest().findPath("user"),User.class);
        u.setEmail(u.getEmail());
        //System.out.println(u.toString());
        UserDAO userDAO = new UserDAO();
        userDAO.addProfile(u);
        return ok( u.toString() );
    }
}
