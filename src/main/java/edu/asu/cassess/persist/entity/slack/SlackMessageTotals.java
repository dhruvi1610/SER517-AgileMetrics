package edu.asu.cassess.persist.entity.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "slack_messagetotals")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackMessageTotals {

    @EmbeddedId
    MessageTotalsID compositeId;

    @Column(name = "fullName")
    private String full_name;

    @Column(name = "display_name")
    private String display_name;

    @Column(name = "team")
    private String team;

    @Column(name = "course")
    private String course;

    @Column(name = "messageCount")
    private int message_count;

    public SlackMessageTotals(){

    }

    public SlackMessageTotals(MessageTotalsID compositeId, String full_name, String team, String course, int message_count, String display_name) {
        this.compositeId = compositeId;
        this.full_name = full_name;
        this.team = team;
        this.course = course;
        this.message_count = message_count;
        this.display_name = display_name;
    }



    public MessageTotalsID getCompositeId() {
        return compositeId;
    }

    public void setCompositeId(MessageTotalsID compositeId) {
        this.compositeId = compositeId;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
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

    public int getMessage_count() {
        return message_count;
    }

    public void setMessage_count(int message_count) {
        this.message_count = message_count;
    }



    }
