package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.org.apache.xpath.internal.SourceTree;
import cors.CorsAction;
import dao.UnknownUsername;
import dao.UserDAO;
import model.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import security.Auth;
import utils.HashUtil;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AuthController extends Controller {

    //Takes username and password and returns JWT
    @With(CorsAction.class)
    public Result login() {
        JsonNode json = request().body().asJson();
        String username = null;
        String password = null;
        try {
            username = json.findPath("username").asText();
            password = json.findPath("password").asText();
        }catch(NullPointerException e){
            return unauthorized("wrong_format");
        }
        User user = null;
        try {
            user = new UserDAO().getByUsername( username );
        } catch (UnknownUsername unknownUsername) {
            return unauthorized("unknown_username");
        }
        Auth auth = new Auth();
        if( auth.authentify(user,password) == false ) return unauthorized("wrong_credentials");
        else return ok(auth.generateJWT(user));
    }

    //Takes username and password and returns JWT
    @With(CorsAction.class)
    public Result signUp() throws NoSuchAlgorithmException, IOException {
        JsonNode json = request().body().asJson();
        String username = null;
        String password = null;
        try {
            username = json.findPath("username").asText();
            password = json.findPath("password").asText();
        }catch(NullPointerException e){
            return unauthorized("wrong_format");
        }
        User user = new User();
        user.setEmail(username);
        user.setRoles( Arrays.asList("user")  );
        user.setPasswordHash( new HashUtil().hash(password) );
        UserDAO userDAO = new UserDAO();
        userDAO.createUser(user);
        Auth auth = new Auth();
        return ok(auth.generateJWT(user));
    }

}
