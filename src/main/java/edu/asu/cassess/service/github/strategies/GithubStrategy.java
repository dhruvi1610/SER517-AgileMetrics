package edu.asu.cassess.service.github.strategies;

import edu.asu.cassess.persist.entity.github.GithubBlame;
import edu.asu.cassess.persist.entity.rest.Team;
import java.util.List;
import java.util.Set;

public interface GithubStrategy {

  List<GithubBlame> fetchData(List<Team> teams, Set<String> commitIdSet);
}
