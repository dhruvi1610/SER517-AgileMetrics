package edu.asu.cassess.dao.slack;

import edu.asu.cassess.dao.CAssessDAO;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.slack.SlackAuth;
import edu.asu.cassess.persist.entity.slack.UserInfo;
import edu.asu.cassess.persist.entity.slack.UserList;
import edu.asu.cassess.persist.entity.slack.UserObject;
import edu.asu.cassess.service.rest.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional
public class ConsumeUsers implements IConsumeUsers {

    private RestTemplate restTemplate;
    private String baseURL;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private IUserObjectQueryDao userObjectQueryDao;

    @Autowired
    private CAssessDAO dao;

    public ConsumeUsers() {
        restTemplate = new RestTemplate();
        baseURL = "https://slack.com/api/";
    }

    @Override
    public UserList getUserList(String token) {
        String ulURL = baseURL + "users.list?token=" + token;
        UserList ul = restTemplate.getForObject(ulURL, UserList.class);
        return ul;
    }

    @Override
    public void saveUserList(UserList userList, String course) {
        userList.getMembers();
        for(UserObject user:userList.getMembers()){
            user.setCourse(course);
            dao.save(user);
        }
    }

    @Override
    public void updateSlackUsers(String course){
        Course tempCourse = (Course) courseService.read(course);
        String token = tempCourse.getSlack_token();
        if(token != null){
            UserList userList = getUserList(token);
            if(userList != null) {
                saveUserList(userList, course);
            }
        }
    }

    @Override
    public void deleteSlackUsers(String course){
        Course tempCourse = (Course) courseService.read(course);
        List<Team> teams = tempCourse.getTeams();
        for(Team team:teams){
            List<Student> students = team.getStudents();
            for(Student student:students){
                userObjectQueryDao.deleteUserByEmail(course, student.getEmail());
            }
        }
    }

    @Override
    public UserObject getUserInfo(String userID, String token) {
        String uidURL = baseURL + "users.info?token=" + token + "&user=" + userID;
        System.out.println("Fetching from " + uidURL);
        UserInfo retUser = restTemplate.getForObject(uidURL, UserInfo.class);
        System.out.println("Some info from data storage class " + retUser.getUser().getName());
        return dao.save(retUser.getUser());
    }

}

