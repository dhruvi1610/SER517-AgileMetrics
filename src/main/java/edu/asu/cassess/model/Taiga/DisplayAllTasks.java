package edu.asu.cassess.model.Taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Subselect(value = "SELECT retrievalDate, tasksClosed, tasksOpen, tasksInProgress, tasksReadyForTest, tasksNew FROM cassess.tasktotals WHERE fullName = ?1")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DisplayAllTasks {

    @Id
    @Column(name = "retrievalDate")
    private String retrievalDate;

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


    public DisplayAllTasks() {

    }

    public DisplayAllTasks(String retrievalDate, int tasks_closed, int tasks_new, int tasks_in_progress, int tasks_ready_for_test, int tasks_open) {
        Date date = new Date();
        this.retrievalDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        this.tasks_closed = tasks_closed;
        this.tasks_new = tasks_new;
        this.tasks_in_progress = tasks_in_progress;
        this.tasks_ready_for_test = tasks_ready_for_test;
        this.tasks_open = tasks_open;
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

    public String getRetrievalDate() {
        return retrievalDate;
    }

    public void setRetrievalDate(String retrievalDate) {

        Date date = new Date();
        this.retrievalDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
    }


}