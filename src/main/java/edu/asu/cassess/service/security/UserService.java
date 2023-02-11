package edu.asu.cassess.service.security;

import edu.asu.cassess.dao.security.UserServiceDao;
import edu.asu.cassess.persist.entity.rest.Admin;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class UserService implements IUserService {

    @Autowired
    private UserServiceDao userServiceDao;

    @Override
    public <T> Object createUser(User userInput, long role) {
        return userServiceDao.createUser(userInput, role);
    }

    @Override
    public <T> Object createUsersByAdmins(List<Admin> admins) {
        return userServiceDao.createUsersByAdmins(admins);
    }

    @Override
    public User adminUser(Admin admin) {
        return userServiceDao.adminUser(admin);
    }

    @Override
    public <T> Object createUsersByStudents(List<Student> students) {
        return userServiceDao.createUsersByStudents(students);
    }

    @Override
    public User studentUser(Student student) {
        return userServiceDao.studentUser(student);
    }

    @Override
    public <T> Object registerUser(String first_name, String family_name, String email, String password, String role) {
        return userServiceDao.registerUser(first_name, family_name, email, password, role);
    }

    @Override
    public User updateStudent(Student student, User user) {
        return userServiceDao.updateStudent(student, user);
    }

    @Override
    public User updateAdmin(Admin admin, User user) {
        return userServiceDao.updateAdmin(admin, user);
    }

    @Override
    public User deleteUser(User user) {
        return userServiceDao.deleteUser(user);
    }

    @Override
    public Course courseDelete(Course course) {
        return userServiceDao.courseDelete(course);
    }

    @Override
    public Course courseUpdate(Course course) {
        return userServiceDao.courseUpdate(course);
    }

    @Override
    public Team teamUpdate(Team team) {
        return userServiceDao.teamUpdate(team);
    }

    @Override
    public Team teamDelete(Team team) {
        return userServiceDao.teamDelete(team);
    }

}
