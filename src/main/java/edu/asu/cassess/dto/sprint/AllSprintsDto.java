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
public class AllSprintsDto {

  @JsonProperty("id")
  private Long sprintId;

  @JsonProperty("name")
  private String sprintName;

  @JsonProperty("closed")
  private Boolean isClosed;

  @JsonProperty("estimated_start")
  private String estimatedStart;

  @JsonProperty("estimated_finish")
  private String estimatedFinish;

  @JsonProperty("user_stories")
  private UserStoryDto[] userStories;

  @JsonProperty("total_points")
  private Double totalPoints;

  @JsonProperty("closed_points")
  private Double closedPoints;
}
