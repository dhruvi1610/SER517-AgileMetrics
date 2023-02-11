package edu.asu.cassess.persist.repo;

import edu.asu.cassess.persist.entity.security.UsersAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Thomas on 3/27/2017.
 */
public interface UsersAuthorityRepo extends JpaRepository<UsersAuthority, Long> {

}