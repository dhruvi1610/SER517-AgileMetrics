package edu.asu.cassess.persist.repo;

import edu.asu.cassess.persist.entity.security.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepo extends JpaRepository<Token, String> {
}
