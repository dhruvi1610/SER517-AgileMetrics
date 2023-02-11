package edu.asu.cassess.persist.entity.github;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Embeddable
public class GitHubPK implements Serializable {

    @Column(name="course")
    private String course;

    @Column(name="team")
    private String team;

    @Column(name="username")
    protected String username;

    @Column(name="date")
    protected String date;

    public GitHubPK() {

    }

    public GitHubPK(String course, String team, String username, Date date) {
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(date);
        this.username = username;
        this.team = team;
        this.course = course;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "GitHubPK{" +
                "date=" + date +
                ", username='" + username + '\'' +
                ", team='" + team + '\'' +
                ", course='" + course + '\'' +
                '}';
    }
}