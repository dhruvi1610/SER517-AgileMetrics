package edu.asu.cassess.dto.sprint;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusDateDto {

  private Long taskId;
  private Long sprintId;
  private LocalDate createdDate;
  private LocalDate inProgressDate;
  private LocalDate readyToTestDate;
  private LocalDate closedDate;

  public TaskStatusDateDto(Long taskId, Long sprintId, LocalDate createdDate) {
    this.taskId = taskId;
    this.sprintId = sprintId;
    this.createdDate = createdDate;
  }
}
