package security;

import net.minidev.json.JSONObject;
import java.util.List;

/**
 * Contains email and roles
 */
public class Credentials {

    private String email;
    private List<String> roles;

    public Credentials(String email, List<String> roles) {
        this.email = email;
        this.roles = roles;
    }

    public Credentials(JSONObject json) {
        this.email = (String) json.get("email");
        this.roles = (List<String>)json.get("roles");
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Credentials() {
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}
