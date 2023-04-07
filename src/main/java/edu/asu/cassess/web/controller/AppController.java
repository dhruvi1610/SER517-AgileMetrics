package edu.asu.cassess.web.controller;

import edu.asu.cassess.dao.github.IGitHubCommitDataDao;
import edu.asu.cassess.dao.github.IGitHubCommitQueryDao;
import edu.asu.cassess.dao.github.IGitHubWeightDao;
import edu.asu.cassess.dao.github.IGitHubWeightQueryDao;
import edu.asu.cassess.dao.slack.IConsumeUsers;
import edu.asu.cassess.dao.slack.ISlackMessageTotalsQueryDao;
import edu.asu.cassess.dao.taiga.IMemberQueryDao;
import edu.asu.cassess.dao.taiga.IProjectQueryDao;
import edu.asu.cassess.dao.taiga.ITaskTotalsQueryDao;
import edu.asu.cassess.model.Taiga.*;
import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.model.slack.DailyMessageTotals;
import edu.asu.cassess.model.slack.WeeklyMessageTotals;
import edu.asu.cassess.persist.entity.github.CommitData;
import edu.asu.cassess.persist.entity.github.GitHubWeight;
import edu.asu.cassess.persist.entity.rest.Admin;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.security.User;
import edu.asu.cassess.persist.entity.taiga.TaigaSprint;
import edu.asu.cassess.persist.entity.taiga.TaigaSprintDays;
import edu.asu.cassess.persist.repo.UserRepo;
import edu.asu.cassess.persist.repo.rest.StudentRepo;
import edu.asu.cassess.security.SecurityUtils;
import edu.asu.cassess.service.github.IGatherGitHubData;
import edu.asu.cassess.service.rest.*;
import edu.asu.cassess.service.security.IUserService;
import edu.asu.cassess.service.slack.IChannelHistoryService;
import edu.asu.cassess.service.taiga.IMembersService;
import edu.asu.cassess.service.taiga.IProjectService;
import edu.asu.cassess.service.taiga.ITaigaSprintService;
import edu.asu.cassess.service.taiga.ITaskDataService;
import io.swagger.annotations.Api;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

@Transactional
@RestController
@Api(description = "Internal Calls API")
public class AppController {

    @Autowired
    private IConsumeUsers consumeUsers;

    @Autowired
    private IGitHubWeightQueryDao gitHubWeightQueryDao;

    @Autowired
    private IChannelHistoryService channelHistoryService;

    @Autowired
    private ISlackMessageTotalsQueryDao slackMessageTotalsService;

    @Autowired
    private IGitHubCommitQueryDao gitHubQueryDao;

    @Autowired
    private ITaskTotalsQueryDao taskTotalService;

    @Autowired
    private ITaskDataService taskService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private IGitHubWeightDao weightDao;

    @Autowired
    private IGitHubCommitDataDao commitDao;

    @Autowired
    private IGatherGitHubData gatherData;

    @EJB
    private ICourseService courseService;

    @EJB
    private ITeamsService teamService;

    @EJB
    private IStudentsService studentService;

    @EJB
    private IAdminsService adminService;

    @EJB
    private IUserService usersService;

    @EJB
    private IChannelService channelService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private StudentRepo studentRepo;

    @EJB
    private IProjectQueryDao projectDao;

    @EJB
    private IMemberQueryDao memberDao;

    @EJB
    private IMembersService members;

    @EJB
    private IProjectService projects;

    @Autowired
    private ITaigaSprintService taigaSprintService;

    //New Query Based method to retrieve the current User object, associated with the current login
    @ResponseBody
    @GetMapping(value = "/current_user")
    public User getCurrentUser(HttpServletRequest request, HttpServletResponse response) {
        return securityUtils.getCurrentUser();
    }

    //New Query Based Method to get the course list for which an admin is assigned to
    @ResponseBody
    @GetMapping(value = "/admin_courses")
    public List<CourseList> getAdminCourses(@RequestHeader(name = "email", required = true) String email,
        HttpServletRequest request, HttpServletResponse response) {
        return adminService.listGetCoursesForAdmin(email);

    }

    @ResponseBody
    @GetMapping(value = "/course_students")
    public List<Student> getCourseStudents(@RequestHeader(name = "course", required = true) String course,
        HttpServletRequest request, HttpServletResponse response) {
        return studentService.listReadByCourse(course);

    }

