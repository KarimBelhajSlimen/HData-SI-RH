package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import cors.CorsAction;
import dao.UnknownUsername;
import dao.UserAlreadyExistsException;
import dao.UserDAO;
import model.Experience;
import model.User;
import model.Education;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import security.Auth;
import security.Secure;
import utils.HashUtil;

import java.util.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


public class AuthStaffController extends RestController {

    /**
     * Takes credentials and returns JWT when successfull
     */


    public Result loginStaff() throws IOException {
        JsonNode json = jsonRequest();
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

        System.out.println("membre staff **** "+user.getEmail()+"  role: "+user.getRoles().get(0));
        Auth auth = new Auth();
        List<String> roles=new ArrayList<String>();
        roles.add("admin");
        roles.add("RH");
        roles.add("consultant");
        if( auth.authentify(user,password) == false ) return unauthorized("wrong_credentials");
        else if( auth.verifyRoles(user,roles) == false ) return unauthorized("unauthorized");
        else return ok(auth.generateJWT(user));
    }

    public Result addMemberStaff() throws IOException {
        JsonNode json = jsonRequest();
        String username = null;
        String password = null;
        String role = null;
        try {
            username = json.findPath("username").asText();
            password = json.findPath("password").asText();
            role = json.findPath("role").asText();
        }catch(NullPointerException e){
            return unauthorized("wrong_format");
        }
        User user = new User();
        user.setEmail(username);
        user.setRoles( Arrays.asList(new String[]{role})  );
        user.setPasswordHash( new HashUtil().hash(password) );
        user.setAddress("");
        user.setDescription("");
        user.setDob("");
        user.setEducation(new ArrayList<Education>());
        user.setExperiences(new ArrayList<Experience>());
        user.setFirstname("");
        user.setGithub("");
        user.setLastname("");
        user.setLinkedin("");
        user.setNumber("");

        UserDAO userDAO = new UserDAO();
        try {
            userDAO.createUser(user);

        } catch (UserAlreadyExistsException e) {
            return unauthorized("user_already_exists");
        }

        return ok("The user has been added successfully");
    }

}
