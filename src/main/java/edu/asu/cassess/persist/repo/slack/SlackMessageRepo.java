package edu.asu.cassess.persist.repo.slack;

import edu.asu.cassess.persist.entity.slack.SlackMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackMessageRepo extends JpaRepository<SlackMessage, Integer> {
}
