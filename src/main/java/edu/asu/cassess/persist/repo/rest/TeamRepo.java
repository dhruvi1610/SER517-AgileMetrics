package edu.asu.cassess.persist.repo.rest;

import edu.asu.cassess.persist.entity.rest.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TeamRepo extends JpaRepository<Team, String> {

}
