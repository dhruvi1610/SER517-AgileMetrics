package edu.asu.cassess.service.rest;

import edu.asu.cassess.dto.github.FileChangesDto;
import edu.asu.cassess.dto.github.internal.CommitDetailDto;
import java.util.List;
import java.util.Set;

public interface IGithubBlameService {

  List<FileChangesDto> findFileChangesOfCommit(String commitId);
  List<CommitDetailDto> findCommitsByUsername(String username);
  List<CommitDetailDto> findCommitsByFullName(String username);
  Set<String> findDistictCommitIds();
  Set<String> findDistictCommitIdsOfCourse(String course);
}
