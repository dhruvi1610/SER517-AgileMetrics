package edu.asu.cassess.service.dto;

import edu.asu.cassess.persist.entity.taiga.TaskTotalsID;
import org.dozer.Mapping;

public class TaskTotalsDto {


    @Mapping("compositeId")
    private TaskTotalsID compositeId;

    @Mapping("fullName")
    private String full_name;

    @Mapping("project")
    private String project_name;

    @Mapping("team")
    private String team;

    @Mapping("course")
    private String course;

    @Mapping("tasksClosed")
    private int tasks_closed;

    @Mapping("tasksNew")
    private int tasks_new;

    @Mapping("tasksInProgress")
    private int tasks_in_progress;

    @Mapping("tasksReadyForTest")
    private int tasks_ready_for_test;

    @Mapping("tasksOpen")
    private int tasks_open;

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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
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


}
