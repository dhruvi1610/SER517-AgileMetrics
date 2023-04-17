package edu.asu.cassess.service.rest;

import edu.asu.cassess.dao.rest.AdminsServiceDao;
import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.persist.entity.rest.Admin;
import edu.asu.cassess.persist.entity.rest.Course;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import javax.ejb.EJB;
import java.util.Arrays;
import java.util.List;

@Service
public class AdminsService implements IAdminsService {

    @EJB
    private AdminsServiceDao adminsDao;

    @Override
    public <T> Object create(Admin admin) {
        return adminsDao.create(admin);
    }

    @Override
    public <T> Object update(Admin admin) {
        return adminsDao.update(admin);
    }

    @Override
    public <T> Object find(String email, String course) {
        return adminsDao.find(email, course);
    }

    @Override
    public <T> Object delete(Admin admin) {
        return adminsDao.delete(admin);
    }

    @Override
    public <T> List<Admin> listReadAll() {
        return adminsDao.listReadAll();
    }
    @Override
    public List<Admin> getAdminFromCanvas(String canvasToken, String course_id){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://canvas.asu.edu/api/v1"+course_id+"/enrollments?type[]=TeacherEnrollment";
        HttpHeaders headers = new HttpHeaders();
        if(!canvasToken.equalsIgnoreCase("na")) {
            headers.setBearerAuth(canvasToken);
        }

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Admin[]> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, Admin[].class);

        } catch (Exception e) {
            System.out.println("Canvas fetch stats failed. " + e.getMessage());
        }

        if(responseEntity != null && responseEntity.getBody() != null && !Arrays.toString(responseEntity.getBody()).startsWith("{}")) {
            List<Admin> admins = Arrays.asList(responseEntity.getBody());

            return admins;
        }
        return null;
    }
    @Override
    public <T> List<Admin> listReadByCourse(String course) {
        return adminsDao.listReadByCourse(course);
    }

    @Override
    public JSONObject listUpdate(List<Admin> admins) {
        return adminsDao.listUpdate(admins);
    }

    @Override
    public JSONObject listCreate(List<Admin> admins) {
        return adminsDao.listCreate(admins);
    }

    @Override
    public <T> Object deleteByCourse(Course course) {
        return adminsDao.deleteByCourse(course);
    }

    @Override
    public List<CourseList> listGetCoursesForAdmin(String email) {
        return adminsDao.listGetCoursesForAdmin(email);
    }
}
