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
public class CommitterDto {

  @JsonProperty("name")
  private String username;

  @JsonProperty("email")
  private String email;

  @JsonProperty("date")
  private String date;
}
