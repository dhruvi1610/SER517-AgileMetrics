package edu.asu.cassess.persist.entity.taiga;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "taiga_sprint_days")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaigaSprintDays {

  @EmbeddedId
  private TaigaSprintDaysId taigaSprintDaysId;

  @Column(name = "actual_points")
  private Double actualPoints;

  @Column(name = "estimated_points")
  private Double estimatedPoints;

  @Column(name = "full_burndown_points")
  private Double fullBurndownPoints;

  @Column(name = "custom_attribute_points")
  private Double customAttributePoints;

  @Column(name = "new_tasks")
  private Integer newTasks;

  @Column(name = "in_progress_tasks")
  private Integer inProgressTasks;

  @Column(name = "closed_tasks")
  private Integer closedTasks;
}