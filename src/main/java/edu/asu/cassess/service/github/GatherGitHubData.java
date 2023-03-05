package edu.asu.cassess.service.github;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.cassess.dao.github.IGitHubCommitDataDao;
import edu.asu.cassess.dao.github.IGitHubWeightQueryDao;
import edu.asu.cassess.model.github.GitHubAnalytics;
import edu.asu.cassess.persist.entity.github.CommitData;

import edu.asu.cassess.persist.entity.github.GitHubPK;
import edu.asu.cassess.persist.entity.github.GitHubWeight;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.repo.github.CommitDataRepo;
import edu.asu.cassess.persist.repo.github.GitHubWeightRepo;
import edu.asu.cassess.service.rest.CourseService;
import edu.asu.cassess.service.rest.ICourseService;
import edu.asu.cassess.service.rest.IStudentsService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
@Transactional
public class GatherGitHubData implements IGatherGitHubData {

    @Autowired
    private IGitHubCommitDataDao commitDao;

    @Autowired
    private GitHubWeightRepo weightRepo;

    @Autowired
    private CommitDataRepo commitDataRepo;

    @Autowired
    private IStudentsService studentsService;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private IGitHubWeightQueryDao ghWeightQuery;

    private RestTemplate restTemplate;
    private String url;
    private String projectName;
    private String owner;

    public GatherGitHubData() {
        restTemplate = new RestTemplate();
    }

