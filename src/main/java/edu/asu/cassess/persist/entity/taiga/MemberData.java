package edu.asu.cassess.persist.entity.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "memberdata")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberData {

    @EmbeddedId
    MemberDataID compositeId;

    @Column(name = "fullName")
    private String full_name;

    @Column(name = "project")
    private String project_name;

    @Column(name = "project_slug")
    private String project_slug;

    @Column(name = "roleName")
    private String role_name;

    public MemberData() {

    }

    public MemberData(MemberDataID compositeId, String full_name, String project_name, String project_slug, String role_name) {
        this.compositeId = compositeId;
        this.full_name = full_name;
        this.project_name = project_name;
        this.project_slug = project_slug;
        this.role_name = role_name;
    }

    public MemberDataID getCompositeId() {
        return compositeId;
    }

    public void setCompositeId(MemberDataID compositeId) {
        this.compositeId = compositeId;
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

}