package edu.asu.cassess.dao.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.model.Taiga.TeamNames;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.repo.rest.StudentRepo;
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


/**
 * REST API implementation for student-based services.
 *
 * @author tjjohn1
 */
@Component
@Transactional
public class StudentsServiceDao {

    protected EntityManager entityManager;
    @Autowired
    private StudentRepo studentRepo;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Adds this student to the database.
     *
     * @param studentInput the student to add
     * @return the student passed or message if student already exists
     */
    @Transactional
    public <T> Object create(Student studentInput) {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE course = ?1 AND team_name = ?2 AND email = ?3", Student.class);
        query.setParameter(1, studentInput.getCourse());
        query.setParameter(2, studentInput.getTeam_name());
        query.setParameter(3, studentInput.getEmail());
        List results = query.getResultList();
        if (!results.isEmpty()) {
            return new RestResponse(studentInput.getEmail() + " already exists in database");
        } else {
            studentInput.setEnabled();
            studentRepo.save(studentInput);
            return studentInput;
        }
    }

    /**
     * Update the database entry for this student with new information.
     *
     * @param studentInput the student to update
     * @return the student passed or message if no such student exists in database
     */
    @Transactional
    public <T> Object update(Student studentInput) {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE course = ?1 AND team_name = ?2 AND email = ?3", Student.class);
        query.setParameter(1, studentInput.getCourse());
        query.setParameter(2, studentInput.getTeam_name());
        query.setParameter(3, studentInput.getEmail());
        List results = query.getResultList();
        if (!results.isEmpty()) {
            delete((Student)results.get(0));
            studentRepo.save(studentInput);
            return studentInput;
        } else {
            return new RestResponse(studentInput.getEmail() + " does not exist in database");
        }
    }

