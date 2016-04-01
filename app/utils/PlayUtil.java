package utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
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
        Config conf = ConfigFactory.load();
        return  conf.getString(s);
    }
}
