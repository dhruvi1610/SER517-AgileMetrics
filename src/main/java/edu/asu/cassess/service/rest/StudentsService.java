package edu.asu.cassess.service.rest;

import edu.asu.cassess.dao.rest.StudentsServiceDao;
import edu.asu.cassess.dto.rest.StudentDTO;
import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.model.Taiga.TeamNames;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.ejb.EJB;
import java.util.Arrays;
import java.util.List;

@Service
public class StudentsService implements IStudentsService {

    @EJB
    private StudentsServiceDao studentsDao;

    @Override
    public <T> Object create(Student student) {

        return studentsDao.create(student);
    }

    @Override
    public <T> Object update(Student student) {

        return studentsDao.update(student);
    }

    @Override
    public <T> Object find(String email, String team, String course) {

        return studentsDao.find(email, team, course);
    }

    @Override
    public <T> Object findGitHubUser(String github_username, String team, String course) {

        return studentsDao.findGitHubUser(github_username, team, course);
    }

    @Override
    public <T> Object findTaigaUser(String taiga_username, String team, String course) {

        return studentsDao.findTaigaUser(taiga_username, team, course);
    }

    @Override
    public <T> Object findSlackUser(String slack_username, String team, String course) {

        return studentsDao.findSlackUser(slack_username, team, course);
    }


    @Override
    public <T> Object find(String email, String course) {

        return studentsDao.find(email, course);
    }

    @Override
    public <T> Object delete(Student student) {

        return studentsDao.delete(student);

    }

    @Override
    public <T> List<Student> listReadAll() {

        return studentsDao.listReadAll();

    }

    @Override
    public <T> List<Student> listReadByTeam(String course, String team_name) {
        return studentsDao.listReadByTeam(course, team_name);
    }

    @Override
    public <T> List<Student> listReadByCourse(String course) {
        return studentsDao.listReadByCourse(course);
    }

    @Override
    public List<Student> listReadStudent(String course, String email) throws DataAccessException {
        return studentsDao.listReadStudent(course, email);
    }

    @Override
    public List<Student> listReadSingleStudent(String course, String team, String email) throws DataAccessException {
        return studentsDao.listReadSingleStudent(course, team, email);
    }

    
    @Override
    public JSONObject listUpdate(List<Student> students) {

        return studentsDao.listUpdate(students);

    }

    @Override
    public JSONObject listCreate(List<Student> students) {

        return studentsDao.listCreate(students);

    }

    @Override
    public <T> Object deleteByTeam(Team team) {

        return studentsDao.deleteByTeam(team);

    }

    @Override
    public List<CourseList> listGetCoursesForStudent(String email) {
        return studentsDao.listGetCoursesForStudent(email);
    }

    @Override
    public List<TeamNames> listGetAssignedTeams(String email, String course) {
        return studentsDao.listGetAssignedTeams(email, course);
    }
// method for fetching the names of the students on canvas using team id and canvas access token
    @Override
    public List<StudentDTO> getStudentsCanvas(String canvasToken, long teamId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://canvas.asu.edu/api/v1/groups/"+teamId+"/users?per_page=10";
        HttpHeaders headers = new HttpHeaders();
        if(!canvasToken.equalsIgnoreCase("na")) {
            headers.setBearerAuth(canvasToken);
        }

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<StudentDTO[]> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, StudentDTO[].class);

        } catch (Exception e) {
            System.out.println("Canvas fetch stats failed. " + e.getMessage());
        }

        if(responseEntity != null && responseEntity.getBody() != null && !Arrays.toString(responseEntity.getBody()).startsWith("{}")) {
            return Arrays.asList(responseEntity.getBody());
        }
        return null;
    }

}
