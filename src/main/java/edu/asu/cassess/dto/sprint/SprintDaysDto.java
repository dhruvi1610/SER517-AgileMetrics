package edu.asu.cassess.dto.sprint;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SprintDaysDto {

  @JsonProperty("day")
//  @JsonFormat(pattern = "yyyy-MM-dd")
  private String date;

  @JsonProperty("open_points")
  private Double actualPoints;

  @JsonProperty("optimal_points")
  private Double estimatedPoints;
}
