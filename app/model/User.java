package model;

import net.minidev.json.JSONObject;

import java.util.List;

/**
 * Created by root on 23/03/16.
 */
public class User implements Cloneable{
    private String email,passwordHash;
    private List<String> roles;
    private List<Education> education;
    private List<Skills> skills;
    private List<Experience> experience;

    public User() {
    }

    public User(JSONObject json){
        this.email = (String) json.get("username");
        this.passwordHash = (String) json.get("hash");
        this.roles = (List<String>)json.get("roles");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(List<Education> education) {
        this.education = education;
    }

    public List<Skills> getSkills() {
        return skills;
    }

    public void setSkills(List<Skills> skills) {
        this.skills = skills;
    }

    public List<Experience> getExperience() {
        return experience;
    }

    public void setExperience(List<Experience> experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", roles=" + roles +
                ", education=" + education +
                ", skills=" + skills +
                ", experience=" + experience +
                '}';
    }



    public User noPassword() {
        try {
            User u =(User) this.clone();
            u.setPasswordHash("");
            return u;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
