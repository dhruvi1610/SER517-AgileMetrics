package edu.asu.cassess.service.taiga;

import edu.asu.cassess.dto.sprint.AllSprintsDto;
import edu.asu.cassess.dto.sprint.SprintDaysDto;
import edu.asu.cassess.dto.sprint.SprintDto;
import edu.asu.cassess.persist.entity.taiga.TaigaSprint;
import edu.asu.cassess.persist.entity.taiga.TaigaSprintDays;
import edu.asu.cassess.persist.entity.taiga.TaigaSprintDaysId;
import edu.asu.cassess.persist.repo.taiga.ITaigaSprintDaysRepository;
import edu.asu.cassess.persist.repo.taiga.ITaigaSprintRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaigaSprintServiceImpl implements ITaigaSprintService{

  @Autowired
  IProjectService projectService;

  @Autowired
  ITaigaSprintRepository taigaSprintRepository;

  @Autowired
  ITaigaSprintDaysRepository taigaSprintDaysRepository;

  private static final String BASE_URL = "https://api.taiga.io/api/v1/milestones";
  private final RestTemplate restTemplate;

  public TaigaSprintServiceImpl() {
    this.restTemplate = new RestTemplate();
  }

  @Override
  public List<TaigaSprint> getSprintNames(String courseName, String teamName) {
    return taigaSprintRepository.findByCourseNameAndTeamName(courseName, teamName);
  }

  @Override
  public List<TaigaSprintDays> getBurndownDataBySprintId(Long sprintId) {
      return taigaSprintDaysRepository.findByTaigaSprintDaysIdSprintIdOrderByTaigaSprintDaysIdDateAsc(sprintId);
  }

  @Override
  public void saveSprintsOfCourse(String courseName) {
    List<ProjectCourseDto> projectCourseDtos = projectService.getProjectInfoOfActiveCourses(courseName);
    fetchAndPersistProjectSprints(projectCourseDtos, false);
  }

  @Override
  public void updateActiveSprints() {
    List<ProjectCourseDto> projectCourseDtos = projectService.getProjectInfoOfActiveCourses();
    fetchAndPersistProjectSprints(projectCourseDtos, true);
  }



  private void persistTaigaSprintsDetails(ProjectCourseDto projectCourseDto, HttpEntity<String> request, List<AllSprintsDto> allSprints) {
    List<TaigaSprint> taigaSprints = new ArrayList<>();
    List<TaigaSprintDays> taigaSprintDays = new ArrayList<>();
    for(AllSprintsDto allSprintsDto : allSprints) {
      ResponseEntity<SprintDto> responseEntity = null;
      try {
        responseEntity = restTemplate.exchange(BASE_URL + "/" + allSprintsDto.getSprintId() + "/stats", HttpMethod.GET, request, SprintDto.class);
      } catch (Exception e) {
        e.printStackTrace();
      }

      if(responseEntity != null && responseEntity.getBody() != null) {
        TaigaSprint taigaSprint = new TaigaSprint(allSprintsDto.getSprintId(), projectCourseDto.getProjectId(), projectCourseDto.getCourseName(), projectCourseDto.getTeamName(),
            allSprintsDto.getSprintName(), allSprintsDto.getIsClosed(), LocalDate.parse(allSprintsDto.getEstimatedStart()), LocalDate.parse(allSprintsDto.getEstimatedFinish()));
        taigaSprints.add(taigaSprint);

        for (SprintDaysDto item : responseEntity.getBody().getDays()) {
          TaigaSprintDaysId taigaSprintsId = new TaigaSprintDaysId(allSprintsDto.getSprintId(), LocalDate.parse(item.getDate()));
          taigaSprintDays.add(new TaigaSprintDays(taigaSprintsId, item.getActualPoints(), item.getEstimatedPoints()));
        }
      }
    }

    taigaSprintRepository.save(taigaSprints);
    taigaSprintDaysRepository.save(taigaSprintDays);
  }
}
