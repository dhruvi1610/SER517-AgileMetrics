package edu.asu.cassess.service.rest;

import edu.asu.cassess.dto.github.FileChangesDto;
import edu.asu.cassess.dto.github.internal.CommitDetailDto;
import edu.asu.cassess.persist.entity.github.GithubBlame;
import edu.asu.cassess.projections.GitFileChangesStats;
import edu.asu.cassess.persist.repo.github.IGithubBlameRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GithubBlameService implements IGithubBlameService{

  @Autowired
  IGithubBlameRepository githubBlameRepository;

  @Override
  public void saveMany(List<GithubBlame> values) {
    githubBlameRepository.save(values);
  }

  @Override
  public List<FileChangesDto> findFileChangesOfCommit(String commitId) {
    return githubBlameRepository.findByGithubBlameIdCommitId(commitId).stream()
        .map(item -> new FileChangesDto(item.getGithubBlameId().getFilename(),
            item.getCommitStatus(), item.getLinesOfCodeAdded(),
            item.getLinesOfCodeDeleted(), item.getPatch()))
        .collect(Collectors.toList());
  }

  @Override
  public List<CommitDetailDto> findCommitsByUsername(String username) {
    return convertObjectToCommitDto(githubBlameRepository.findDistinctCommitsByUsername(username));
  }

  @Override
  public List<CommitDetailDto> findCommitsByFullName(String fullname) {
    return convertObjectToCommitDto(githubBlameRepository.findDistinctCommitsByFullName(fullname));
  }

  @Override
  public Set<String> findDistictCommitIds() {
    return githubBlameRepository.findDistinctCommitIds();
  }

  @Override
  public Set<String> findDistictCommitIdsOfCourse(String course) {
    return githubBlameRepository.findDistinctCommitIdsOfCourse(course);
  }

  @Override
  public List<GitFileChangesStats> getLineChangesOfTeams(String course) {
    return githubBlameRepository.getLineChangesOfTeams(course);
  }

  @Override
  public List<GitFileChangesStats> getLineChangesOfStudents(String course, String team) {
    return githubBlameRepository.getLineChangesOfStudents(course, team);
  }

  private static List<CommitDetailDto> convertObjectToCommitDto(List<Object[]> data) {
    List<CommitDetailDto> commitDetails = new ArrayList<>();
    data.forEach(item -> commitDetails.add(new CommitDetailDto(item[0].toString(), item[1].toString(), LocalDate.parse(item[2].toString()))));
    return commitDetails;
  }
}
