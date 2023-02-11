package edu.asu.cassess.persist.entity.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaigaMember {

    public int id;

    private String full_name;

    private String project_name;

    private String project_slug;

    private String role_name;

    private String team;

    private String course;

    private String user_email;

    public TaigaMember() {

    }

    public TaigaMember(int id, String full_name, String project_name, String project_slug, String role_name, String user_email, String team, String course) {
        this.id = id;
        this.full_name = full_name;
        this.project_name = project_name;
        this.project_slug = project_slug;
        this.role_name = role_name;
        this.user_email = user_email;
        this.team = team;
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_slug() {
        return project_slug;
    }

    public void setProject_slug(String project_slug) {
        this.project_slug = project_slug;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}


