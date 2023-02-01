package edu.asu.cassess.dao.taiga;

import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.taiga.MemberData;
import org.springframework.dao.DataAccessException;

import javax.persistence.EntityManager;
import java.util.List;

public interface IMemberQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    RestResponse deleteMembersByCourse(Course course) throws DataAccessException;

    RestResponse deleteMembersByTeam(Team team) throws DataAccessException;

    RestResponse deleteMembersByStudent(Student student) throws DataAccessException;

    /**
     * Get members based on role and/or slug.
     *
     * @param roleName     the name of the role to filter by
     * @param project_slug the Taiga slug to filter by
     * @return List of MemberData
     * @throws DataAccessException
     */
    List<MemberData> getMembers(String roleName, String project_slug) throws DataAccessException;
}
