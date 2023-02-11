package edu.asu.cassess.persist.repo;

import edu.asu.cassess.persist.entity.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepo extends JpaRepository<Authority, Long> {

}