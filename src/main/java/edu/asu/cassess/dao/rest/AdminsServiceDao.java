package edu.asu.cassess.dao.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.persist.entity.rest.Admin;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.repo.rest.AdminsRepo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Component
@Transactional
public class AdminsServiceDao {

    protected EntityManager entityManager;
    @Autowired
    private AdminsRepo adminsRepo;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T> Object create(Admin adminInput) {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.admins WHERE course = ?1 AND email = ?2", Admin.class);
        query.setParameter(1, adminInput.getCourse());
        query.setParameter(2, adminInput.getEmail());
        List results = query.getResultList();
        if (!results.isEmpty()) {
                return new RestResponse(adminInput.getEmail() + " already exists in database");
        } else {
            adminsRepo.save(adminInput);
            return adminInput;
        }
    }

    public <T> Object update(Admin adminInput) {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.admins WHERE course = ?1 AND email = ?2", Admin.class);
        query.setParameter(1, adminInput.getCourse());
        query.setParameter(2, adminInput.getEmail());
        List results = query.getResultList();
        if (!results.isEmpty()) {
                delete((Admin)results.get(0));
                adminsRepo.save(adminInput);
                return adminInput;
            } else {
            return new RestResponse(adminInput.getEmail() + " does not exist in database");
        }
    }

    public <T> Object find(String email, String course) {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.admins WHERE course = ?1 AND email = ?2", Admin.class);
        query.setParameter(1, course);
        query.setParameter(2, email);
        List results = query.getResultList();
        if (!results.isEmpty()) {
            Admin admin = (Admin) results.get(0);
            return admin;
        } else {
            return new RestResponse(email + " does not exist in database");
        }
    }

    public <T> Object delete(Admin admin) {
        Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.admins WHERE course = ?1 AND email = ?2", Admin.class);
        query.setParameter(1, admin.getCourse());
        query.setParameter(2, admin.getEmail());
        query.executeUpdate();
        return new RestResponse(admin.getEmail() + " has been removed from the database");
    }

    public List<Admin> listReadAll() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.admins", Admin.class);
        List<Admin> resultList = query.getResultList();
        return resultList;
    }

    public List<Admin> listReadByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.admins WHERE course = ?1", Admin.class);
        query.setParameter(1, course);
        List<Admin> resultList = query.getResultList();
        return resultList;
    }


    public JSONObject listCreate(List<Admin> admins) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Admin adminInput : admins) {
            Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.admins WHERE course = ?1 AND email = ?2", Admin.class);
            query.setParameter(1, adminInput.getCourse());
            query.setParameter(2, adminInput.getEmail());
            List results = query.getResultList();
            if (!results.isEmpty()) {
                    try {
                        failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(adminInput.getEmail() + " already exists in database"))));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                } else {
                    adminsRepo.save(adminInput);
                    try {
                        successArray.put(new JSONObject(ow.writeValueAsString(adminInput)));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
            }
        }
        JSONObject returnJSON = new JSONObject();
        returnJSON.put("success", successArray);
        returnJSON.put("failure", failureArray);
        return returnJSON;
    }

    public JSONObject listUpdate(List<Admin> admins) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Admin adminInput : admins) {
            Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.admins WHERE course = ?1 AND email = ?2", Admin.class);
            query.setParameter(1, adminInput.getCourse());
            query.setParameter(2, adminInput.getEmail());
            List results = query.getResultList();
            if (results.isEmpty()) {
                    try {
                        failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(adminInput.getEmail() + " does not exist in database"))));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                } else {
                    delete((Admin)results.get(0));
                    adminsRepo.save(adminInput);
                    try {
                        successArray.put(new JSONObject(ow.writeValueAsString(adminInput)));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
            }
        }
        JSONObject returnJSON = new JSONObject();
        returnJSON.put("success", successArray);
        returnJSON.put("failure", failureArray);
        return returnJSON;
    }

    public <T> Object deleteByCourse(Course course) {
        Query preQuery = getEntityManager().createNativeQuery("SELECT * FROM cassess.admins WHERE course = ?1 LIMIT 1", Admin.class);
        preQuery.setParameter(1, course);
        List results = preQuery.getResultList();
        if (!results.isEmpty()) {
            Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.admins WHERE course = ?1");
            query.setParameter(1, course);
            query.executeUpdate();
            return new RestResponse("All admin in course " + course + " have been removed from the database");
        } else {
            return new RestResponse("No admin in course " + course + " exist in the database");
        }
    }

    public List<CourseList> listGetCoursesForAdmin(String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT course FROM cassess.admins WHERE email = ?1", CourseList.class);
        query.setParameter(1, email);
        List<CourseList> resultList = query.getResultList();
        return resultList;
    }

}
