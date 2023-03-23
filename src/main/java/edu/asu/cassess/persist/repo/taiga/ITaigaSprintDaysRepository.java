package edu.asu.cassess.persist.repo.taiga;

import edu.asu.cassess.persist.entity.taiga.TaigaSprintDays;
import edu.asu.cassess.persist.entity.taiga.TaigaSprintDaysId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaigaSprintDaysRepository extends JpaRepository<TaigaSprintDays, TaigaSprintDaysId> {

  List<TaigaSprintDays> findByTaigaSprintDaysIdSprintIdOrderByTaigaSprintDaysIdDateAsc(Long sprintId);
}