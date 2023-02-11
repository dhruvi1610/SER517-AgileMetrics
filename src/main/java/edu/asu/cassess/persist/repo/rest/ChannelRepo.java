package edu.asu.cassess.persist.repo.rest;

import edu.asu.cassess.persist.entity.rest.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ChannelRepo extends JpaRepository<Channel, String> {
}
