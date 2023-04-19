package edu.asu.cassess.service.taiga;

import edu.asu.cassess.persist.entity.taiga.TaigaSprint;
import edu.asu.cassess.persist.entity.taiga.TaigaSprintDays;
import java.util.List;

public interface ITaigaSprintService {

  List<TaigaSprint> getSprints(String courseName, String teamName);

  List<TaigaSprintDays> getSprintDaysBySprintId(Long sprintId);

  void saveSprintsOfCourse(String courseName);

  void updateActiveSprints();
  void saveTaskHistoryOfCourse(String courseName);
  void updateTaskHistoryofActiveSprints();
}
