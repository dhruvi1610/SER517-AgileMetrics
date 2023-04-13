package edu.asu.cassess.dto.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileChangesDto {

  @JsonProperty("filename")
  private String filename;

  @JsonProperty("status")
  private String status;

  @JsonProperty("additions")
  private Integer additions;

  @JsonProperty("deletions")
  private Integer deletions;

  @JsonProperty("patch")
  private String patch;
}
