package edu.asu.cassess.service.rest;

import edu.asu.cassess.dto.github.FileChangesDto;
import edu.asu.cassess.dto.github.internal.CommitDetailDto;
import edu.asu.cassess.persist.entity.github.GithubBlame;
import edu.asu.cassess.projections.GitFileChangesStats;
import java.util.List;
import java.util.Set;

public interface IGithubBlameService {

  void saveMany(List<GithubBlame> values);
  List<FileChangesDto> findFileChangesOfCommit(String commitId);
  List<CommitDetailDto> findCommitsByUsername(String username);
  List<CommitDetailDto> findCommitsByFullName(String username);
  Set<String> findDistictCommitIds();
  Set<String> findDistictCommitIdsOfCourse(String course);
  List<GitFileChangesStats> getLineChangesOfTeams(String course);
  List<GitFileChangesStats> getLineChangesOfStudents(String course, String team);
}
