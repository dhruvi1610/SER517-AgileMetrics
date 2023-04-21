package edu.asu.cassess.dto.github.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommitDto {

  @JsonProperty("sha")
  private String commitId;

  @JsonProperty("commit")
  private CommitInfoDto commitInfo;

  @JsonProperty("committer")
  private CommitterDto committer;
}
