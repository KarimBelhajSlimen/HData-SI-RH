package security;

import play.mvc.With;

import java.lang.annotation.*;

/**
 * Created by root on 23/03/16.
 */
@With(SecureAction.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Secure{

    public String[] value();


}