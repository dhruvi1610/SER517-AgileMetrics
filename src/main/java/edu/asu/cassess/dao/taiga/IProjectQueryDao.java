package edu.asu.cassess.dao.taiga;

import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.taiga.Project;
import edu.asu.cassess.persist.entity.taiga.ProjectIDSlug;
import org.springframework.dao.DataAccessException;

import javax.persistence.EntityManager;
import java.util.List;

public interface IProjectQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    /**
     * Get a list of all projects from database.
     *
     * @return List of Projects
     * @throws DataAccessException
     */
    List<Project> getAllTaigaProjects() throws DataAccessException;

    /**
     * Get Project data from database for this slug; only ever returns one object.
     *
     * @param slug the Taiga slug to filter by
     * @return a single Project object
     * @throws DataAccessException
     */
    <T> Object getTaigaProject(String slug) throws DataAccessException;

    RestResponse deleteTaigaProjectByCourse(Course course);

    RestResponse deleteTaigaProjectByTeam(Team team);
}
