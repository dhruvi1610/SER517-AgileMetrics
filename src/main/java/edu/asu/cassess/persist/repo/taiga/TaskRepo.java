package edu.asu.cassess.persist.repo.taiga;


import edu.asu.cassess.persist.entity.taiga.TaskData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TaskRepo extends JpaRepository<TaskData, Long> {

}