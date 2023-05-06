package edu.asu.cassess.service.github.strategies;

import edu.asu.cassess.dto.github.FileChangesDto;
import edu.asu.cassess.dto.github.external.BlameResponseDto;
import edu.asu.cassess.dto.github.external.CommitDto;
import edu.asu.cassess.persist.entity.github.GithubBlame;
import edu.asu.cassess.persist.entity.github.GithubBlameId;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.service.rest.IGithubBlameService;
import edu.asu.cassess.utils.DateUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubBlameStrategy implements IGithubStrategy {

  public static final String BASE_URL = "https://api.github.com/repos";

  @Autowired
  private IGithubBlameService githubBlameService;

  private RestTemplate restTemplate;

  public GithubBlameStrategy() {
    this.restTemplate = new RestTemplate();
  }

  public GithubBlameStrategy(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public void consumeData(List<Team> teams) {
    List<GithubBlame> result = new ArrayList<>();

    for (Team team : teams) {
      Set<String> commitIdSet = githubBlameService.findDistictCommitIdsOfCourseAndTeam(team.getCourse(), team.getTeam_name());
      String commitUrl = String.format("%s/%s/%s/commits", BASE_URL, team.getGithub_owner(), team.getGithub_repo_id());
      Map<String, String> studentsMap = team.getStudents().stream()
          .collect(Collectors.toMap(Student::getGithub_username, Student::getFull_name));
      List<String> commitIds = getNewCommitIdsOfStudents(commitUrl, studentsMap, commitIdSet);
      for (String commitId : commitIds) {
        try {
          BlameResponseDto blameResponseDto = restTemplate.getForObject(commitUrl + "/" + commitId, BlameResponseDto.class);
          if (blameResponseDto != null) {
            result.addAll(getFileChanges(blameResponseDto, team.getCourse(), team.getTeam_name(), studentsMap));
          }
        } catch (Exception e) {
          System.out.println("Git blame fetch failed. " + e.getMessage());
        }
      }
    }

    githubBlameService.saveMany(result);;
  }

  private List<String> getNewCommitIdsOfStudents(String commitUrl, Map<String, String> studentsMap, Set<String> commitIdSet) {
    List<String> result = new ArrayList<>();
    int itemsPerPage = 100;
    int page = 0;

    boolean hasNextPage = true;
    while(hasNextPage) {
      try {
        String url = String.format("%s?per_page=%d&page=%d", commitUrl, itemsPerPage, page);
        CommitDto[] commits = restTemplate.getForObject(url, CommitDto[].class);
        for (CommitDto commit : Objects.requireNonNull(commits)) {
          if(commit.getCommitter() != null) {
            String username = commit.getCommitter().getUsername();
            if (studentsMap.containsKey(username) && !commitIdSet.contains(commit.getCommitId())) {
              result.add(commit.getCommitId());
            }
          }
        }

        if(commits.length < itemsPerPage) {
          hasNextPage = false;
        }
        page = page + 1;
      } catch (Exception e) {
        System.out.println("Git commit fetch failed. " + e.getMessage());
        hasNextPage = false;
      }
    }

    return result;
  }

  private List<GithubBlame> getFileChanges(BlameResponseDto blameResponseDto, String course, String team, Map<String, String> studentsMap) {
    List<GithubBlame> result = new ArrayList<>();
    String username = blameResponseDto.getCommitter().getUsername();
    String fullName = studentsMap.get(username);
    String commitMsg = blameResponseDto.getCommitInfo().getMessage();
    LocalDate commitDate = DateUtil.stringToDate(blameResponseDto.getCommitInfo().getAuthor().getDate());
    for (FileChangesDto file : blameResponseDto.getFiles()) {
      GithubBlameId githubBlameId = new GithubBlameId(blameResponseDto.getCommitId(), file.getFilename());
      GithubBlame githubBlame = new GithubBlame(githubBlameId, course, team, username, commitDate,
          file.getStatus(), commitMsg, fullName, file.getAdditions(), file.getDeletions(), file.getPatch());
      result.add(githubBlame);
    }
    return result;
  }
}
