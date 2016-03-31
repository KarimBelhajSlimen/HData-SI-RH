package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by root on 23/03/16.
 */
public class CorsController extends Controller {

    public Result options(String all) {
        response().setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        response().setHeader("Access-Control-Max-Age", "3600");
        response().setHeader("Access-Control-Allow-Headers", "jwt, Origin, X-Requested-With, Content-Type, Accept, Authorization, X-Auth-Token, Cache-Control");
        response().setHeader("Access-Control-Allow-Credentials", "true");
        response().setHeader("Access-Control-Allow-Origin", "*");
        return ok();
    }
}
