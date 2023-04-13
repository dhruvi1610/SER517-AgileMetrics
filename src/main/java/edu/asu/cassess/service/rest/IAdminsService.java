package edu.asu.cassess.service.rest;

import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.persist.entity.rest.Admin;
import edu.asu.cassess.persist.entity.rest.Course;
import org.json.JSONObject;

import java.util.List;

public interface IAdminsService {

    <T> Object create(Admin admin);

    <T> Object update(Admin admin);

    <T> Object find(String email, String course);

    <T> Object delete(Admin admin);

    <T> List<Admin> listReadAll();

    <T> List<Admin> listReadByCourse(String course);

    JSONObject listUpdate(List<Admin> admins);

    JSONObject listCreate(List<Admin> admins);

    <T> Object deleteByCourse(Course course);

    List<CourseList> listGetCoursesForAdmin(String email);
    List<Admin> getAdminFromCanvas(String token);
}
