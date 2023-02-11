package edu.asu.cassess.persist.entity.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "taskdata")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskData {

    @Id
    @Column(name = "id")
    public int id;

    @Column(name = "project")
    public Long project;
    public AssignmentData assigned_to_extra_info;
    public StatusData status_extra_info;

    public TaskData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getProject() {
        return project;
    }

    public void setProject(Long project) {
        this.project = project;
    }

    public AssignmentData getAssignmentData() {
        return assigned_to_extra_info;
    }

    public StatusData getStatusData() {
        return status_extra_info;
    }

    public void setStatusData(StatusData status_extra_info) {
        this.status_extra_info = status_extra_info;
    }

}
