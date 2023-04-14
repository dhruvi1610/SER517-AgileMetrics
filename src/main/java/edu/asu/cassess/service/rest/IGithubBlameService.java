package edu.asu.cassess.service.rest;

import java.util.Set;

public interface IGithubBlameService {
  Set<String> findDistictCommitIds();
  Set<String> findDistictCommitIdsOfCourse(String course);
}
