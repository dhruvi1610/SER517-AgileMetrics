package edu.asu.cassess.service.taiga;

import edu.asu.cassess.persist.entity.taiga.Project;

/**
 * Created by Thomas on 4/11/2017.
 */
public interface IProjectService {
    Project getProjectInfo(String token, String slug);

    /* Method to provide single operation on
        updating the projects table based on the course and student tables
         */
    void updateProjects(String course);
}
