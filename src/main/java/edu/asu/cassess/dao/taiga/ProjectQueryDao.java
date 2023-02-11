package edu.asu.cassess.dao.taiga;

import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.taiga.Project;
import edu.asu.cassess.persist.entity.taiga.ProjectIDSlug;
import edu.asu.cassess.persist.repo.taiga.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
@Transactional
public class ProjectQueryDao implements IProjectQueryDao {

    protected EntityManager entityManager;

    @Autowired
    ProjectRepo projectRepo;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public RestResponse deleteTaigaProjectByCourse(Course course) {
        for (Team team : course.getTeams()) {
            Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.project WHERE slug = ?1");
            query.setParameter(1, team.getTaiga_project_slug());
            query.executeUpdate();
        }
        return new RestResponse("taiga projects for course: " + course.getCourse() + " have been removed from the database");
    }

    @Override
    public RestResponse deleteTaigaProjectByTeam(Team team) {
        Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.project WHERE slug = ?1");
        query.setParameter(1, team.getTaiga_project_slug());
        query.executeUpdate();
        return new RestResponse("taiga projects for team: " + team.getTeam_name() + " have been removed from the database");
    }

    public List<Project> getAllTaigaProjects() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.project");
        List<Project> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public <T> Object getTaigaProject(String slug) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.project WHERE slug = ?1", Project.class);
        query.setParameter(1, slug);
        List<Project> resultList = query.getResultList();
        if(!resultList.isEmpty()){
            return resultList.get(0);
        }else {
            return new RestResponse("taiga project for slug: " + slug + " does not exist in the database");
        }
    }
}
