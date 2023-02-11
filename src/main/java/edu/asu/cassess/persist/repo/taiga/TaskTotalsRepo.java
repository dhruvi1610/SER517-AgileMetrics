package edu.asu.cassess.persist.repo.taiga;

import edu.asu.cassess.persist.entity.taiga.TaskTotals;
import edu.asu.cassess.persist.entity.taiga.TaskTotalsID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TaskTotalsRepo extends JpaRepository<TaskTotals, TaskTotalsID> {

}
