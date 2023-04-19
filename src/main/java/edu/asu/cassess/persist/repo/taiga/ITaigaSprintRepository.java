package edu.asu.cassess.persist.repo.taiga;

import edu.asu.cassess.persist.entity.taiga.TaigaSprint;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ITaigaSprintRepository extends JpaRepository<TaigaSprint, Long> {
  List<TaigaSprint> findByCourseNameAndTeamNameOrderBySprintNameDesc(String courseName, String teamName);

  @Query("SELECT sprintId FROM TaigaSprint where isClosed = false")
  Set<Long> findActiveSprintIds();
}