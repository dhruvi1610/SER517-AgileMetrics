package edu.asu.cassess.dto.sprint;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskHistoryDto {

  @JsonProperty("created_at")
  private String statusChangeDate;

  @JsonProperty("values_diff")
  private TaskStatusChangeDto statusChange;
}

