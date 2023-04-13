package edu.asu.cassess.persist.repo.github;

import edu.asu.cassess.persist.entity.github.GithubBlame;
import edu.asu.cassess.persist.entity.github.GithubBlameId;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IGithubBlameRepository extends JpaRepository<GithubBlame, GithubBlameId> {

  List<GithubBlame> findByGithubBlameIdCommitId(String commit);

  @Query(value = "SELECT distinct(commit_id), commit_message, commit_date FROM github_blame where username = ?1 order by commit_date desc", nativeQuery = true)
  List<Object[]> findDistinctCommitsByUsername(String username);

  @Query(value = "SELECT distinct(commit_id), commit_message, commit_date FROM github_blame where full_name = ?1 order by commit_date desc", nativeQuery = true)
  List<Object[]> findDistinctCommitsByFullName(String fullname);

  @Query("SELECT distinct(githubBlameId.commitId) FROM GithubBlame")
  Set<String> findDistinctCommitIds();

  @Query("SELECT distinct(githubBlameId.commitId) FROM GithubBlame WHERE course = ?1")
  Set<String> findDistinctCommitIdsOfCourse(String course);
}