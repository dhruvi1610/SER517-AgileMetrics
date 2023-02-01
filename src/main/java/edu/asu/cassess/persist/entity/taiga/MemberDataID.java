package edu.asu.cassess.persist.entity.taiga;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class MemberDataID implements Serializable {

    @Column(name = "id")
    public int id;

    @Column(name = "email")
    public String email;

    @Column(name = "team")
    private String team;

    @Column(name = "course")
    private String course;

    public MemberDataID() {
    }

    public MemberDataID(int id, String email, String team, String course) {
        this.id = id;
        this.email = email;
        this.team = team;
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

