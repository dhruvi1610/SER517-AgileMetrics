package edu.asu.cassess.service.taiga;

import edu.asu.cassess.model.Taiga.Slugs;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.taiga.Project;
import edu.asu.cassess.persist.repo.taiga.ProjectRepo;
import edu.asu.cassess.service.rest.ICourseService;
import edu.asu.cassess.service.rest.ITeamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional
public class ProjectService implements IProjectService {

    private RestTemplate restTemplate;
    private String projectURL;

    @Autowired
    private ProjectRepo projectStoreDao;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ITeamsService teamsService;


    public ProjectService() {
        restTemplate = new RestTemplate();
        projectURL = "https://api.taiga.io/api/v1/projects/by_slug?slug=";

    }

    @Override
    public Project getProjectInfo(String token, String slug) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        //Console Output for testing purposes
        System.out.println("Fetching from " + projectURL);

        ResponseEntity<Project> project = restTemplate.getForEntity(projectURL + slug, Project.class, request);

        return projectStoreDao.save(project.getBody());
    }

    /* Method to provide single operation on
    updating the projects table based on the course and student tables
     */
    @Override
    public void updateProjects(String course) {
        System.out.println("Updating Projects");
        if (courseService.read(course).getClass() == Course.class) {
            Course tempCourse = (Course) courseService.read(course);
            String token = tempCourse.getTaiga_token();
            List<Slugs> slugList = teamsService.listGetSlugs(course);
            if (token != null && !slugList.isEmpty()) {
                for (Slugs slug : slugList) {
                    System.out.println("Slug: " + slug);
                    getProjectInfo(token, slug.getSlug());
                }
            }
        }

    }
}