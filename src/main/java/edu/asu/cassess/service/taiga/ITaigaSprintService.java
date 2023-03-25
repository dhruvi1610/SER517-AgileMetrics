package edu.asu.cassess.service.taiga;

import edu.asu.cassess.persist.entity.taiga.TaigaSprint;
import edu.asu.cassess.persist.entity.taiga.TaigaSprintDays;
import java.util.List;

public interface ITaigaSprintService {

  List<TaigaSprint> getSprintNames(String courseName, String teamName);

  List<TaigaSprintDays> getBurndownDataBySprintId(Long sprintId);

  void saveSprintsOfCourse(String courseName);

  void updateActiveSprints();
}
