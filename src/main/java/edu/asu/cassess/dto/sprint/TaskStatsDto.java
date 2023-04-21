package edu.asu.cassess.dto.sprint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatsDto {

  private int newTasks;
  private int inProgressTasks;
  private int readyToTestTasks;
  private int closedTasks;
}
