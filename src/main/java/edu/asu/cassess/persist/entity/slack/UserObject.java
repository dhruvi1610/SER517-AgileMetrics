package edu.asu.cassess.persist.entity.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "slack_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserObject {

    @Transient
    private boolean ok;
    @Id
    private String id;
    private String team_id;
    private String name;
    private boolean deleted;
    private String status;
    private String real_name;
    private String course;
    @Embedded
    private UserProfile profile;
    private boolean is_admin;
    private boolean is_owner;

    /**
     * @return the ok
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * @param ok the ok to set
     */
    public void setOk(boolean ok) {
        this.ok = ok;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the team_id
     */
    public String getTeam_id() {
        return team_id;
    }

    /**
     * @param team_id the team_id to set
     */
    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the real_name
     */
    public String getReal_name() {
        return real_name;
    }

    /**
     * @param real_name the real_name to set
     */
    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * @return the profile
     */
    public UserProfile getProfile() {
        return profile;
    }

    /**
     * @param profile the profile to set
     */
    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    /**
     * @return the is_admin
     */
    public boolean isIs_admin() {
        return is_admin;
    }

    /**
     * @param is_admin the is_admin to set
     */
    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    /**
     * @return the is_owner
     */
    public boolean isIs_owner() {
        return is_owner;
    }

    /**
     * @param is_owner the is_owner to set
     */
    public void setIs_owner(boolean is_owner) {
        this.is_owner = is_owner;
    }

}
