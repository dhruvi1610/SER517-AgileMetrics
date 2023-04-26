package edu.asu.cassess.service.github.strategies;

import edu.asu.cassess.persist.entity.github.GithubBlame;
import edu.asu.cassess.persist.entity.rest.Team;
import java.util.List;

public interface IGithubStrategy {

  void consumeData(List<Team> teams);
}
