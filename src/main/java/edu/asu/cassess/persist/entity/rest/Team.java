package edu.asu.cassess.persist.entity.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

import static edu.asu.cassess.persist.entity.rest.Course.COURSE_STRING;

@Entity
@Table(name = "teams")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {

    @Transient
    public static String TEAM_STRING;
    @Transient
    public Long id;
    @Transient
    public String name;

    @Id
    @Column(name = "team_name")
    public String team_name;
    @Column(name = "course")
    public String course;
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team_name", fetch = FetchType.EAGER)
    public List<Student> students;
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team_name", fetch = FetchType.EAGER)
    public List<Channel> channels;
    @Column(name = "slack_team_id")
    protected String slack_team_id;
    @Column(name = "github_repo_id")
    protected String github_repo_id;
    @Column(name = "taiga_project_slug")
    protected String taiga_project_slug;
    @Column(name = "github_token")
    protected String github_token;
    @Column(name = "github_owner")
    protected String github_owner;

    @Column(name = "taiga_custom_attribute")
    protected String taiga_custom_attribute;

    public Team() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        TEAM_STRING = team_name;
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

    public String getSlack_team_id() {
        return slack_team_id;
    }

    public void setSlack_team_id(String slack_team_id) {
        this.slack_team_id = slack_team_id;
    }

    public String getGithub_repo_id() {
        return github_repo_id;
    }

    public void setGithub_repo_id(String github_repo_id) {
        this.github_repo_id = github_repo_id;
    }

    public String getTaiga_project_slug() {
        return taiga_project_slug;
    }

    public void setTaiga_project_slug(String taiga_project_slug) {
        this.taiga_project_slug = taiga_project_slug;
    }

    public List<Student> getStudents() {
        return students;
    }

    public String getGithub_token() {
        return github_token;
    }

    public void setGithub_token(String github_token) {
        this.github_token = github_token;
    }

    public String getGithub_owner() {
        return github_owner;
    }

    public void setGithub_owner(String github_owner) {
        this.github_owner = github_owner;
    }

    public void setStudents(List<Student> students) {
        for (Student student : students) {
            student.setCourse(COURSE_STRING);
            student.setTeam_name(TEAM_STRING);
        }
        this.students = students;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        for (Channel channel : channels) {
            channel.setCourse(COURSE_STRING);
            channel.setTeam_name(TEAM_STRING);
        }
        this.channels = channels;
    }

    public String getTaiga_custom_attribute() {
        return taiga_custom_attribute;
    }

    public void setTaiga_custom_attribute(String taiga_custom_attribute) {
        this.taiga_custom_attribute = taiga_custom_attribute;
    }

}