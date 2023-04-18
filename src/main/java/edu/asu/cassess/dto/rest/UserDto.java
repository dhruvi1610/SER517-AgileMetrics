package edu.asu.cassess.dto.rest;

public class UserDto {
    int id;
    int user_id;
    int course_id;
    String type;
    AdminDto user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AdminDto getUser() {
        return user;
    }

    public void setUser(AdminDto user) {
        this.user = user;
    }
}
