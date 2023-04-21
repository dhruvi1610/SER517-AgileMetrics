package edu.asu.cassess.service.github.strategies;

import edu.asu.cassess.persist.entity.github.GithubBlame;
import edu.asu.cassess.persist.entity.rest.Team;
import java.util.List;
import java.util.Set;

public class GithubContext {

  private  GithubStrategy strategy;

  public GithubContext(GithubStrategy strategy) {
    this.strategy = strategy;
  }

  public List<GithubBlame> executeStrategy(List<Team> teams, Set<String> commitIdSet) {
    return strategy.fetchData(teams, commitIdSet);
  }
}
