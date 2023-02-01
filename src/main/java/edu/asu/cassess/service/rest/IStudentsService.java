package edu.asu.cassess.service.rest;

import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.model.Taiga.TeamNames;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface IStudentsService {

    <T> Object create(Student student);

    <T> Object update(Student student);

    <T> Object find(String email, String team, String course);

    <T> Object findGitHubUser(String github_username, String team, String course);

    <T> Object findTaigaUser(String taiga_username, String team, String course);

    <T> Object findSlackUser(String slack_username, String team, String course);

    <T> Object find(String email, String course);

    <T> Object delete(Student student);

    <T> List<Student> listReadAll();

    <T> List<Student> listReadByTeam(String course, String team_name);

    <T> List<Student> listReadByCourse(String course);

    List<Student> listReadStudent(String course, String email) throws DataAccessException;

    List<Student> listReadSingleStudent(String course, String team, String email) throws DataAccessException;

    JSONObject listUpdate(List<Student> students);

    JSONObject listCreate(List<Student> students);

    <T> Object deleteByTeam(Team team);

    List<CourseList> listGetCoursesForStudent(String email);

    List<TeamNames> listGetAssignedTeams(String email, String course);

	}