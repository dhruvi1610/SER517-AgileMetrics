package edu.asu.cassess.service.rest;

import edu.asu.cassess.dto.github.FileChangesDto;
import edu.asu.cassess.dto.github.internal.CommitDetailDto;
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
  public Set<String> findDistictCommitIds() {
    return githubBlameRepository.findDistinctCommitIds();
  }

  @Override
  public Set<String> findDistictCommitIdsOfCourse(String course) {
    return githubBlameRepository.findDistinctCommitIdsOfCourse(course);
  }

  private static List<CommitDetailDto> convertObjectToCommitDto(List<Object[]> data) {
    List<CommitDetailDto> commitDetails = new ArrayList<>();
    data.forEach(item -> commitDetails.add(new CommitDetailDto(item[0].toString(), item[1].toString(), LocalDate.parse(item[2].toString()))));
    return commitDetails;
  }
}
