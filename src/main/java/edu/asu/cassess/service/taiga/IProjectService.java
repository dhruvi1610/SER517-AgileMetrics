package edu.asu.cassess.service.taiga;

import edu.asu.cassess.dto.ProjectCourseDto;
import edu.asu.cassess.persist.entity.taiga.Project;
import java.util.List;

/**
 * Created by Thomas on 4/11/2017.
 */
public interface IProjectService {
    Project getProjectInfo(String token, String slug);

    /* Method to provide single operation on
        updating the projects table based on the course and student tables
         */
    void updateProjects(String course);

    List<ProjectCourseDto> getProjectInfoOfActiveCourses();

    List<ProjectCourseDto> getProjectInfoOfActiveCourses(String courseName);
}
