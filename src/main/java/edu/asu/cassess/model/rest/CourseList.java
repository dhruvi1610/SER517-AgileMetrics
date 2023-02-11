package edu.asu.cassess.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Subselect("SELECT course AS 'course' FROM cassess.students")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseList {

    @Id
    @Column(name = "course")
    public String course;

    public CourseList() {
    }

    public CourseList(String course) {
        this.course = course;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

}