    //Previous Query Based method to obtain Teams assigned to a particular course

    //Gets the Teams/Projects which are assigned to a course
    @ResponseBody
    @GetMapping(value = "/course_teams")
    public ResponseEntity<List<TeamNames>> getCourseTeams(@RequestHeader(name = "course", required = true) String course,
        HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>(teamService.listGetTeamNames(course), HttpStatus.OK);
    }

    //Previous Query Based method to obtain Students assigned to a particular team/project
    @ResponseBody
    @GetMapping(value = "/team_students")
    public ResponseEntity<List<Student>> getTeamStudents(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>(studentService.listReadByTeam(course, team), HttpStatus.OK);
    }

    //New Query Based Methods to get the courses and projects lists for which a student is assigned to

    //Gets courses which the student is assigned to
    @ResponseBody
    @GetMapping(value = "/student_courses")
    public List<CourseList> getStudentCourses(@RequestHeader(name = "email", required = true) String email,
        HttpServletRequest request, HttpServletResponse response) {
        return studentService.listGetCoursesForStudent(email);

    }

    //Gets Teams assigned to a student
    @ResponseBody
    @GetMapping(value = "/student_teams")
    public List<TeamNames> getAssignedTeams(@RequestHeader(name = "email", required = true) String email,
        @RequestHeader(name = "course", required = true) String course,
        HttpServletRequest request, HttpServletResponse response) {

        return studentService.listGetAssignedTeams(email, course);
    }

