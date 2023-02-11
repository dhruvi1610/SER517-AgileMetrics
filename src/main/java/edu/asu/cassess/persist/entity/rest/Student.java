package edu.asu.cassess.persist.entity.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.UUID;

import static edu.asu.cassess.persist.entity.rest.Course.COURSE_STRING;
import static edu.asu.cassess.persist.entity.rest.Team.TEAM_STRING;

@Entity
@Table(name = "students")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "email")
    private String email;

    @Column(name = "full_name")
    private String full_name;

    @Column(name = "team_name")
    private String team_name;

    @Column(name = "course")
    private String course;

    @Column(name = "github_username")
    private String github_username;

    @Column(name = "taiga_username")
    private String taiga_username;

    @Column(name = "slack_username")
    private String slack_username;

    @Transient
    private String password;

    @Column(name = "enabled", columnDefinition = "BIT", length = 1)
    private Boolean enabled;

    public Student() {
        this.enabled = true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        if (team_name == null) {
            team_name = TEAM_STRING;
        }
        this.team_name = team_name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        if (course == null) {
            course = COURSE_STRING;
        }
        this.course = course;
    }

    public String getGithub_username() {
        return github_username;
    }

    public void setGithub_username(String github_username) {
        this.github_username = github_username;
    }

    public String getTaiga_username() {
        return taiga_username;
    }

    public void setTaiga_username(String taiga_username) {
        this.taiga_username = taiga_username;
    }

    public String getSlack_username() {
        return slack_username;
    }

    public void setSlack_username(String slack_username) {
        this.slack_username = slack_username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled() {
        this.enabled = true;
    }

    public void setDisabled() {
        this.enabled = false;
    }


}