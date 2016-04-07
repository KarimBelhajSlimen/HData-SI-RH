package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import dao.UnknownUsername;
import dao.UserDAO;
import model.User;
import org.apache.hadoop.hbase.util.Hash;
import play.libs.Json;
import play.mvc.Result;
import security.Secure;

import java.io.IOException;
import java.util.HashMap;
import java.util.*;
import play.libs.Json;


import cors.CorsAction;
import dao.UserAlreadyExistsException;

import play.mvc.Controller;
import play.mvc.With;
import security.Auth;


/**
 * Created by root on 23/03/16.
 */
public class StaffController extends RestController {



    public Result getList() throws IOException, UnknownUsername {

        UserDAO userDAO = new UserDAO();
        List<HashMap> Userlist = userDAO.getList();
        List<HashMap> result = new ArrayList<HashMap>();

        //System.out.println("getting list in controller" );
        //System.out.println("email: "+Userlist.get(0).get("email")+"role: "+Userlist.get(0).get("roles"));

        for(HashMap user : Userlist){
            if(user.get("roles").equals("RH") || user.get("roles").equals("consultant")  || user.get("roles").equals("admin")){
                 result.add(user);
            }
        }
        System.out.println(Json.stringify( Json.toJson(result) ) );
        return ok( Json.stringify( Json.toJson(result) ) );

    }



    public Result getMember(String email) throws IOException, UnknownUsername {


            //String email = null;
            //JsonNode json = jsonRequest();


            try {
               // email = json.findPath("cible").asText();
                System.out.println("getting member ******** " + email);

            } catch (NullPointerException e) {
                return unauthorized("wrong_format");
            }

            User u = null;
            String picture = null;
            try {

                UserDAO userDAO = new UserDAO();
                u = userDAO.getByUsername(email);
                u = u.noPassword();
                picture = userDAO.getPicture(email);
            } catch (UnknownUsername unknownUsername) {
                return unauthorized("unknown_username");
            }
            Map<String, Object> map = new HashMap();
            map.put("user", u);
            map.put("picture", picture);
            System.out.println(Json.stringify(Json.toJson(map)));
            return ok(Json.stringify(Json.toJson(map)));


    }
}