    /**
     * Gathers the GitHub commit data from the GitHub Repo Stats
     * The github owner and project name can be found in the repo url as follows
     * www.github.com/:owner/:projectName
     *
     * @param owner       the owner of the repo
     * @param projectName the project name of the repo
     */
    @Override
    public void fetchData(String owner, String projectName, String course, String team, String accessToken) {
        if (courseService == null) courseService = new CourseService();
        Course tempCourse = (Course) courseService.read(course);
        Date current = new Date();
        try {
            current = new SimpleDateFormat("yyyy-mm-dd").parse(LocalDate.now().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("Getting Stats for course: " + course + " & Team: " + team);
        if (current.before(tempCourse.getEnd_date())) {
            this.projectName = projectName;
            this.owner = owner;
            url = "https://api.github.com/repos/" + owner + "/" + projectName;
            getStats(course, team, accessToken, tempCourse);
        } else {
            ///System.out.println("*****************************************************Course Ended, no GH data Gathering");
        }
    }

    private void getStats(String course, String team, String accessToken, Course tempCourse) {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/stats/contributors");
//                .queryParam("access_token", accessToken + "&scope=&token_type=bearer");
        String urlPath = builder.build().toUriString();

        //System.out.println("GitHub URL: " + urlPath);

        String json = "";
        try {
            json = restTemplate.getForObject(urlPath, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println("Read as String");

        List<GitHubContributors> contributors = null;

        System.out.println("contributors initialized");

        if(!json.isEmpty() && !json.startsWith("{}")) {

            try {
                contributors = mapper.readValue(json, new TypeReference<List<GitHubContributors>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(contributors != null) {
                contributors.removeAll(Collections.singleton(null));
                storeStats(contributors, accessToken, course, team, tempCourse);
            }
        } else {
            //System.out.println("Response Empty");
        }
    }

    private void storeStats(List<GitHubContributors> contributors, String accessToken, String course, String team, Course tempCourse) {
        //System.out.println("Storing Stats for course: " + course + " & Team: " + team);
        Collections.reverse(contributors);

        for (GitHubContributors contributor : contributors) {
            //System.out.println("*******************************************************Iterating Contributors");
            Weeks[] weeks = contributor.getWeeks();
            //System.out.println("*******************************************************Weeks: " + weeks);

            String userName = contributor.getAuthor().getLogin();

            //System.out.println("*******************************************************Username: " + userName);

            Date lastDate = null;

            if (ghWeightQuery.getlastDate(course, team, userName) != null) {
                try {
                    lastDate = new SimpleDateFormat("yyyy-MM-dd").parse(ghWeightQuery.getlastDate(course, team, userName).getGitHubPK().getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                    lastDate = null;
                }
            }

            Date sqlStartDate = new Date();
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(tempCourse.getStart_date());
            System.out.println("StartCal: " + startCal.toString());
            int weekDay = startCal.get (Calendar.DAY_OF_WEEK);
            int weekSub = weekDay * -1;
            //System.out.println("WeekDay: " +  weekDay);
            //System.out.println("WeekSub: " +  weekSub);
            if(weekDay == 7){
                sqlStartDate = startCal.getTime();
            } else {
                startCal.add(Calendar.DAY_OF_WEEK, weekSub);
                sqlStartDate = startCal.getTime();
                //System.out.println("StartCalConverted: " + startCal.toString());
            }

            //System.out.println("*******************************************************SatStartDate: " + sqlStartDate);
            //System.out.println("*******************************************************RealStartDate: " + tempCourse.getStart_date());
            //System.out.println("*******************************************************UserName: " + userName);

            boolean ghMatch = false;
            boolean studentEnabled = false;

            Student student = new Student();
            //System.out.println("Finding Student for Course: " + course + " & Team: " + team + " & Email: " + email);
            Object object = studentsService.findGitHubUser(userName, team, course);
            if (object.getClass() == Student.class) {
                student = (Student) object;
                ghMatch = true;
                if (student.getEnabled() != null) {
                    if (student.getEnabled()) {
                        //System.out.println("**********************************************Enabled");
                        studentEnabled = true;
                    } else {
                        //System.out.println("**********************************************Not Enabled");
                        studentEnabled = false;
                    }
                } else {
                    //System.out.println("**********************************************Not Enabled");
                    studentEnabled = false;
                }
            } else {
                //System.out.println("**********************************************Not a Student);
                ghMatch = false;
            }
            //System.out.println("Found Student for Course: " + student.getCourse() + " & Team: " + student.getTeam_name() + " & Email: " + student.getEmail());


            if (studentEnabled && ghMatch) {
                for (Weeks week : weeks) {
                    Date date = new Date(week.getW() * 1000L);
                    //System.out.print("*********************************************************ContribDate: " + date);
                    if (lastDate == null || !date.before(lastDate)) {

                        if (date.after(sqlStartDate) && date.before(tempCourse.getEnd_date())) {
                            //System.out.println("*****************************************************Inserting to DB");
                            int linesAdded = week.getA();
                            int linesDeleted = week.getD();
                            int commits = week.getC();

                            //System.out.println("Saving Commit Data for Course: " + course + " & Team: " + team + " & userName: " + userName);
                            commitDataRepo.save(new CommitData(new GitHubPK(course, student.getTeam_name(), userName, date), student.getEmail(), linesAdded, linesDeleted, commits, projectName, owner));

                            double weight = GitHubAnalytics.calculateWeight(linesAdded, linesDeleted);
                            //System.out.println("Saving Weight Data for Course: " + course + " & Team: " + team + " & userName: " + userName);
                            GitHubWeight gitHubWeight = new GitHubWeight(new GitHubPK(course, student.getTeam_name(), userName, date), student.getEmail(), weight);
                            weightRepo.save(gitHubWeight);
                        } else {
                            //System.out.println("************Not Inserting to DB - contributions date not between course start end end dates");
                        }
                    } else {
                        //System.out.println("*****************************************************Not Inserting to DB - complete record for week already exists");
                    }
                }
            } else {
                //System.out.println("*****************************************************Not a match or not enabled");
            }
        }
    }

    /**
     * Gathers all the rows in the commit_data table
     *
     * @return A list of CommitData Objects
     */
    @Override
    public List<CommitData> getCommitList(){
        return commitDao.getAllCommitData();
    }
}
