package edu.asu.cassess.web.controller;

import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.service.github.IGatherGitHubData;
import edu.asu.cassess.service.rest.ICourseService;
import edu.asu.cassess.service.rest.ITeamsService;
import edu.asu.cassess.service.slack.IChannelHistoryService;
import edu.asu.cassess.service.taiga.ITaigaSprintService;
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
    private ITaigaSprintService taigaSprintService;
	
	@Scheduled(cron = "${taiga.cron.expression}")
	public void TaigaTasks() {
        List<CourseList> courseList = coursesService.listGetCourses();
        for (CourseList course : courseList) {
            taigaDataService.updateTaskTotals(course.getCourse());
        }
		System.out.println("taiga cron ran as scheduled");
	}

    @Scheduled(cron = "${github.cron.expression}")
    public void GitHubCommits() {
        List<Team> teams = teamsService.listReadAll();
        for(Team team: teams){
            Course course = (Course) coursesService.read(team.getCourse());
            gatherData.fetchData(team.getGithub_owner(), team.getGithub_repo_id(), course.getCourse(), team.getTeam_name(), team.getGithub_token());
        }
        System.out.println("github cron ran as scheduled");
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
    //@Scheduled(fixedRate = 10000)
    public void TaigaSprints() {
        taigaSprintService.updateActiveSprints();
        System.out.println("taiga sprints cron ran as scheduled");
    }

    @Scheduled(cron = "${taiga.tokens.cron.expression}")
    public void RefreshTaigaTokens() {
        coursesService.refreshTaigaTokes();
        System.out.println("refresh taiga tokens cron ran as scheduled");
    }
}
