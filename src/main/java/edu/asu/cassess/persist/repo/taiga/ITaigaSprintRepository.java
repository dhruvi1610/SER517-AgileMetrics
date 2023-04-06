package edu.asu.cassess.persist.repo.taiga;

import edu.asu.cassess.persist.entity.taiga.TaigaSprint;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaigaSprintRepository extends JpaRepository<TaigaSprint, Long> {
  List<TaigaSprint> findByCourseNameAndTeamNameOrderBySprintNameDesc(String courseName, String teamName);
}