package edu.asu.cassess.persist.entity.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tasktotals")
@JsonIgnoreProperties(ignoreUnknown = true)
@SQLDelete(sql = "DELETE FROM cassess.tasktotals WHERE course = ?1 AND team = ?2")
public class TaskTotals {

    @EmbeddedId
    TaskTotalsID compositeId;

    @Column(name = "fullName")
    private String full_name;

    @Column(name = "taiga_username")
    private String taiga_username;

    @Column(name = "slug")
    private String slug;

    @Column(name = "tasksClosed")
    private int tasks_closed;

    @Column(name = "tasksNew")
    private int tasks_new;

    @Column(name = "tasksInProgress")
    private int tasks_in_progress;

    @Column(name = "tasksReadyForTest")
    private int tasks_ready_for_test;

    @Column(name = "tasksOpen")
    private int tasks_open;

    public TaskTotals() {

    }

    public TaskTotals(TaskTotalsID compositeId, String full_name, String taiga_username, String slug, int tasks_closed, int tasks_new, int tasks_in_progress, int tasks_ready_for_test, int tasks_open) {
        this.compositeId = compositeId;
        this.full_name = full_name;
        this.taiga_username = taiga_username;
        this.slug = slug;
        this.tasks_closed = tasks_closed;
        this.tasks_new = tasks_new;
        this.tasks_in_progress = tasks_in_progress;
        this.tasks_ready_for_test = tasks_ready_for_test;
        this.tasks_open = tasks_open;
    }

    public TaskTotalsID getCompositeId() {
        return compositeId;
    }

    public void setCompositeId(TaskTotalsID compositeId) {
        this.compositeId = compositeId;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getTaiga_username() {
        return taiga_username;
    }

    public void setTaiga_username(String taiga_username) {
        this.taiga_username = taiga_username;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getTasks_closed() {
        return tasks_closed;
    }

    public void setTasks_closed(int tasks_closed) {
        this.tasks_closed = tasks_closed;
    }

    public int getTasks_new() {
        return tasks_new;
    }

    public void setTasks_new(int tasks_new) {
        this.tasks_new = tasks_new;
    }

    public int getTasks_in_progress() {
        return tasks_in_progress;
    }

    public void setTasks_in_progress(int tasks_in_progress) {
        this.tasks_in_progress = tasks_in_progress;
    }

    public int getTasks_ready_for_test() {
        return tasks_ready_for_test;
    }

    public void setTasks_ready_for_test(int tasks_ready_for_test) {
        this.tasks_ready_for_test = tasks_ready_for_test;
    }

    public int getTasks_open() {
        return tasks_open;
    }

    public void setTasks_open(int tasks_open) {
        this.tasks_open = tasks_open;
    }

    public String getEmail() {
        return compositeId.getEmail();
    }

    public String getRetrievalDate() {
        return compositeId.getRetrievalDate();
    }


}
