package utils;

import play.Play;

/**
 * Created by root on 30/03/16.
 */
public class PlayUtil {
    /**
     * Reads parameters from application's configuration
     * @param s Property's name
     * @return Value
     */
    public String getProperty(String s){
        return Play.application().configuration().getString(s);
    }
}
