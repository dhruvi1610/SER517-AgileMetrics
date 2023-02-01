package edu.asu.cassess.service.taiga;

import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.taiga.TaskData;

/**
 * Created by Thomas on 4/11/2017.
 */
public interface ITaskDataService {
    TaskData[] getTasks(Long id, String token, int page);

    void getTaskTotals(String slug, Course course, Team team);

    /* Method to obtain all task totals for members of a particular course,
                occurring on a schedule
             */
    void updateTaskTotals(String course);
}