    @ResponseBody
    @GetMapping(value = "/student_data")
    public List<Student> getStudent(@RequestHeader(name = "email", required = true) String email,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "course", required = true) String course,
        HttpServletRequest request, HttpServletResponse response) {

        return studentService.listReadSingleStudent(course, team, email);
    }


    //End of New Student Course and Project list methods


    //---------------------------------------------------------------------------------------------


    //New Taiga Charting Query Based Methods for Sprint 4

    //Daily task totals for a student
    @ResponseBody
    @GetMapping(value = "/taiga/student_tasks")
    public ResponseEntity<List<DailyTaskTotals>> getStudentTasks(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
        @RequestHeader(name = "weekEnding", required = true) long weekEnding, HttpServletRequest request, HttpServletResponse response) {

        Date dateBegin = new Date(weekBeginning * 1000L); // *1000 is to convert seconds to milliseconds
        Date dateEnd = new Date(weekEnding * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfBegin.format(dateEnd);
        List<DailyTaskTotals> tasksList = (List<DailyTaskTotals>) taskTotalService.getDailyTasksByStudent(formattedDateBegin, formattedDateEnd, course, team, email);
        return new ResponseEntity<List<DailyTaskTotals>>(tasksList, HttpStatus.OK);
    }

    //Daily task totals for a project
    @ResponseBody
    @GetMapping(value = "/taiga/team_tasks")
    public ResponseEntity<List<DailyTaskTotals>> getAverageTeamTasks(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
        @RequestHeader(name = "weekEnding", required = true) long weekEnding, HttpServletRequest request, HttpServletResponse response) {
        Date dateBegin = new Date(weekBeginning * 1000L); // *1000 is to convert seconds to milliseconds
        Date dateEnd = new Date(weekEnding * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfBegin.format(dateEnd);
        //System.out.print("-------------------------------------------------------------DateBeginning: " + formattedDateBegin);
        //System.out.print("-------------------------------------------------------------DateEnd: " + formattedDateEnd);
        List<DailyTaskTotals> tasksList = (List<DailyTaskTotals>) taskTotalService.getDailyTasksByTeam(formattedDateBegin, formattedDateEnd, course, team);
        return new ResponseEntity<List<DailyTaskTotals>>(tasksList, HttpStatus.OK);
    }

    //Daily task totals for a course
    @ResponseBody
    @GetMapping(value = "/taiga/course_tasks")
    public ResponseEntity<List<DailyTaskTotals>> getAverageCourseTasks(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
        @RequestHeader(name = "weekEnding", required = true) long weekEnding,
        HttpServletRequest request, HttpServletResponse response) {
        Date dateBegin = new Date(weekBeginning * 1000L); // *1000 is to convert seconds to milliseconds
        Date dateEnd = new Date(weekEnding * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfBegin.format(dateEnd);
        //System.out.print("-------------------------------------------------------------DateBeginning: " + formattedDateBegin);
        //System.out.print("-------------------------------------------------------------DateEnd: " + formattedDateEnd);
        List<DailyTaskTotals> tasksList = (List<DailyTaskTotals>) taskTotalService.getDailyTasksByCourse(formattedDateBegin, formattedDateEnd, course);
        return new ResponseEntity<List<DailyTaskTotals>>(tasksList, HttpStatus.OK);
    }

    //Weekly Activity for a student
    @ResponseBody
    @GetMapping(value = "/taiga/student_activity")
    public ResponseEntity<List<WeeklyActivity>> getStudentActivity(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyActivity> activityList = (List<WeeklyActivity>) taskTotalService.getWeeklyUpdatesByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyActivity>>(activityList, HttpStatus.OK);
    }

    //Weekly Activity for a team
    @ResponseBody
    @GetMapping(value = "/taiga/team_activity")
    public ResponseEntity<List<WeeklyActivity>> getTeamActivity(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyActivity> activityList = (List<WeeklyActivity>) taskTotalService.getWeeklyUpdatesByTeam(course, team);
        return new ResponseEntity<List<WeeklyActivity>>(activityList, HttpStatus.OK);
    }

    @GetMapping(value = "/taiga/sprint-days/{sprintId}")
    public ResponseEntity<List<TaigaSprintDays>> getTaigaSprints(@PathVariable("sprintId") Long sprintId) {
        List<TaigaSprintDays> taigaSprints = taigaSprintService.getBurndownDataBySprintId(sprintId);
        return new ResponseEntity<>(taigaSprints, HttpStatus.OK);
    }

    @GetMapping(value = "/taiga/sprints")
    public ResponseEntity<List<TaigaSprint>> getTaigaSprintNames(@RequestHeader(name = "courseName", required = true) String courseName, @RequestHeader(name = "teamName", required = true) String teamName) {
        List<TaigaSprint> taigaSprints = taigaSprintService.getSprintNames(courseName, teamName);
        return new ResponseEntity<>(taigaSprints, HttpStatus.OK);
    }
    @GetMapping(value = "/getTeam")
    public ResponseEntity<Team> getTeam(@RequestHeader(name = "courseName", required = true) String courseName, @RequestHeader(name = "teamName", required = true) String teamName) {
        Team team = (Team) teamService.read(teamName, courseName);
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    //Weekly Activity for a course
    @ResponseBody
    @GetMapping(value = "/taiga/course_activity")
    public ResponseEntity<List<WeeklyActivity>> getCourseActivity(@RequestHeader(name = "course", required = true) String course,
        HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyActivity> activityList = (List<WeeklyActivity>) taskTotalService.getWeeklyUpdatesByCourse(course);
        return new ResponseEntity<List<WeeklyActivity>>(activityList, HttpStatus.OK);
    }

    //Weekly Intervals for a project
    @ResponseBody
    @GetMapping(value = "/taiga/course_intervals")
    public ResponseEntity<List<WeeklyIntervals>> getTaigaCourseIntervals(@RequestHeader(name = "course", required = true) String course,
        HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) taskTotalService.getWeeklyIntervalsByCourse(course);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Intervals for a project
    @ResponseBody
    @GetMapping(value = "/taiga/team_intervals")
    public ResponseEntity<List<WeeklyIntervals>> getTaigaTeamIntervals(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) taskTotalService.getWeeklyIntervalsByTeam(course, team);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Intervals for a student
    @ResponseBody
    @GetMapping(value = "/taiga/student_intervals")
    public ResponseEntity<List<WeeklyIntervals>> getTaigaStudentIntervals(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) taskTotalService.getWeeklyIntervalsByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly weights for a student
    @ResponseBody
    @GetMapping(value = "/taiga/student_weightFreq")
    public ResponseEntity<List<WeeklyFreqWeight>> getTaigaStudentWeightFreq(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) taskTotalService.weeklyWeightFreqByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Weekly average weights for a project
    @ResponseBody
    @GetMapping(value = "/taiga/team_weightFreq")
    public ResponseEntity<List<WeeklyFreqWeight>> getTaigaTeamWeight(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) taskTotalService.weeklyWeightFreqByTeam(course, team);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Weekly average weights for a course
    @ResponseBody
    @GetMapping(value = "/taiga/course_weightFreq")
    public ResponseEntity<List<WeeklyFreqWeight>> getTaigaCourseWeight(@RequestHeader(name = "course", required = true) String course,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) taskTotalService.weeklyWeightFreqByCourse(course);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week Taiga weight for a student
    @ResponseBody
    @GetMapping(value = "/taiga/student_quickweightFreq")
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekStudentWeightFreqTG(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) taskTotalService.twoWeekWeightFreqByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week Taiga average weight for a project
    @ResponseBody
    @GetMapping(value = "/taiga/team_quickweightFreq")
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekTeamWeightFreqTG(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) taskTotalService.twoWeekWeightFreqByTeam(course, team);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week Taiga average weight for a course
    @ResponseBody
    @GetMapping(value = "/taiga/course_quickweightFreq")
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekCourseWeightFreqTG(@RequestHeader(name = "course", required = true) String course,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) taskTotalService.twoWeekWeightFreqByCourse(course);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week GH weight for a student
    @ResponseBody
    @GetMapping(value = "/github/student_quickweightFreq")
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekStudentWeightFreqGH(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) gitHubQueryDao.getWeightFreqByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week GH average weight for a project
    @ResponseBody
    @GetMapping(value = "/github/team_quickweightFreq")
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekProjectWeightFreqGH(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) gitHubQueryDao.getWeightFreqByTeam(course, team);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week GH average weight for a course
    @ResponseBody
    @GetMapping(value = "/github/course_quickweightFreq")
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekCourseWeightFreqGH(@RequestHeader(name = "course", required = true) String course,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) gitHubQueryDao.getWeightFreqByCourse(course);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }
    //Weekly task status averages for a student
    @ResponseBody
    @GetMapping(value = "/taiga/student_average")
    public ResponseEntity<List<WeeklyAverages>> getStudentAverage(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.getWeeklyAverageByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Weekly task status averages for a project
    @ResponseBody
    @GetMapping(value = "/taiga/team_average")
    public ResponseEntity<List<WeeklyAverages>> getProjectAverage(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.getWeeklyAverageByTeam(course, team);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Weekly task status averages for a course
    @ResponseBody
    @GetMapping(value = "/taiga/course_average")
    public ResponseEntity<List<WeeklyAverages>> getCourseAverage(@RequestHeader(name = "course", required = true) String course,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.getWeeklyAverageByCourse(course);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Current and last week task status averages for a student
    @ResponseBody
    @GetMapping(value = "/taiga/student_quickaverage")
    public ResponseEntity<List<WeeklyAverages>> lastTwoWeekStudentAverage(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.lastTwoWeekAveragesByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Current and last week task status averages for a project
    @ResponseBody
    @GetMapping(value = "/taiga/team_quickaverage")
    public ResponseEntity<List<WeeklyAverages>> lastTwoWeekProjectAverage(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.lastTwoWeekAveragesByTeam(course, team);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Current and last week task status averages for a course
    @ResponseBody
    @GetMapping(value = "/taiga/course_quickaverage")
    public ResponseEntity<List<WeeklyAverages>> lastTwoWeekCourseAverage(@RequestHeader(name = "course", required = true) String course,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.lastTwoWeekAveragesByCourse(course);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }


    //End of New Taiga Charting Methods for Sprint 4

    //-----------------------------------------------------------------------------------


    //Previous Query Based method to obtain the courses currently in the Database
    //For Admins not assigned to a particular course, but system-Admins
    @GetMapping(value = "/taigaCourses")
    public ResponseEntity<List<CourseList>> getCourses(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = (List<CourseList>) courseService.listGetCourses();
        return new ResponseEntity<List<CourseList>>(courseList, HttpStatus.OK);
    }


    @PostMapping(value = "/taiga/Update/Projects")
    public void updateTaigaProjects(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = courseService.listGetCourses();
        for (CourseList course : courseList) {
            //System.out.print("Course: " + course.getCourse());
            projects.updateProjects(course.getCourse());
        }
    }

    @PostMapping(value = "/taiga/Update/Memberships")
    public void updateTaigaMemberships(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = courseService.listGetCourses();
        for (CourseList course : courseList) {
            //System.out.print("Course: " + course.getCourse());
            members.updateMembership(course.getCourse());
        }
    }

    @PostMapping(value = "/taiga/Update/Tasks")
    public void updateTaigaTasks(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = courseService.listGetCourses();
        for (CourseList course : courseList) {
            //System.out.print("Course: " + course.getCourse());
            taskService.updateTaskTotals(course.getCourse());
        }
    }

    //---------- Slack Routes -----------------

    @PostMapping(value = "/slack/update_users")
    public void updateSlackUses(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = courseService.listGetCourses();
        for (CourseList course : courseList) {
            //System.out.print("Course: " + course.getCourse());
            consumeUsers.updateSlackUsers(course.getCourse());
        }
    }

    @PostMapping(value = "/slack/update_messageTotals")
    public void updateSlackMessageTotals(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = courseService.listGetCourses();
        for (CourseList course : courseList) {
            //System.out.print("Course: " + course.getCourse());
            channelHistoryService.updateMessageTotals(course.getCourse());
        }
    }

    //------------- Slack Charting Methods --------------

    //Daily Message Counts for a course
    @ResponseBody
    @GetMapping(value = "/slack/course_messages")
    public ResponseEntity<List<DailyMessageTotals>> getSlackCourseMessageCounts(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
        @RequestHeader(name = "weekEnding", required = true) long weekEnding, HttpServletRequest request, HttpServletResponse response) {

        Date dateBegin = new Date(weekBeginning * 1000L); // *1000 is to convert seconds to milliseconds
        Date dateEnd = new Date(weekEnding * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfBegin.format(dateEnd);
        //System.out.print("-------------------------------------------------------------DateBeginning: " + formattedDateBegin);
        //System.out.print("-------------------------------------------------------------DateEnd: " + formattedDateEnd);
        List<DailyMessageTotals> countList = (List<DailyMessageTotals>) slackMessageTotalsService.getDailyCountsByCourse(formattedDateBegin, formattedDateEnd, course);
        return new ResponseEntity<List<DailyMessageTotals>>(countList, HttpStatus.OK);
    }

    //Daily Message Counts for a team
    @ResponseBody
    @GetMapping(value = "/slack/team_messages")
    public ResponseEntity<List<DailyMessageTotals>> getSlackTeamMessageCounts(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
        @RequestHeader(name = "weekEnding", required = true) long weekEnding, HttpServletRequest request, HttpServletResponse response) {

        Date dateBegin = new Date(weekBeginning * 1000L); // *1000 is to convert seconds to milliseconds
        Date dateEnd = new Date(weekEnding * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfBegin.format(dateEnd);
        //System.out.print("-------------------------------------------------------------DateBeginning: " + formattedDateBegin);
        //System.out.print("-------------------------------------------------------------DateEnd: " + formattedDateEnd);
        List<DailyMessageTotals> countList = (List<DailyMessageTotals>) slackMessageTotalsService.getDailyCountsByTeam(formattedDateBegin, formattedDateEnd, course, team);
        return new ResponseEntity<List<DailyMessageTotals>>(countList, HttpStatus.OK);
    }

    //Daily Message Counts for a student
    @ResponseBody
    @GetMapping(value = "/slack/student_messages")
    public ResponseEntity<List<DailyMessageTotals>> getSlackStudentMessageCounts(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
        @RequestHeader(name = "weekEnding", required = true) long weekEnding, HttpServletRequest request, HttpServletResponse response) {

        Date dateBegin = new Date(weekBeginning * 1000L); // *1000 is to convert seconds to milliseconds
        Date dateEnd = new Date(weekEnding * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfBegin.format(dateEnd);
        //System.out.print("-------------------------------------------------------------DateBeginning: " + formattedDateBegin);
        //System.out.print("-------------------------------------------------------------DateEnd: " + formattedDateEnd);
        List<DailyMessageTotals> countList = (List<DailyMessageTotals>) slackMessageTotalsService.getDailyCountsByStudent(formattedDateBegin, formattedDateEnd, course, team, email);
        return new ResponseEntity<List<DailyMessageTotals>>(countList, HttpStatus.OK);
    }

    //Weekly Intervals for a project
    @ResponseBody
    @GetMapping(value = "/slack/course_intervals")
    public ResponseEntity<List<WeeklyIntervals>> getSlackCourseIntervals(@RequestHeader(name = "course", required = true) String course,
        HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) slackMessageTotalsService.getWeeklyIntervalsByCourse(course);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Intervals for a project
    @ResponseBody
    @GetMapping(value = "/slack/team_intervals")
    public ResponseEntity<List<WeeklyIntervals>> getSlackTeamIntervals(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) slackMessageTotalsService.getWeeklyIntervalsByTeam(course, team);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Intervals for a student
    @ResponseBody
    @GetMapping(value = "/slack/student_intervals")
    public ResponseEntity<List<WeeklyIntervals>> getSlackStudentIntervals(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) slackMessageTotalsService.getWeeklyIntervalsByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Message Totals for a student
    @ResponseBody
    @GetMapping(value = "/slack/student_totals")
    public ResponseEntity<List<WeeklyMessageTotals>> getStudentMessageTotals(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyMessageTotals> activityList = (List<WeeklyMessageTotals>) slackMessageTotalsService.getWeeklyTotalsByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyMessageTotals>>(activityList, HttpStatus.OK);
    }

    //Weekly Message Totals for a team
    @ResponseBody
    @GetMapping(value = "/slack/team_totals")
    public ResponseEntity<List<WeeklyMessageTotals>> getTeamMessageTotals(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyMessageTotals> totalsList = (List<WeeklyMessageTotals>) slackMessageTotalsService.getWeeklyTotalsByTeam(course, team);
        return new ResponseEntity<List<WeeklyMessageTotals>>(totalsList, HttpStatus.OK);
    }

    //Weekly Message Totals for a course
    @ResponseBody
    @GetMapping(value = "/slack/course_totals")
    public ResponseEntity<List<WeeklyMessageTotals>> getCourseMessageTotals(@RequestHeader(name = "course", required = true) String course,
        HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyMessageTotals> totalsList = (List<WeeklyMessageTotals>) slackMessageTotalsService.getWeeklyTotalsByCourse(course);
        return new ResponseEntity<List<WeeklyMessageTotals>>(totalsList, HttpStatus.OK);
    }

    //Weekly weights/frequencies for a student
    @ResponseBody
    @GetMapping(value = "/slack/student_weightFreq")
    public ResponseEntity<List<WeeklyFreqWeight>> getSlackStudentWeightFreq(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) slackMessageTotalsService.weeklyWeightFreqByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Weekly average weights/frequencies for a project
    @ResponseBody
    @GetMapping(value = "/slack/team_weightFreq")
    public ResponseEntity<List<WeeklyFreqWeight>> getSlackTeamWeightFreq(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) slackMessageTotalsService.weeklyWeightFreqByTeam(course, team);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Weekly average weights/frequencies for a course
    @ResponseBody
    @GetMapping(value = "/slack/course_weightFreq")
    public ResponseEntity<List<WeeklyFreqWeight>> getSlackCourseWeightFreq(@RequestHeader(name = "course", required = true) String course,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) slackMessageTotalsService.weeklyWeightFreqByCourse(course);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week Slack weight/frequency for a student
    @ResponseBody
    @GetMapping(value = "/slack/student_quickweightFreq")
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekStudentWeightFreqSK(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) slackMessageTotalsService.twoWeekWeightFreqByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week Slack average weight/frequency for a project
    @ResponseBody
    @GetMapping(value = "/slack/team_quickweightFreq")
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekTeamWeightFreqSK(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) slackMessageTotalsService.twoWeekWeightFreqByTeam(course, team);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week Taiga average weight/frequency for a course
    @ResponseBody
    @GetMapping(value = "/slack/course_quickweightFreq")
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekCourseWeightFreqSK(@RequestHeader(name = "course", required = true) String course,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) slackMessageTotalsService.twoWeekWeightFreqByCourse(course);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //---------- GitHub Routes --------------

    //GET the weights for the selected email
    @GetMapping(value = "/github/weight")
    public ResponseEntity<List<GitHubWeight>> getGitHubWeight(@RequestHeader(name = "email") String email,
        HttpServletRequest request, HttpServletResponse response) {
        List<GitHubWeight> weightList = weightDao.getWeightByEmail(email);
        return new ResponseEntity<List<GitHubWeight>>(weightList, HttpStatus.OK);
    }

    @PostMapping(value = "/github/weight")
    public void updateGitHubData(HttpServletRequest request, HttpServletResponse response) {
        List<Team> teams = teamService.listReadAll();
        for (Team team : teams) {
            Course course = (Course) courseService.read(team.getCourse());
            gatherData.fetchData(team.getGithub_owner(), team.getGithub_repo_id(), course.getCourse(), team.getTeam_name(), team.getGithub_token());
        }
    }

    //GET the commits for the selected email
    @GetMapping(value = "/github/commits")
    public ResponseEntity<List<CommitData>> getGitHubCommits(@RequestHeader(name = "email") String email,
        HttpServletRequest request, HttpServletResponse response) {
        List<CommitData> commitList = commitDao.getCommitByEmail(email);
        return new ResponseEntity<List<CommitData>>(commitList, HttpStatus.OK);
    }

    //GET the commits for the selected email
    @GetMapping(value = "/github/commits_course")
    public ResponseEntity<List<CommitData>> getGitHubCommitsByCourse(@RequestHeader(name = "course") String course,
        @RequestHeader(name = "weekBeginning",required = false) long weekBeginning,
        @RequestHeader(name = "weekEnding", required = false) long weekEnding,
        HttpServletRequest request, HttpServletResponse response) {
        Date dateBegin = new Date(weekBeginning * 1000L); // *1000 is to convert seconds to milliseconds
        Date dateEnd = new Date(weekEnding * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfEnd.format(dateEnd);
        List<CommitData> commitList = gitHubQueryDao.getCommitsByCourse(course, formattedDateBegin, formattedDateEnd);
        return new ResponseEntity<List<CommitData>>(commitList, HttpStatus.OK);
    }

    //GET the commits for the selected email
    @GetMapping(value = "/github/commits_team")
    public ResponseEntity<List<CommitData>> getGitHubCommitsByTeam(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
        @RequestHeader(name = "weekEnding", required = true) long weekEnding,
        HttpServletRequest request, HttpServletResponse response) {
        Date dateBegin = new Date(weekBeginning * 1000L); // *1000 is to convert seconds to milliseconds
        Date dateEnd = new Date(weekEnding * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfEnd.format(dateEnd);
        List<CommitData> commitList = gitHubQueryDao.getCommitsByTeam(course, team, formattedDateBegin, formattedDateEnd);
        return new ResponseEntity<List<CommitData>>(commitList, HttpStatus.OK);
    }

    //GET the commits for the selected email
    @GetMapping(value = "/github/commits_student")
    public ResponseEntity<List<CommitData>> getGitHubCommitsByStudent(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
        @RequestHeader(name = "weekEnding", required = true) long weekEnding,
        HttpServletRequest request, HttpServletResponse response) {
        Date dateBegin = new Date(weekBeginning * 1000L); // *1000 is to convert seconds to milliseconds
        Date dateEnd = new Date(weekEnding * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfBegin.format(dateEnd);
        List<CommitData> commitList = gitHubQueryDao.getCommitsByStudent(course, team, email,formattedDateBegin, formattedDateEnd);
        return new ResponseEntity<List<CommitData>>(commitList, HttpStatus.OK);
    }

    //Weekly Intervals for a project
    @ResponseBody
    @GetMapping(value = "/github/course_intervals")
    public ResponseEntity<List<WeeklyIntervals>> getGithubCourseIntervals(@RequestHeader(name = "course", required = true) String course,
        HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) gitHubQueryDao.getWeeklyIntervalsByCourse(course);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Intervals for a project
    @ResponseBody
    @GetMapping(value = "/github/team_intervals")
    public ResponseEntity<List<WeeklyIntervals>> getGithubTeamIntervals(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) gitHubQueryDao.getWeeklyIntervalsByTeam(course, team);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Intervals for a student
    @ResponseBody
    @GetMapping(value = "/github/student_intervals")
    public ResponseEntity<List<WeeklyIntervals>> getGithubStudentIntervals(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) gitHubQueryDao.getWeeklyIntervalsByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //GET the commits for the selected email
    @GetMapping(value = "/github/weights_course")
    public ResponseEntity<List<GitHubWeight>> getGitHubWeightsByCourse(@RequestHeader(name = "course") String course,
        HttpServletRequest request, HttpServletResponse response) {
        List<GitHubWeight> weightList = gitHubWeightQueryDao.getWeightsByCourse(course);
        return new ResponseEntity<List<GitHubWeight>>(weightList, HttpStatus.OK);
    }

    //GET the commits for the selected email
    @GetMapping(value = "/github/weights_team")
    public ResponseEntity<List<GitHubWeight>> getGitHubWeightsByTeam(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        HttpServletRequest request, HttpServletResponse response) {
        List<GitHubWeight> weightList = gitHubWeightQueryDao.getWeightsByTeam(course, team);
        return new ResponseEntity<List<GitHubWeight>>(weightList, HttpStatus.OK);
    }

    //GET the commits for the selected email
    @GetMapping(value = "/github/weights_student")
    public ResponseEntity<List<GitHubWeight>> getGitHubWeightsByStudent(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        HttpServletRequest request, HttpServletResponse response) {
        List<GitHubWeight> weightList = gitHubWeightQueryDao.getWeightsByStudent(course, team, email);
        return new ResponseEntity<List<GitHubWeight>>(weightList, HttpStatus.OK);
    }
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/studentProfileDelTeam", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object deleteStudentFromGUITeam(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        HttpServletRequest request, HttpServletResponse response) {
        if (email != null) {
            User user = userRepo.findByEmail(email);
            Object object = studentService.find(email, team, course);
            Student student = new Student();
            if (object.getClass() == Student.class) {
                student = (Student) object;
                usersService.deleteUser(user);
                taskTotalService.deleteTaskTotalsByStudent(student);
                gitHubWeightQueryDao.deleteWeightsByStudent(student);
                gitHubQueryDao.deleteCommitsByStudent(student);
                slackMessageTotalsService.deleteMessagesByStudent(student);
                memberDao.deleteMembersByStudent(student);
                response.setStatus(HttpServletResponse.SC_OK);
                return studentService.delete(student);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/studentProfileDelCourse", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> void deleteStudentFromGUICourse(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "email", required = true) String email,
        HttpServletRequest request, HttpServletResponse response) {
        if (email != null) {
            List<Student> students = studentService.listReadStudent(course, email);
            if (!students.isEmpty()) {
                {
                    for (Student student : students) {
                        taskTotalService.deleteTaskTotalsByStudent(student);
                        gitHubWeightQueryDao.deleteWeightsByStudent(student);
                        gitHubQueryDao.deleteCommitsByStudent(student);
                        slackMessageTotalsService.deleteMessagesByStudent(student);
                        memberDao.deleteMembersByStudent(student);
                        studentService.delete(student);
                    }
                }
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/adminProfileDelete", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object deleteAdminFromGUI(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "email", required = true) String email,
        HttpServletRequest request, HttpServletResponse response) {
        if (email != null) {
            Object object = adminService.find(email, course);
            Admin admin = new Admin();
            if (object.getClass() == Admin.class) {
                admin = (Admin) object;
                response.setStatus(HttpServletResponse.SC_OK);
                return adminService.delete(admin);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/userProfileDelete", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object deleteUserFromGUI(@RequestHeader(name = "email", required = true) String email,
        HttpServletRequest request, HttpServletResponse response) {
        if (email != null) {
            User user = userRepo.findByEmail(email);
            response.setStatus(HttpServletResponse.SC_OK);
            return usersService.deleteUser(user);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/studentDisable", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object disableStudentFromGUI(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        HttpServletRequest request, HttpServletResponse response) {
        Object object = studentService.find(email, team, course);
        Student student = new Student();
        if (object.getClass() == Student.class) {
            student = (Student) object;
            student.setDisabled();
            studentRepo.save(student);
        }
        return object;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/studentEnable", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object enableStudentFromGUI(@RequestHeader(name = "course", required = true) String course,
        @RequestHeader(name = "team", required = true) String team,
        @RequestHeader(name = "email", required = true) String email,
        HttpServletRequest request, HttpServletResponse response) {
        Object object = studentService.find(email, team, course);
        Student student = new Student();
        if (object.getClass() == Student.class) {
            student = (Student) object;
            student.setEnabled();
            studentRepo.save(student);
        }
        return object;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/userDisable", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object disableUserFromGUI(@RequestHeader(name = "email", required = true) String email,
        HttpServletRequest request, HttpServletResponse response) {
        Object object = userRepo.findByEmail(email);
        User user = new User();
        if (object.getClass() == User.class) {
            user = (User) object;
            user.setDisabled();
            userRepo.save(user);
        }
        return object;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/userEnable", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object enableUserFromGUI(@RequestHeader(name = "email", required = true) String email,
        HttpServletRequest request, HttpServletResponse response) {
        Object object = userRepo.findByEmail(email);
        User user = new User();
        if (object.getClass() == User.class) {
            user = (User) object;
            user.setEnabled();
            userRepo.save(user);
        }
        return object;
    }
}