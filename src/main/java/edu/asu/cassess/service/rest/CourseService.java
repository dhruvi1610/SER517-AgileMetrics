package edu.asu.cassess.service.rest;

import edu.asu.cassess.dao.rest.CourseServiceDao;
import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.persist.entity.rest.Course;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.ejb.EJB;
import java.util.List;

@Service
public class CourseService implements ICourseService {

    @EJB
    private CourseServiceDao courseServiceDao;

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
