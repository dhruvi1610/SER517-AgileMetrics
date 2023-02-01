package edu.asu.cassess.persist.entity.taiga;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Embeddable
public class TaskTotalsID implements Serializable {

    @Column(name = "email")
    public String email;

    @Column(name = "retrievalDate")
    private String retrievalDate;

    @Column(name = "team")
    private String team;

    @Column(name = "course")
    private String course;

    public TaskTotalsID() {
    }

    public TaskTotalsID(String email, String team, String course) {
        this.email = email;
        Date date = new Date();
        this.retrievalDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        this.team = team;
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getRetrievalDate() {
        return retrievalDate;
    }

    public void setRetrievalDate(String retrievalDate) {

        Date date = new Date();
        this.retrievalDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
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
