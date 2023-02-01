package edu.asu.cassess.persist.entity.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssignmentData {

    @Column(name = "username")
    private String username;

    @Column(name = "full_name")
    private String full_name_display;

    @Column(name = "member_id")
    private Long id;

    public AssignmentData() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name_display() {
        return full_name_display;
    }

    private void setFull_name_display(String full_name_display) {
        this.full_name_display = full_name_display;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

