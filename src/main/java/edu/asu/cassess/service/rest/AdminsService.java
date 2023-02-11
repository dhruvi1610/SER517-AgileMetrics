package edu.asu.cassess.service.rest;

import edu.asu.cassess.dao.rest.AdminsServiceDao;
import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.persist.entity.rest.Admin;
import edu.asu.cassess.persist.entity.rest.Course;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.ejb.EJB;
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
