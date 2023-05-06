package edu.asu.cassess.config;

import edu.asu.cassess.constants.AppConstants;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GithubStrategyConfig {

  private String selectedStrategy = AppConstants.GITHUB_BLAME_STRATEGY;

  public String getSelectedStrategy() {
    return selectedStrategy;
  }
}
