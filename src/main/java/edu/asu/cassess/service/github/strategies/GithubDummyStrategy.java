package edu.asu.cassess.service.github.strategies;

import edu.asu.cassess.persist.entity.rest.Team;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GithubDummyStrategy implements IGithubStrategy{

  @Override
  public void consumeData(List<Team> teams) {
    System.out.println("Github Dummy Strategy");
  }
}
