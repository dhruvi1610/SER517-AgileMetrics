package edu.asu.cassess.service.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.asu.cassess.dto.github.FileChangesDto;
import edu.asu.cassess.persist.entity.github.GithubBlame;
import edu.asu.cassess.persist.entity.github.GithubBlameId;
import edu.asu.cassess.persist.repo.github.IGithubBlameRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GithubBlameServiceTest {

  @Mock
  IGithubBlameRepository githubBlameRepository;

  @InjectMocks
  GithubBlameService githubBlameService;

  List<GithubBlame> repoResponse = new ArrayList<>();

  @BeforeEach
  void setUp() {
    repoResponse.add(new GithubBlame(new GithubBlameId("commitId1", "src/file1.java"), "SER 517",
        "Test Team", "sbaktha", LocalDate.now(), "Added", "Added file1",
        "Srikanth", 30, 0, "@@public Main class{}"));
    repoResponse.add(new GithubBlame(new GithubBlameId("commitId1", "src/file2.java"), "SER 517",
        "Test Team", "Dhruvi", LocalDate.now(), "Modified", "Modified file2",
        "Dhruvi Modi", 20, 5, "@@public static Main(){}"));
  }

  @Test
  @DisplayName("JUnit test for fetch file changes of a commit - Size Check")
  void findFileChangesOfCommitSizeCheck() {
    when(githubBlameRepository.findByGithubBlameIdCommitId("commitId1")).thenReturn(repoResponse);

    assertEquals(2, githubBlameService.findFileChangesOfCommit("commitId1").size());
  }

  @Test
  @DisplayName("JUnit test for fetch file changes of a commit - Objects Check")
  void findFileChangesOfCommitObjectsCheck() {
    when(githubBlameRepository.findByGithubBlameIdCommitId("commitId1")).thenReturn(repoResponse);

    List<FileChangesDto> expected = new ArrayList<>();
    expected.add(new FileChangesDto("src/file1.java", "Added", 30, 0, "@@public Main class{}"));
    expected.add(new FileChangesDto("src/file2.java", "Modified", 20, 5, "@@public static Main(){}"));

    assertEquals(expected, githubBlameService.findFileChangesOfCommit("commitId1"));
  }
}
