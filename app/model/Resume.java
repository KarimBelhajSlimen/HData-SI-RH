package model;

import java.util.Date;
import java.util.List;

/**
 * Created by root on 23/03/16.
 */
public class Resume implements Cloneable{
    private String company,name,statut,formation,skills,langues,experiences;


    public Resume() {
    }

    @Override
    public String toString() {
        return "Resume{" +
                "company='" + company + '\'' +
                ", name='" + name + '\'' +
                ", statut='" + statut + '\'' +
                ", formation=" + formation +
                ", skills=" + skills +
                ", langues=" + langues +
                ", experiences=" + experiences +
                '}';
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public String getLangues() {
        return langues;
    }

    public void setLangues(String langues) {
        this.langues = langues;
    }


    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getExperiences() {
        return experiences;
    }

    public void setExperiences(String experiences) {
        this.experiences = experiences;
    }


}
