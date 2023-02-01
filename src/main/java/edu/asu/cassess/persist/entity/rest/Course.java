package edu.asu.cassess.persist.entity.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "courses")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {

    @Transient
    public static String COURSE_STRING;
    @Id
    @Column(name = "course")
    public String course;

    @Column(name = "slack_token")
    protected String slack_token;

    @Column(name = "taiga_token")
    protected String taiga_token;

    @Column(name = "start_date")
    protected Date start_date;

    @Column(name = "end_date")
    protected Date end_date;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", fetch = FetchType.EAGER)
    protected List<Team> teams;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", fetch = FetchType.EAGER)
    protected List<Admin> admins;

    public Course() {
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        COURSE_STRING = course;
        this.course = COURSE_STRING;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getSlack_token() {
        return slack_token;
    }

    public void setSlack_token(String slack_token) {
        this.slack_token = slack_token;
    }

    public String getTaiga_token() {
        return taiga_token;
    }

    public void setTaiga_token(String taiga_token) {
        this.taiga_token = taiga_token;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        for (Team team : teams) {
            team.setCourse(COURSE_STRING);
        }
        this.teams = teams;

    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        for (Admin admin : admins) {
            admin.setCourse(COURSE_STRING);
        }
        this.admins = admins;
    }

}