    /**
     * Finds student in database by email address.
     *
     * @param email the string (email address) to search
     * @return the student or a message if the student is not found
     */
    @Transactional
    public <T> Object find(String email, String team, String course) {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE course = ?1 AND team_name = ?2 AND email = ?3", Student.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List results = query.getResultList();
        if (!results.isEmpty()) {
            Student student = (Student) results.get(0);
            return student;
        } else {
            return new RestResponse(email + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object findGitHubUser(String github_username, String team, String course) {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE course = ?1 AND team_name = ?2 AND github_username = ?3", Student.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, github_username);
        List results = query.getResultList();
        if (!results.isEmpty()) {
            Student student = (Student) results.get(0);
            return student;
        } else {
            return new RestResponse(github_username + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object findTaigaUser(String taiga_username, String team, String course) {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE course = ?1 AND team_name = ?2 AND taiga_username = ?3", Student.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, taiga_username);
        List results = query.getResultList();
        if (!results.isEmpty()) {
            Student student = (Student) results.get(0);
            return student;
        } else {
            return new RestResponse(taiga_username + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object findSlackUser(String slack_username, String team, String course) {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE course = ?1 AND team_name = ?2 AND slack_username = ?3", Student.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, slack_username);
        List results = query.getResultList();
        if (!results.isEmpty()) {
            Student student = (Student) results.get(0);
            return student;
        } else {
            return new RestResponse(slack_username + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object find(String email, String course) {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE course = ?1 AND email = ?2", Student.class);
        query.setParameter(1, course);
        query.setParameter(2, email);
        List results = query.getResultList();
        if (!results.isEmpty()) {
            Student student = (Student) results.get(0);
            return student;
        } else {
            return new RestResponse(email + " does not exist in database");
        }
    }


    /**
     * Delete the student record from the database associated with this email address.
     *
     * @param student the student object to remove from the database
     * @return a message indicating success or no such database object
     */
    @Transactional
    public <T> Object delete(Student student) {
        Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.students WHERE course = ?1 AND team_name = ?2 AND email = ?3");
        query.setParameter(1, student.getCourse());
        query.setParameter(2, student.getTeam_name());
        query.setParameter(3, student.getEmail());
        query.executeUpdate();
        return new RestResponse(student.getEmail() + " has been removed from the database");
    }

    /**
     * List of all students from database.
     *
     * @return List of all Student objects
     * @throws DataAccessException
     */
    public List<Student> listReadAll() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students", Student.class);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    /**
     * List of all students on the given team.
     *
     * @param team_name name of the team to filter by
     * @return List of Student objects where student team is team_name
     * @throws DataAccessException
     */
    public List<Student> listReadByTeam(String course, String team_name) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE course = ?1 AND team_name = ?2", Student.class);
        query.setParameter(1, course);
        query.setParameter(2, team_name);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    public List<Student> listReadByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE course = ?1", Student.class);
        query.setParameter(1, course);
        List<Student> resultList = query.getResultList();
        return resultList;
    }


    public List<Student> listReadStudent(String course, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE course = ?1 AND email = ?2", Student.class);
        query.setParameter(1, course);
        query.setParameter(2, email);
        List<Student> resultList = query.getResultList();
        return resultList;

    }

    public List<Student> listReadSingleStudent(String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE course = ?1 AND team_name = ?2 AND email = ?3", Student.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List<Student> results = query.getResultList();
        return results;
    }
    


    /**
     * Adds students to the database.
     *
     * @param students List of Student objects to add
     * @return JSONObject of student information and success or failure messages.
     */
    public JSONObject listCreate(List<Student> students) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Student studentInput : students) {
            Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE course = ?1 AND team_name = ?2 AND email = ?3", Student.class);
            query.setParameter(1, studentInput.getCourse());
            query.setParameter(2, studentInput.getTeam_name());
            query.setParameter(3, studentInput.getEmail());
            List results = query.getResultList();
            if (!results.isEmpty()) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(studentInput.getEmail() + " already exists in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                studentInput.setEnabled();
                studentRepo.save(studentInput);
                try {
                    successArray.put(new JSONObject(ow.writeValueAsString(studentInput)));
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
     * Update database records for students.
     *
     * @param students List of Student objects to update
     * @return JSONObject of student information and success or failure messages.
     */
    public JSONObject listUpdate(List<Student> students) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Student studentInput : students) {
            Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE course = ?1 AND team_name = ?2 AND email = ?3", Student.class);
            query.setParameter(1, studentInput.getCourse());
            query.setParameter(2, studentInput.getTeam_name());
            query.setParameter(3, studentInput.getEmail());
            List results = query.getResultList();
            if (results.isEmpty()) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(studentInput.getEmail() + " does not exist in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                delete((Student)results.get(0));
                studentRepo.save(studentInput);
                try {
                    successArray.put(new JSONObject(ow.writeValueAsString(studentInput)));
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
     * Deletes all student records from database associated with this team.
     *
     * @param team object of team to filter by
     * @return RestResponse indicating success or failure
     */
    public <T> Object deleteByTeam(Team team) {
        Query preQuery = getEntityManager().createNativeQuery("SELECT * FROM cassess.students WHERE course = ?1 AND team_name = ?2 LIMIT 1", Student.class);
        preQuery.setParameter(1, team.getCourse());
        preQuery.setParameter(2, team.getTeam_name());
        List results = preQuery.getResultList();
        if (!results.isEmpty()) {
            Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.students WHERE course = ?1 AND team_name = ?2");
            query.setParameter(1, team.getCourse());
            query.setParameter(2, team.getTeam_name());
            query.executeUpdate();
            return new RestResponse("All students in team " + team.getTeam_name() + " have been removed from the database");
        } else {
            return new RestResponse("No students in project " + team.getTeam_name() + " exist in the database");
        }
    }

    public List<CourseList> listGetCoursesForStudent(String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT course FROM cassess.students WHERE email = ?1", CourseList.class);
        query.setParameter(1, email);
        List<CourseList> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<TeamNames> listGetAssignedTeams(String email, String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT team_name AS 'team' FROM cassess.students WHERE email = ?1 AND course = ?2", TeamNames.class);
        query.setParameter(1, email);
        query.setParameter(2, course);
        List<TeamNames> resultList = query.getResultList();
        return resultList;
    }

}