package edu.asu.cassess.service.rest;

import edu.asu.cassess.model.Taiga.Slugs;
import edu.asu.cassess.model.Taiga.TeamNames;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Team;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;

import java.util.List;


public interface ITeamsService {

    <T> Object create(Team team);

    <T> Object update(Team team);

    <T> Object read(String team, String course);

    <T> Object findOne(String team_name, String course);

    <T> Object delete(Team team);

    <T> List<Team> listReadAll();

    <T> List<Team> listReadByCourse(String course);

    JSONObject listCreate(List<Team> teams);

    JSONObject listUpdate(List<Team> teams);

    List<Slugs> listGetSlugs(String course);

    List<TeamNames> listGetTeamNames(String course);

    <T> Object deleteByCourse(Course course);

    List<Team> getTeamCanvas(Long courseId, String canvasToken);
}