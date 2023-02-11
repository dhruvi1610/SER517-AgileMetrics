package edu.asu.cassess.web.controller;

import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.persist.entity.rest.Admin;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.security.Token;
import edu.asu.cassess.persist.entity.security.User;
import edu.asu.cassess.persist.repo.TokenRepo;
import edu.asu.cassess.persist.repo.UserRepo;
import edu.asu.cassess.security.SecurityUtils;
import edu.asu.cassess.service.rest.AdminsService;
import edu.asu.cassess.service.rest.StudentsService;
import edu.asu.cassess.service.rest.TeamsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Api(description = "Users management API")
public class SecurityController {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AdminsService adminsService;

    @Autowired
    private StudentsService studentsService;

    @Autowired
    private TeamsService teamsService;

    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ResponseBody
    @RequestMapping(value = "/check_profileaccess", method = RequestMethod.GET)
    public boolean checkProfileAccess(@RequestHeader(name = "login", required = true) String login,
                                      @RequestHeader(name = "auth", required = true) String auth,
                                      @RequestHeader(name = "email", required = true) String email,
                                      HttpServletRequest request, HttpServletResponse response) {
        Boolean bool = false;
        //System.out.println(course);
        //System.out.println(login);
        //System.out.println(auth);
        if (auth.equalsIgnoreCase("super_user")) {
            bool = true;
        } else if (login.equalsIgnoreCase(email)) {
            bool = true;
        }else if(auth.equalsIgnoreCase("admin") && !login.equalsIgnoreCase(email)){
                List<CourseList> coursesStudent = studentsService.listGetCoursesForStudent(email);
                List<CourseList> coursesAdmin = adminsService.listGetCoursesForAdmin(login);
                for(CourseList courseAdmin:coursesAdmin){
                    for(CourseList courseStudent:coursesStudent){
                        if(courseAdmin.getCourse().equalsIgnoreCase(courseStudent.getCourse())){
                            bool = true;
                        }
                    }
                }
        } else {
            bool = false;
        }
        return bool;
    }


    @ResponseBody
    @RequestMapping(value = "/check_courseaccess", method = RequestMethod.GET)
    public boolean checkCourseAccess(@RequestHeader(name = "course", required = true) String course,
                                     @RequestHeader(name = "login", required = true) String login,
                                     @RequestHeader(name = "auth", required = true) String auth,
                                     HttpServletRequest request, HttpServletResponse response) {
        Boolean bool = false;
        //System.out.println(course);
        //System.out.println(login);
        //System.out.println(auth);
        if (auth.equalsIgnoreCase("super_user")) {
            bool = true;
        } else if (auth.equalsIgnoreCase("admin")) {
            //System.out.println("---------------------------------!!!!!!!!!!!!!!!!   IS ADMIN------");
            Object object = adminsService.find(login, course);
            if (object.getClass() != Admin.class) {
                bool = false;
            } else {
                bool = true;
            }
        }
        if (auth.equalsIgnoreCase("student")) {
            //System.out.println("---------------------------------!!!!!!!!!!!!!!!!   IS STUDENT------");
            Object object = studentsService.find(login, course);
            if (object.getClass() != Student.class) {
                bool = false;
            } else {
                bool = true;
            }
        }
        return bool;
    }

    @ResponseBody
    @RequestMapping(value = "/check_teamaccess", method = RequestMethod.GET)
    public boolean checkTeamAccess(@RequestHeader(name = "course", required = true) String course,
                                   @RequestHeader(name = "team", required = true) String team,
                                   @RequestHeader(name = "login", required = true) String login,
                                   @RequestHeader(name = "auth", required = true) String auth,
                                   HttpServletRequest request, HttpServletResponse response) {
        Boolean bool = false;
        //System.out.println(course);
        //System.out.println(team);
        //System.out.println(login);
        //System.out.println(auth);
        if (auth.equalsIgnoreCase("super_user")) {
            bool = true;
        } else if (auth.equalsIgnoreCase("admin")) {
            //System.out.println("---------------------------------!!!!!!!!!!!!!!!!   IS ADMIN------");
            Object object = adminsService.find(login, course);
            if (object.getClass() != Admin.class) {
                //System.out.println("---------------------------------!!!!!!!!!!!!!!!!   IS NOT ADMIN OBJECT------");
                bool = false;
            } else {
                //System.out.println("---------------------------------!!!!!!!!!!!!!!!!   IS ADMIN OBJECT------");
                Admin admin = (Admin) object;
                if (admin.getCourse().equalsIgnoreCase(course)) {
                    Object objectTeam = teamsService.findOne(team, course);
                    if (objectTeam.getClass() != Team.class) {
                        //System.out.println("---------------------------------!!!!!!!!!!!!!!!!   IS NOT TEAM------");
                        bool = false;
                    } else {
                        //System.out.println("---------------------------------!!!!!!!!!!!!!!!!   IS TEAM------");
                        bool = true;
                    }
                } else {
                    bool = false;
                }

            }
        }
        if (auth.equalsIgnoreCase("student")) {
            //System.out.println("---------------------------------!!!!!!!!!!!!!!!!   IS STUDENT------");
            Object object = studentsService.find(login, team, course);
            if (object.getClass() != Student.class) {
                bool = false;
            } else {
                bool = true;
            }
        }
        return bool;
    }

