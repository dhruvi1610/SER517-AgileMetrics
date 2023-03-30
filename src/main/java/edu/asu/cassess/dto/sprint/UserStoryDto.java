package edu.asu.cassess.dto.sprint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserStoryDto {

  @JsonProperty("is_closed")
  private Boolean isClosed;

  @JsonProperty("finish_date")
  private String finishDate;

  @JsonProperty("total_points")
  private Double points;
}
