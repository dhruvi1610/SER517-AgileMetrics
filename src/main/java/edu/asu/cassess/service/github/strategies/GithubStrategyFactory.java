package edu.asu.cassess.service.github.strategies;

import constants.AppConstants;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GithubStrategyFactory {

  @Autowired
  private GithubBlameStrategy githubBlameStrategy;

  private final Map<String, IGithubStrategy> strategies = new HashMap<>();

  @PostConstruct
  private void populateStrategies() {
    strategies.put(AppConstants.GITHUB_BLAME_STRATEGY, githubBlameStrategy);
  }

  public IGithubStrategy getStrategy(String strategyName) {
    return strategies.get(strategyName);
  }
}
