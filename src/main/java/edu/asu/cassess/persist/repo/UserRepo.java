package edu.asu.cassess.persist.repo;

import edu.asu.cassess.persist.entity.security.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepo extends JpaRepository<User, Long> {
    User findByLogin(String login);

    User findByEmail(String email);
}
