package edu.asu.cassess.persist.repo.slack;

import edu.asu.cassess.persist.entity.slack.SlackMessageTotals;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackMessageTotalsRepo extends JpaRepository<SlackMessageTotals, String> {
}

