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
public class CommitInfoDto {

  @JsonProperty("committer")
  private CommitterDto committer;

  @JsonProperty("message")
  private String message;
}
