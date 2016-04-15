package controllers;

import cors.CorsAction;
import dao.UnknownUsername;
import dao.UserDAO;
import model.User;
import model.Resume;
import org.apache.commons.collections.map.HashedMap;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import security.Secure;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 23/03/16.
 */

public class UserController extends RestController {

    public Result addProfile() throws IOException {
        User u = Json.fromJson(jsonRequest().findPath("user"),User.class);
        u.setEmail(credentials().getEmail());
        UserDAO userDAO = new UserDAO();
        userDAO.addProfile(u);


        Resume r = Json.fromJson(jsonRequest().findPath("resume"),Resume.class);

        if(!r.getExperiences().equals("") && !r.getExperiences().equals("") && !r.getExperiences().equals("")) {
            userDAO.addExistingCV(r, u.getEmail());
        }
        // String cv = Json.fromJson(jsonRequest().findPath("cv"),String.class);

        System.out.println("le cv reçu **** "+r);

        Map<String,Object> map = new HashMap();
        map.put("user",u);


        if(!jsonRequest().findPath("picture").asText().equals("")) {
            userDAO.addPicture(jsonRequest().findPath("picture").asText(), u.getEmail());
            map.put("picture",jsonRequest().findPath("picture").asText());
        }else {
             map.put("picture", "img/user.png");
        }


        return ok( Json.stringify( Json.toJson(map) ) );
    }

    public Result getProfile() throws IOException, UnknownUsername {
        UserDAO userDAO = new UserDAO();
        User u = userDAO.getByUsername(credentials().getEmail());
        Resume r= userDAO.getResume(credentials().getEmail());
        u = u.noPassword();
        String picture = userDAO.getPicture(credentials().getEmail());
        Map<String,Object> map = new HashMap();
        map.put("user",u);
        map.put("picture",picture);
        map.put("resume",r);
        System.out.println(Json.stringify( Json.toJson(map) ) );
        return ok( Json.stringify( Json.toJson(map) ) );

    }
}
