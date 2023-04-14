package edu.asu.cassess.service.github;

import edu.asu.cassess.dao.github.IGitHubCommitDataDao;
import edu.asu.cassess.dao.github.IGitHubWeightQueryDao;
import edu.asu.cassess.dto.github.external.BlameResponseDto;
import edu.asu.cassess.dto.github.external.CommitDto;
import edu.asu.cassess.dto.github.FileChangesDto;
import edu.asu.cassess.model.github.GitHubAnalytics;
import edu.asu.cassess.persist.entity.github.CommitData;

import edu.asu.cassess.persist.entity.github.GitHubPK;
import edu.asu.cassess.persist.entity.github.GitHubWeight;
import edu.asu.cassess.persist.entity.github.GithubBlame;
import edu.asu.cassess.persist.entity.github.GithubBlameId;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.repo.github.CommitDataRepo;
import edu.asu.cassess.persist.repo.github.GitHubWeightRepo;
import edu.asu.cassess.persist.repo.github.IGithubBlameRepository;
import edu.asu.cassess.service.rest.CourseService;
import edu.asu.cassess.service.rest.ICourseService;
import edu.asu.cassess.service.rest.IStudentsService;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
@Transactional
public class GatherGitHubData implements IGatherGitHubData {

    public static final String BASE_URL = "https://api.github.com/repos/";
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

    @Autowired
    private IGithubBlameRepository githubBlameRepository;

    private RestTemplate restTemplate;
    private String url;
    private String projectName;
    private String owner;

    public GatherGitHubData() {
        restTemplate = new RestTemplate();
    }

    @Override
    public void fetchBlameData(String owner, String repoName, String course, String team,
        String accessToken, List<Student> students, Set<String> commitIdSet) {
        List<GithubBlame> result = new ArrayList<>();
        String commitUrl = String.format("%s/%s/%s/commits", BASE_URL, owner, repoName);
        Map<String, String> studentsMap = students.stream()
            .collect(Collectors.toMap(Student::getGithub_username, Student::getFull_name));

        List<String> commitIds = getNewCommitIdsOfStudents(commitUrl, studentsMap, commitIdSet);
        for(String commitId : commitIds) {
            try {
                BlameResponseDto blameResponseDto = restTemplate.getForObject(commitUrl + "/" + commitId, BlameResponseDto.class);
                if(blameResponseDto != null) {
                    result.addAll(getFileChanges(blameResponseDto, course, team, studentsMap));
                }
            } catch (Exception e) {
                System.out.println("Git blame fetch failed. " + e.getMessage());
            }
        }

        githubBlameRepository.save(result);
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
    public void fetchContributorsStats(String owner, String projectName, String course, String team, String accessToken) {
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
            url = BASE_URL + owner + "/" + projectName;
            getStats(course, team, accessToken, tempCourse);
        } else {
            ///System.out.println("*****************************************************Course Ended, no GH data Gathering");
        }
    }

    private List<String> getNewCommitIdsOfStudents(String commitUrl, Map<String, String> studentsMap, Set<String> commitIdSet) {
        List<String> result = new ArrayList<>();
        try {
            CommitDto[] commits = restTemplate.getForObject(commitUrl, CommitDto[].class);
            for (CommitDto commit : commits) {
                String username = commit.getCommitInfo().getCommitter().getUsername();
                if(studentsMap.containsKey(username) && !commitIdSet.contains(commit.getCommitId())) {
                    result.add(commit.getCommitId());
                }
            }
            return result;
        } catch (Exception e) {
            System.out.println("Git commit fetch failed. " + e.getMessage());
            return result;
        }
    }

    private List<GithubBlame> getFileChanges(BlameResponseDto blameResponseDto, String course, String team, Map<String, String> studentsMap) {
        List<GithubBlame> result = new ArrayList<>();
        String username = blameResponseDto.getCommitInfo().getCommitter().getUsername();
        String fullName = studentsMap.get(username);
        String commitMsg = blameResponseDto.getCommitInfo().getMessage();
        LocalDate commitDate = getDateFromString(blameResponseDto.getCommitInfo().getCommitter().getDate());
        for(FileChangesDto file : blameResponseDto.getFiles()) {
            GithubBlameId githubBlameId = new GithubBlameId(blameResponseDto.getCommitId(), file.getFilename());
            GithubBlame githubBlame = new GithubBlame(githubBlameId, course, team, username, commitDate, file.getStatus(), commitMsg, fullName,
                file.getAdditions(), file.getDeletions(), file.getPatch());
            result.add(githubBlame);
        }
        return result;
    }

    private LocalDate getDateFromString(String dateString) {
        Instant instant = Instant.parse(dateString);
        return LocalDate.ofInstant(Instant.parse(dateString), ZoneOffset.UTC);
    }

    private void getStats(String course, String team, String accessToken, Course tempCourse) {
        System.out.println("contributors initialized");

        HttpHeaders headers = new HttpHeaders();
        if(!accessToken.equalsIgnoreCase("na")) {
            headers.setBearerAuth(accessToken);
        }
        //headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<GitHubContributors[]> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(url + "/stats/contributors", HttpMethod.GET, request, GitHubContributors[].class);
        } catch (Exception e) {
            System.out.println("Git fetch stats failed. " + e.getMessage());
        }

        if(responseEntity != null && responseEntity.getBody() != null && !Arrays.toString(responseEntity.getBody()).startsWith("{}")) {
            List<GitHubContributors> contributors = Arrays.asList(responseEntity.getBody());
            if (!contributors.isEmpty()) {
                contributors.removeAll(Collections.singleton(null));
                storeStats(contributors, accessToken, course, team, tempCourse);
            }
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