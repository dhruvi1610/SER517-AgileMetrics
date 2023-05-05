package edu.asu.cassess.web.controller;

import edu.asu.cassess.config.GithubStrategyConfig;
import edu.asu.cassess.constants.AppConstants;
import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.service.github.IGatherGitHubData;
import edu.asu.cassess.service.github.strategies.IGithubStrategy;
import edu.asu.cassess.service.github.strategies.GithubStrategyFactory;
import edu.asu.cassess.service.rest.ICourseService;
import edu.asu.cassess.service.rest.IGithubBlameService;
import edu.asu.cassess.service.rest.ITeamsService;
import edu.asu.cassess.service.slack.IChannelHistoryService;
import edu.asu.cassess.service.taiga.ITaigaSprintService;
import edu.asu.cassess.utils.DateUtil;
import java.time.LocalDate;
import java.util.Map;
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

    @Autowired
    private GithubStrategyFactory githubStrategyFactory;

    private GithubStrategyConfig githubStrategyConfig;

    public TaskController() {
        githubStrategyConfig = new GithubStrategyConfig();
    }
	
	@Scheduled(cron = "${taiga.cron.expression}")
	public void TaigaTasks() {
        List<CourseList> courseList = coursesService.listGetCourses();
        for (CourseList course : courseList) {
            taigaDataService.updateTaskTotals(course.getCourse());
        }
		System.out.println("taiga cron ran as scheduled");
	}

    @Scheduled(cron = "${github.stats.cron.expression}")
    //@Scheduled(fixedRate = 10000)
    public void GitHubStats() {
        Map<String, Course> courseMap = coursesService.listRead().stream()
            .collect(Collectors.toMap(Course::getCourse, course -> course));
        List<Team> teams = teamsService.listReadAll();
        for(Team team: teams){
            if(team.getCourse() != null) {

                Course course = courseMap.get(team.getCourse());
               // if(course != null && course.getCourse().equals("SER 517"))
                    //gatherData.fetchContributorsStats(team.getGithub_owner(), team.getGithub_repo_id(), course.getCourse(), team.getTeam_name(), team.getGithub_token());

                if(DateUtil.isDateLesserThanEqual(LocalDate.now(), course.getEnd_date().toLocalDate())) {
                    gatherData.fetchContributorsStats(team.getGithub_owner(), team.getGithub_repo_id(), course.getCourse(), team.getTeam_name(), team.getGithub_token());
                }
            }
        }
        System.out.println("github stats cron for stats ran as scheduled");
    }

    @Scheduled(cron = "${github.blame.cron.expression}")
//    @Scheduled(fixedRate = 1000000)
    public void GitHubBlame() {
        Map<String, Course> courseMap = coursesService.listRead().stream()
            .collect(Collectors.toMap(Course::getCourse, course -> course));
        List<Team> teams = teamsService.listReadAll().stream().filter(team -> {
            if(team.getCourse() != null) {
                Course course = courseMap.get(team.getCourse());
               //if(course != null)
                 //return course.getCourse().equals("SER 517");
                return (DateUtil.isDateLesserThanEqual(LocalDate.now(), course.getEnd_date().toLocalDate()));
            }
            return false;
        }).collect(Collectors.toList());
        IGithubStrategy githubStrategy = githubStrategyFactory.getStrategy(githubStrategyConfig.getSelectedStrategy());
        githubStrategy.consumeData(teams);
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
    //@Scheduled(fixedRate = 100000)
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
