package edu.asu.cassess.dto.sprint;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

  @JsonProperty("id")
  private Long taskId;

  @JsonProperty("milestone")
  private Long sprintId;

  @JsonProperty("created_date")
  private String createdDate;

  @JsonProperty("finished_date")
  private String finishedDate;

  @JsonProperty("subject")
  private String taskName;

  @JsonProperty("is_closed")
  private Boolean isClosed;
}
