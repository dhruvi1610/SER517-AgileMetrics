package edu.asu.cassess.dao.taiga;

import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.taiga.MemberData;
import edu.asu.cassess.persist.repo.taiga.MemberRepo;
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
public class MemberQueryDao implements IMemberQueryDao {

    protected EntityManager entityManager;

    @Autowired
    MemberRepo memberRepo;

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
    public RestResponse deleteMembersByCourse(Course course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.memberdata WHERE course = ?1");
        query.setParameter(1, course.getCourse());
        query.executeUpdate();
        return new RestResponse("Taiga Members for course: " + course.getCourse() + " have been removed from the database");
    }

    @Override
    public RestResponse deleteMembersByTeam(Team team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.memberdata WHERE course = ?1 AND team = ?2");
        query.setParameter(1, team.getCourse());
        query.setParameter(2, team.getTeam_name());
        query.executeUpdate();
        return new RestResponse("Taiga Members for team: " + team.getTeam_name() + " have been removed from the database");
    }

    @Override
    public RestResponse deleteMembersByStudent(Student student) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.memberdata WHERE course = ?1 AND team = ?2 AND email = ?3");
        query.setParameter(1, student.getCourse());
        query.setParameter(2, student.getTeam_name());
        query.setParameter(3, student.getEmail());
        query.executeUpdate();
        return new RestResponse("Taiga Member for student: " + student.getEmail() + " have been removed from the database");
    }

    @Override
    public List<MemberData> getMembers(String roleName, String project_slug) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.memberdata WHERE roleName != ?1 AND project_slug = ?2", MemberData.class);
        query.setParameter(1, roleName);
        query.setParameter(2, project_slug);
        List<MemberData> resultList = query.getResultList();
        return resultList;
    }

}