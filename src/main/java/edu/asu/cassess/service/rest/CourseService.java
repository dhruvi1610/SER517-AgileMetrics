package edu.asu.cassess.service.rest;

import edu.asu.cassess.dao.rest.CourseServiceDao;
import edu.asu.cassess.dto.TaigaTokenRefreshRequestDto;
import edu.asu.cassess.dto.TaigaTokenRefreshResponseDto;
import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.repo.rest.CourseRepo;
import java.util.ArrayList;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ejb.EJB;
import java.util.List;
import org.springframework.web.client.RestTemplate;

@Service
public class CourseService implements ICourseService {

    @EJB
    private CourseServiceDao courseServiceDao;

    @Autowired
    private CourseRepo courseRepo;

    @Override
    public <T> Object create(Course course) {

        return courseServiceDao.create(course);
    }

    @Override
    public <T> Object update(Course course) {

        return courseServiceDao.update(course);
    }

    @Override
    public <T> Object read(String course) {

        return courseServiceDao.find(course);
    }

    @Override
    public <T> Object delete(Course course) {

        return courseServiceDao.delete(course);

    }

    @Override
    public List<CourseList> listGetCourses() {
        return courseServiceDao.listGetCourses();
    }

    @Override
    public List<Course> refreshTaigaTokes() {
        String url = "https://api.taiga.io/api/v1/auth/refresh";
        RestTemplate restTemplate = new RestTemplate();
        List<Course> courses = courseRepo.findActiveCourses();
        List<Course> updatedCourses = new ArrayList<>();

        for(Course course : courses) {
            TaigaTokenRefreshResponseDto responseObject = null;
            try {
                TaigaTokenRefreshRequestDto body = new TaigaTokenRefreshRequestDto(course.getTaigaRefreshToken());
                responseObject = restTemplate.postForObject(url, body, TaigaTokenRefreshResponseDto.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(responseObject != null) {
                course.setTaiga_token(responseObject.getAuthToken());
                course.setTaigaRefreshToken(responseObject.getRefreshToken());
                updatedCourses.add(course);
            }
        }

        return courseRepo.save(updatedCourses);
    }

    @Override
    public <T> List<Course> listRead() {

        return courseServiceDao.listRead();
    }

    @Override
    public JSONObject listCreate(List<Course> courses) {

        return courseServiceDao.listCreate(courses);
    }

    @Override
    public JSONObject listUpdate(List<Course> courses) {

        return courseServiceDao.listUpdate(courses);
    }
}