    @ResponseBody
    @RequestMapping(value = "/check_studentaccess", method = RequestMethod.GET)
    public boolean checkStudentAccess(@RequestHeader(name = "course", required = true) String course,
                                      @RequestHeader(name = "team", required = true) String team,
                                      @RequestHeader(name = "login", required = true) String login,
                                      @RequestHeader(name = "auth", required = true) String auth,
                                      @RequestHeader(name = "studentemail", required = true) String studentemail,
                                      @RequestHeader(name = "fullname", required = true) String fullname,
                                      HttpServletRequest request, HttpServletResponse response) {
        Boolean bool = false;
        //System.out.println(course);
        //System.out.println(team);
        //System.out.println(login);
        //System.out.println(studentemail);
        //System.out.println(fullname);
        //System.out.println(auth);
        if (auth.equalsIgnoreCase("super_user")) {
            bool = true;
        } else if (auth.equalsIgnoreCase("admin")) {
            Object object = adminsService.find(login, course);
            if (object.getClass() != Admin.class) {
                bool = false;
            } else {
                Admin admin = (Admin) object;
                if (admin.getCourse().equalsIgnoreCase(course)) {
                    Object objectTeam = teamsService.findOne(team, course);
                    if (objectTeam.getClass() != Team.class) {
                        bool = false;
                    } else {
                        Object objectStudent = studentsService.find(studentemail, team, course);
                        if (objectStudent.getClass() != Student.class) {
                            bool = false;
                        } else {
                            bool = true;
                        }
                    }
                } else {
                    bool = true;
                }
            }
        } else {
            bool = false;
        }

        if (auth.equalsIgnoreCase("student")) {
            User user = userRepo.findByLogin(login);
            String name = user.getFirstName() + " " + user.getFamilyName();
            //System.out.println(name);
            Object object = studentsService.find(login, team, course);
            if (object.getClass() != Student.class) {
                bool = false;
            } else {
                //System.out.println("-------------------------------!!!!!!!!!!!!!!!!!!!__________IS STUDENT IN TEAM AND COURSE");
                if (name.equalsIgnoreCase(fullname)) {
                    bool = true;
                    //System.out.println("-------------------------------!!!!!!!!!!!!!!!!!!!__________NAMES ARE EQUAL");
                } else {
                    //System.out.println("-------------------------------!!!!!!!!!!!!!!!!!!!__________NAMES NOT EQUAL");
                    bool = false;
                }
            }
        }
        return bool;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/userPasswordUpdate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object changeUserPassword(@RequestHeader(name = "login", required = true) String login,
                                  @RequestHeader(name = "pass", required = true) String pass,
                                  HttpServletRequest request, HttpServletResponse response) {
        if (login != null) {
            User user = userRepo.findByLogin(login);
            response.setStatus(HttpServletResponse.SC_OK);
            user.setPassword(passwordEncoder.encode(pass));
            return userRepo.save(user);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @RequestMapping(value = "/security/account", method = RequestMethod.GET)
    public
    @ResponseBody
    User getUserAccount() {
        User user = userRepo.findByLogin(SecurityUtils.getCurrentLogin());
        user.setPassword(null);
        return user;
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/security/tokens", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Token> getTokens() {
        List<Token> tokens = tokenRepo.findAll();
        for (Token t : tokens) {
            t.setSeries(null);
            t.setValue(null);
        }
        return tokens;
    }


}
