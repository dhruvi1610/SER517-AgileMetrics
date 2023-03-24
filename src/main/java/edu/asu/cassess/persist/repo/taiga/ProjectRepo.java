package edu.asu.cassess.persist.repo.taiga;

import edu.asu.cassess.persist.entity.taiga.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface ProjectRepo extends JpaRepository<Project, String> {

  @Query(value = "Select t.team_name, t.course as course_name, p.id as project_id, c.taiga_token \n"
      + "from project p Join teams t on t.taiga_project_slug = p.slug Join courses c on t.course = c.course \n"
      + "where c.end_date >= curdate()", nativeQuery = true)
  List<Object[]> getProjectInfoOfActiveCourses();

  @Query(value = "Select t.team_name, t.course as course_name, p.id as project_id, c.taiga_token \n"
      + "from project p Join teams t on t.taiga_project_slug = p.slug Join courses c on t.course = c.course \n"
      + "where t.course = ?1", nativeQuery = true)
  List<Object[]> getProjectCourseInfoByCourse(String course);
}
