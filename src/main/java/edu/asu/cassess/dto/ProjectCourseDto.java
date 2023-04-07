package edu.asu.cassess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCourseDto {
  private String teamName;
  private String courseName;
  private Long projectId;
  private String taigaToken;
  private String taigaCustomAttribute;

}
