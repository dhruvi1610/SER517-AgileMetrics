package edu.asu.cassess.persist.entity.github;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class GithubBlameId implements Serializable {

  @Column(name = "commit_id")
  private String commitId;

  @Column(name = "filename")
  private String filename;
}