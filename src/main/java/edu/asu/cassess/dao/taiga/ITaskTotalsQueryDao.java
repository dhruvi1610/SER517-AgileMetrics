package edu.asu.cassess.dao.taiga;

import edu.asu.cassess.model.Taiga.*;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.taiga.TaskTotals;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

public interface ITaskTotalsQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    List<TaskTotals> getTaskTotals() throws DataAccessException;

    RestResponse deleteTaskTotalsByCourse(Course course) throws DataAccessException;

    RestResponse deleteTaskTotalsByProject(Team team) throws DataAccessException;

    RestResponse deleteTaskTotalsByStudent(Student student) throws DataAccessException;

    List<DailyTaskTotals> getDailyTasksByCourse(String beginDate, String endDate, String course) throws DataAccessException;

    List<DailyTaskTotals> getDailyTasksByTeam(String beginDate, String endDate, String course, String project) throws DataAccessException;

    List<DailyTaskTotals> getDailyTasksByStudent(String beginDate, String endDate, String course, String project, String student) throws DataAccessException;

    List<WeeklyIntervals> getWeeklyIntervalsByStudent(String course, String team, String student) throws DataAccessException;

    List<WeeklyIntervals> getWeeklyIntervalsByTeam(String course, String team) throws DataAccessException;

    List<WeeklyIntervals> getWeeklyIntervalsByCourse(String course) throws DataAccessException;

    List<WeeklyActivity> getWeeklyUpdatesByTeam(String course, String team) throws DataAccessException;

    List<WeeklyActivity> getWeeklyUpdatesByStudent(String course, String team, String student) throws DataAccessException;

    List<WeeklyActivity> getWeeklyUpdatesByCourse(String course) throws DataAccessException;

    List<WeeklyFreqWeight> twoWeekWeightFreqByStudent(String course, String team, String student) throws DataAccessException;

    List<WeeklyFreqWeight> twoWeekWeightFreqByTeam(String course, String team) throws DataAccessException;

    List<WeeklyFreqWeight> twoWeekWeightFreqByCourse(String course) throws DataAccessException;

    List<WeeklyFreqWeight> weeklyWeightFreqByStudent(String course, String team, String student) throws DataAccessException;

    List<WeeklyFreqWeight> weeklyWeightFreqByTeam(String course, String team) throws DataAccessException;

    List<WeeklyFreqWeight> weeklyWeightFreqByCourse(String course) throws DataAccessException;

    List<WeeklyAverages> getWeeklyAverageByCourse(String course) throws DataAccessException;

    List<WeeklyAverages> getWeeklyAverageByTeam(String course, String team) throws DataAccessException;

    List<WeeklyAverages> getWeeklyAverageByStudent(String course, String team, String email) throws DataAccessException;

    List<WeeklyAverages> lastTwoWeekAveragesByCourse(String course) throws DataAccessException;

    List<WeeklyAverages> lastTwoWeekAveragesByTeam(String course, String team) throws DataAccessException;

    List<WeeklyAverages> lastTwoWeekAveragesByStudent(String course, String team, String email) throws DataAccessException;
}
