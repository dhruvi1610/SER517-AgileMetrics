package edu.asu.cassess.dao.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.persist.entity.rest.Admin;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.repo.rest.CourseRepo;
import edu.asu.cassess.service.rest.IAdminsService;
import edu.asu.cassess.service.rest.ITeamsService;
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
public class CourseServiceDao {

    protected EntityManager entityManager;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private ITeamsService teamsService;
    @Autowired
    private IAdminsService adminsService;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Save this course to the database.
     *
     * @param courseInput the Course object to save
     * @return courseInput or a message if the course is already in the database
     */
    @Transactional
    public <T> Object create(Course courseInput) {
        List<Team> teams = courseInput.getTeams();
        List<Admin> admins = courseInput.getAdmins();
        if (courseRepo.exists(courseInput.getCourse())) {
            return new RestResponse(courseInput.getCourse() + " already exists in database");
        } else {
            courseRepo.save(courseInput);
            if (courseInput.getTeams() != null) {
                teamsService.listCreate(teams);
            }
            if (courseInput.getAdmins() != null) {
                adminsService.listCreate(admins);
            }
            return courseInput;
        }
    }

    /**
     * Update this course in the database.
     *
     * @param courseInput the Course to update
     * @return courseInput or a message indicating the course does not exist in the database
     */
    @Transactional
    public <T> Object update(Course courseInput) {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.courses WHERE course = ?1", Course.class);
        query.setParameter(1, courseInput.getCourse());
        List<Team> teams = courseInput.getTeams();
        List<Admin> admins = courseInput.getAdmins();
        List results = query.getResultList();
        if (!results.isEmpty()) {
            Course course = (Course) results.get(0);
            courseRepo.save(courseInput);
            if (course.getTeams() != null) {
                teamsService.listUpdate(teams);
            }
            if (course.getAdmins() != null) {
                adminsService.listUpdate(admins);
            }
            return courseInput;
        } else {
            return new RestResponse(courseInput + " does not exist in database");
        }
    }

    /**
     * Find a course in the database by its name.
     *
     * @param courseName the name to be used to find the course
     * @return the Course object or a message indicating the course does not exist
     */
    @Transactional
    public <T> Object find(String courseName) {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.courses WHERE course = ?1", Course.class);
        query.setParameter(1, courseName);
        List<Course> results = query.getResultList();
        if (!results.isEmpty()) {
            return (Course) results.get(0);
        } else {
            return new RestResponse(courseName + " does not exist in database");
        }
    }

    /**
     * Delete a course from the database by its name.
     *
     * @param course the course to be removed from the database
     * @return a message indicating the course was deleted or not found
     */
    @Transactional
    public <T> Object delete(Course course) {
        if (course != null) {
            adminsService.deleteByCourse(course);
            teamsService.deleteByCourse(course);
            Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.courses WHERE course = ?1");
            query.setParameter(1, course.getCourse());
            query.executeUpdate();
            return new RestResponse(course.getCourse() + " has been removed from the database");
        } else {
            return new RestResponse(course.getCourse() + " does not exist in the database");
        }
    }

    /**
     * Fetch a list of all courses from database.
     *
     * @return A List of Courses
     * @throws DataAccessException
     */
    @Transactional
    public List<Course> listRead() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.courses", Course.class);
        List<Course> resultList = query.getResultList();
        return resultList;
    }

    /**
     * Fetch course list from students table.
     *
     * @return A List of CourseList
     * @throws DataAccessException
     */
    @Transactional
    public List<CourseList> listGetCourses() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT course FROM cassess.courses", CourseList.class);
        List<CourseList> resultList = query.getResultList();
        return resultList;
    }

    /**
     * Create new courses in database from a List of Courses.
     *
     * @param courses List of Courses
     * @return JSONObject of courses or messages if course(s) already exist
     */
    @Transactional
    public JSONObject listCreate(List<Course> courses) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Course course : courses) {
            if (course.getTeams() != null) {
                teamsService.listCreate(course.getTeams());
            }
            if (course.getAdmins() != null) {
                adminsService.listCreate(course.getAdmins());
            }
            if (courseRepo.findOne(course.getCourse()) != null) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(course.getCourse() + " already exists in the database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                courseRepo.save(course);
                try {
                    successArray.put(new JSONObject(ow.writeValueAsString(course)));
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

    /**
     * Update courses from a List.
     *
     * @param courses List of Courses
     * @return JSONObject of course information or messages if course(s) do not exist
     */
    @Transactional
    public JSONObject listUpdate(List<Course> courses) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Course course : courses) {
            if (course.getTeams() != null) {
                teamsService.listUpdate(course.getTeams());
            }
            if (course.getAdmins() != null) {
                adminsService.listUpdate(course.getAdmins());
            }
            if (courseRepo.findOne(course.getCourse()) == null) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(course.getCourse() + " does not exist in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                courseRepo.save(course);
                try {
                    successArray.put(new JSONObject(ow.writeValueAsString(course)));
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
}