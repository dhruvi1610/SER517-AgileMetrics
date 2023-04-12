package edu.asu.cassess.persist.entity.github;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "github_blame")
public class GithubBlame {

  @EmbeddedId
  private GithubBlameId githubBlameId;

  @Column(name = "course")
  private String course;

  @Column(name = "team")
  private String team;

  @Column(name = "username")
  private String username;

  @Column(name = "commit_date")
  private LocalDate commitDate;

  @Column(name = "commit_status")
  private String commitStatus;

  @Column(name = "commit_message")
  private String commitMessage;

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "lines_of_code_added")
  private Integer linesOfCodeAdded;

  @Column(name = "lines_of_code_deleted")
  private Integer linesOfCodeDeleted;

  @Column(name = "patch")
  private String patch;
}