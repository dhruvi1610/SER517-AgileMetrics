package edu.asu.cassess.service.taiga;

import edu.asu.cassess.dao.taiga.IMemberQueryDao;
import edu.asu.cassess.dao.taiga.IProjectQueryDao;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.taiga.*;
import edu.asu.cassess.persist.repo.taiga.TaskTotalsRepo;
import edu.asu.cassess.service.rest.CourseService;
import edu.asu.cassess.service.rest.ICourseService;
import edu.asu.cassess.service.rest.IStudentsService;
import edu.asu.cassess.service.rest.ITeamsService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TaskDataService implements ITaskDataService {

    class MutableInt {
        int value = 1; // note that we start at 1 since we're counting
        public void increment () { ++value;      }
        public int  get ()       { return value; }
    }

    private RestTemplate restTemplate;

    private String tasksListURL;

    private Map<String, MutableInt> closedMap = new HashMap<String, MutableInt>();
    private Map<String, MutableInt> newMap = new HashMap<String, MutableInt>();
    private Map<String, MutableInt> inProgressMap = new HashMap<String, MutableInt>();
    private Map<String, MutableInt> readyForTestMap = new HashMap<String, MutableInt>();


    @Autowired
    private TaskTotalsRepo TaskTotalsRepo;

    @Autowired
    private IMemberQueryDao MemberQueryDao;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private IProjectQueryDao projectService;

    @Autowired
    private IStudentsService studentsService;

    @Autowired
    private ITeamsService teamsService;

    public TaskDataService() {
        restTemplate = new RestTemplate();
        tasksListURL = "https://api.taiga.io/api/v1/tasks?project=";
    }

    @Override
    public TaskData[] getTasks(Long id, String token, int page) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        headers.add("x-disable-pagination", "True");

        //System.out.println("Page: " + page);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<TaskData[]> taskList = restTemplate.getForEntity(tasksListURL + id + "&page=" + page, TaskData[].class, request);

        TaskData[] tasks = taskList.getBody();

        for (TaskData task:tasks) {
            if (task.getAssignmentData() != null) {
                //System.out.println("-----------------------------****************************************User Name Display: " + task.getAssignmentData().getUsername());
                //System.out.println("-----------------------------****************************************ClosedMap " + closedMap.get(task.getAssignmentData().getUsername()));
                MutableInt closedCount = closedMap.get(task.getAssignmentData().getUsername());
                //System.out.println("-----------------------------****************************************NewMap " + newMap.get(task.getAssignmentData().getUsername()));
                MutableInt newCount = newMap.get(task.getAssignmentData().getUsername());
                //System.out.println("-----------------------------****************************************InProgressMap " + inProgressMap.get(task.getAssignmentData().getUsername()));
                MutableInt inProgressCount = inProgressMap.get(task.getAssignmentData().getUsername());
                //System.out.println("-----------------------------****************************************ReadyForTestMap " + readyForTestMap.get(task.getAssignmentData().getUsername()));
                MutableInt readyForTestCount = readyForTestMap.get(task.getAssignmentData().getUsername());

                if (task.getStatusData() != null) {
                    if (task.getStatusData().getName() != null) {
                        if (task.getStatusData().getName().equalsIgnoreCase("Closed")) {
                            if (closedCount == null) {
                                closedMap.put(task.getAssignmentData().getUsername(), new MutableInt());
                            } else {
                                closedCount.increment();
                            }
                        }

                        if (task.getStatusData().getName().equalsIgnoreCase("New")) {
                            if (newCount == null) {
                                newMap.put(task.getAssignmentData().getUsername(), new MutableInt());
                            } else {
                                newCount.increment();
                            }
                        }
                        //System.out.println("-----------------------------****************************************InProgressStatus " + task.getStatusData().getName().equalsIgnoreCase("In Progress"));
                        if (task.getStatusData().getName().equalsIgnoreCase("In Progress")) {
                            if (inProgressCount == null) {
                                inProgressMap.put(task.getAssignmentData().getUsername(), new MutableInt());
                            } else {
                                inProgressCount.increment();
                            }
                        }
                        //System.out.println("-----------------------------****************************************ReadyForTestStatus " + task.getStatusData().getName().equalsIgnoreCase("Ready For Test"));
                        if (task.getStatusData().getName().equalsIgnoreCase("Ready For Test")) {
                            if (readyForTestCount == null) {
                                readyForTestMap.put(task.getAssignmentData().getUsername(), new MutableInt());
                            } else {
                                readyForTestCount.increment();
                            }
                        }
                    }
                }
            }
        }
        //System.out.println("Headers Response" + taskList.getHeaders());

        if (taskList.getHeaders().containsKey("x-pagination-next")) {
            page++;
            return getTasks(id, token, page);
        } else {

            return tasks;
        }
    }

    @Override
    public void getTaskTotals(String slug, Course course, Team team) {
        List<Student> students = studentsService.listReadByTeam(course.getCourse(), team.getTeam_name());
        for (Student student : students) {
            String taiga_username = student.getTaiga_username();
            int closedTasks = 0;
            int newTasks = 0;
            int inProgressTasks = 0;
            int readyForTestTasks = 0;
            if (closedMap.get(taiga_username) != null) {
                closedTasks = closedMap.get(taiga_username).get();
            }
            if (newMap.get(taiga_username) != null) {
                newTasks = newMap.get(taiga_username).get();
            }
            if (inProgressMap.get(taiga_username) != null) {
                inProgressTasks = inProgressMap.get(taiga_username).get();
            }
            if (readyForTestMap.get(taiga_username) != null) {
                readyForTestTasks = readyForTestMap.get(taiga_username).get();
            }
            int openTasks = newTasks + inProgressTasks + readyForTestTasks;
            //System.out.println("----------------------------**********************************************=========User: " + taiga_username);
            //System.out.println("----------------------------**********************************************=========ClosedCount: " + closedTasks);
            //System.out.println("----------------------------**********************************************=========NewCount: " + newTasks);
            //System.out.println("----------------------------**********************************************=========inProgressCount: " + inProgressTasks);
            //System.out.println("----------------------------**********************************************=========readyForTestCount: " + readyForTestTasks);
            if (student.getEnabled() != null) {
                if (student.getEnabled() != false) {
                    TaskTotalsRepo.save(new TaskTotals(new TaskTotalsID(student.getEmail(), student.getTeam_name(), course.getCourse()), student.getFull_name(), taiga_username, slug, closedTasks, newTasks, inProgressTasks,
                            readyForTestTasks, openTasks));
                }
            }
        }
    }

    /* Method to obtain all task totals for members of a particular course,
        occurring on a schedule
     */
    @Override
    public void updateTaskTotals(String course) {
        //System.out.println("Updating Tasks");
        if (courseService == null) courseService = new CourseService();
        Course tempCourse = (Course) courseService.read(course);
        Date current = new Date();
        try {
            current = new SimpleDateFormat("yyyy-mm-dd").parse(LocalDate.now().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //System.out.println("CurrentDate: " + current);
        //System.out.println("EndDate: " + tempCourse.getEnd_date());
        if (current.before(tempCourse.getEnd_date())) {
            //System.out.println("Course not ended, gathering data for Taiga");
            String token = tempCourse.getTaiga_token();
            List<Team> teams = teamsService.listReadByCourse(course);
            for(Team team : teams) {
                String slug = team.getTaiga_project_slug();
                Object object = projectService.getTaigaProject(slug);
                if (object.getClass() == Project.class) {
                    Project project = (Project) object;
                    Long slugId = project.getId();
                    if (token != null && slugId != null) {
                        //System.out.println("Id: " + slugId + "/Slug: " + slug);
                        getTasks(slugId, token, 1);
                        getTaskTotals(slug, tempCourse, team);
                        closedMap.clear();
                        newMap.clear();
                        inProgressMap.clear();
                        readyForTestMap.clear();
                    }
                }
            }

        } else {
            System.out.println("Course Ended, no data gathering for Taiga");
        }
    }
}

