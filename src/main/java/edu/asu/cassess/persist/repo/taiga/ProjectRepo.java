package edu.asu.cassess.persist.repo.taiga;

import edu.asu.cassess.persist.entity.taiga.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProjectRepo extends JpaRepository<Project, String> {
}
