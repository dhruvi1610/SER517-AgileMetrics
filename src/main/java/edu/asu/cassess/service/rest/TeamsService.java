package edu.asu.cassess.service.rest;

import edu.asu.cassess.dao.rest.TeamsServiceDao;
import edu.asu.cassess.model.Taiga.Slugs;
import edu.asu.cassess.model.Taiga.TeamNames;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Team;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.ejb.EJB;
import java.util.Arrays;
import java.util.List;

@Service
public class TeamsService implements ITeamsService {

    @EJB
    private TeamsServiceDao teamsDao;

    @Override
    public <T> Object create(Team team) {
        return teamsDao.create(team);
    }

    @Override
    public <T> Object update(Team team) {
        return teamsDao.update(team);
    }

    @Override
    public <T> Object read(String team_name, String course) {
        return teamsDao.find(team_name, course);
    }

    @Override
    public <T> Object findOne(String team_name, String course) {
        return teamsDao.findOne(team_name, course);
    }

    @Override
    public <T> Object delete(Team team) {
        return teamsDao.delete(team);
    }

    @Override
    public <T> List<Team> listReadAll() {
        return teamsDao.listReadAll();
    }

    @Override
    public <T> List<Team> listReadByCourse(String course) {
        return teamsDao.listReadByCourse(course);
    }

    @Override
    public JSONObject listCreate(List<Team> teams) {
        return teamsDao.listCreate(teams);
    }

    @Override
    public JSONObject listUpdate(List<Team> teams) {
        return teamsDao.listUpdate(teams);
    }

    @Override
    public List<Slugs> listGetSlugs(String course) {
        return teamsDao.listGetSlugs(course);
    }

    @Override
    public List<TeamNames> listGetTeamNames(String course) {
        return teamsDao.listGetTeamNames(course);
    }

    @Override
    public <T> Object deleteByCourse(Course course) {
        return teamsDao.deleteByCourse(course);
    }
    // method for fetching the names of the teams on canvas using course id and canvas access token
    @Override
    public List<Team> getTeamCanvas(Long courseId, String canvasToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://canvas.asu.edu/api/v1/courses/"+courseId+"/groups?per_page=30";
        HttpHeaders headers = new HttpHeaders();
        if(!canvasToken.equalsIgnoreCase("na")) {
            headers.setBearerAuth(canvasToken);
        }

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Team[]> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, Team[].class);

        } catch (Exception e) {
            System.out.println("Canvas fetch stats failed. " + e.getMessage());
        }

        if(responseEntity != null && responseEntity.getBody() != null && !Arrays.toString(responseEntity.getBody()).startsWith("{}")) {
            return Arrays.asList(responseEntity.getBody());
        }
        return null;
    }

}
