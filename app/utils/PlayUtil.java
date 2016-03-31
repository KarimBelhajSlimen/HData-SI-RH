package utils;

import play.Play;

/**
 * Created by root on 30/03/16.
 */
public class PlayUtil {
    public String getProperty(String s){
        return Play.application().configuration().getString(s);
    }
}
