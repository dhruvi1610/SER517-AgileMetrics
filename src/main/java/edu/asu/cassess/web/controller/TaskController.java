package edu.asu.cassess.web.controller;

import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.persist.entity.github.GithubBlame;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.service.github.IGatherGitHubData;
import edu.asu.cassess.service.github.strategies.GithubBlameStrategy;
import edu.asu.cassess.service.github.strategies.GithubContext;
import edu.asu.cassess.service.rest.ICourseService;
import edu.asu.cassess.service.rest.IGithubBlameService;
import edu.asu.cassess.service.rest.ITeamsService;
import edu.asu.cassess.service.slack.IChannelHistoryService;
import edu.asu.cassess.service.taiga.ITaigaSprintService;
import edu.asu.cassess.utils.DateUtil;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import edu.asu.cassess.service.taiga.ITaskDataService;

import javax.ejb.EJB;
import java.util.List;

@RestController
@PropertySource("classpath:scheduling.properties")
public class TaskController {

	@Autowired
	private ITaskDataService taigaDataService;

    @Autowired
    private IChannelHistoryService channelHistoryService;

    @EJB
    private ICourseService coursesService;

    @EJB
    private ITeamsService teamsService;

    @Autowired
    private IGatherGitHubData gatherData;

    @Autowired
    private IGithubBlameService githubBlameService;

    @Autowired
    private ITaigaSprintService taigaSprintService;
	
	@Scheduled(cron = "${taiga.cron.expression}")
	public void TaigaTasks() {
        List<CourseList> courseList = coursesService.listGetCourses();
        for (CourseList course : courseList) {
            taigaDataService.updateTaskTotals(course.getCourse());
        }
		System.out.println("taiga cron ran as scheduled");
	}

    @Scheduled(cron = "${github.stats.cron.expression}")
    public void GitHubStats() {
        Map<String, Course> courseMap = coursesService.listRead().stream()
            .collect(Collectors.toMap(Course::getCourse, course -> course));
        List<Team> teams = teamsService.listReadAll();
        for(Team team: teams){
            if(team.getCourse() != null) {
                Course course = courseMap.get(team.getCourse());
                if(DateUtil.isDateLesserThanEqual(LocalDate.now(), course.getEnd_date().toLocalDate())) {
                    gatherData.fetchContributorsStats(team.getGithub_owner(), team.getGithub_repo_id(), course.getCourse(), team.getTeam_name(), team.getGithub_token());
                }
            }
        }
        System.out.println("github stats cron for stats ran as scheduled");
    }

    @Scheduled(cron = "${github.blame.cron.expression}")
    @Scheduled(fixedRate = 1000000)
    public void GitHubBlame() {
        Map<String, Course> courseMap = coursesService.listRead().stream()
            .collect(Collectors.toMap(Course::getCourse, course -> course));
        GithubContext githubContext = new GithubContext(new GithubBlameStrategy());
        List<Team> teams = teamsService.listReadAll().stream().filter(team -> {
            if(team.getCourse() != null) {
                Course course = courseMap.get(team.getCourse());
                return course.getCourse().equals("Test Course");
//                return (DateUtil.isDateLesserThanEqual(LocalDate.now(), course.getEnd_date().toLocalDate()));
            }
            return false;
        }).collect(Collectors.toList());
        Set<String> commitIdSet = githubBlameService.findDistictCommitIds();
        List<GithubBlame> result = githubContext.executeStrategy(teams, commitIdSet);
        githubBlameService.saveMany(result);
        System.out.println("github blame cron for stats ran as scheduled");
    }

    @Scheduled(cron = "${slack.cron.expression}")
    public void SlackMessages() {
    List<CourseList> courseList = coursesService.listGetCourses();
        for (CourseList course : courseList) {
        channelHistoryService.updateMessageTotals(course.getCourse());
        }
        System.out.println("slack cron ran as scheduled");
    }

    @Scheduled(cron = "${taiga.sprints.cron.expression}")
    public void TaigaSprints() {
        taigaSprintService.updateActiveSprints();
        System.out.println("taiga sprints cron ran as scheduled");
    }

    @Scheduled(cron = "${taiga.tokens.cron.expression}")
    public void RefreshTaigaTokens() {
        coursesService.refreshTaigaTokes();
        System.out.println("refresh taiga tokens cron ran as scheduled");
    }

    @Scheduled(cron = "${taiga.tasks.history.cron.expression}")
    public void UpdateTaskHistory() {
        taigaSprintService.updateTaskHistoryofActiveSprints();
        System.out.println("Task History cron ran as scheduled");
    }
}
