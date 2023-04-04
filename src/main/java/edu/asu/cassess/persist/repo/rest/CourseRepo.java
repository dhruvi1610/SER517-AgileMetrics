package edu.asu.cassess.persist.repo.rest;

import edu.asu.cassess.persist.entity.rest.Course;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CourseRepo extends JpaRepository<Course, String> {

  @Query(value = "Select * from courses c where c.end_date >= curdate() and c.taiga_refresh_token is not null", nativeQuery = true)
  List<Course> findActiveCourses();
}
