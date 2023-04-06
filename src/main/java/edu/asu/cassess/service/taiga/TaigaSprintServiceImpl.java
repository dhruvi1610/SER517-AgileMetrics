package edu.asu.cassess.service.taiga;

import edu.asu.cassess.dto.ProjectCourseDto;
import edu.asu.cassess.dto.sprint.*;
import edu.asu.cassess.persist.entity.taiga.TaigaSprint;
import edu.asu.cassess.persist.entity.taiga.TaigaSprintDays;
import edu.asu.cassess.persist.entity.taiga.TaigaSprintDaysId;
import edu.asu.cassess.persist.repo.taiga.ITaigaSprintDaysRepository;
import edu.asu.cassess.persist.repo.taiga.ITaigaSprintRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  private static final String ALL_CUSTOM_ATT_BASE_URL = "https://api.taiga.io/api/v1/userstory-custom-attributes?project=";

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

  private void fetchAndPersistProjectSprints(List<ProjectCourseDto> projectCourseDtos, boolean isUpdateActiveSprints) {
    for (ProjectCourseDto projectCourseDto: projectCourseDtos) {
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(projectCourseDto.getTaigaToken());
      HttpEntity<String> request = new HttpEntity<>(headers);
      ResponseEntity<AllSprintsDto[]> responseEntity = null;
      try {
        responseEntity = restTemplate.exchange(BASE_URL + "?project=" + projectCourseDto.getProjectId(), HttpMethod.GET, request, AllSprintsDto[].class);
      } catch (Exception e) {
        e.printStackTrace();
      }

      if(responseEntity != null && responseEntity.getBody() != null) {
        List<AllSprintsDto> allSprints = Arrays.asList(responseEntity.getBody());
        if(isUpdateActiveSprints) {
          allSprints = allSprints.stream().filter(item -> !item.getIsClosed()).collect(Collectors.toList());
        }
        persistTaigaSprintsDetails(projectCourseDto, request, allSprints);
      }
    }
  }

  private void persistTaigaSprintsDetails(ProjectCourseDto projectCourseDto, HttpEntity<String> request, List<AllSprintsDto> allSprints) {
    List<TaigaSprint> taigaSprints = new ArrayList<>();
    List<TaigaSprintDays> taigaSprintDays = new ArrayList<>();
    for(AllSprintsDto sprintDto : allSprints) {
      ResponseEntity<SprintDto> responseEntity = null;
      try {
        responseEntity = restTemplate.exchange(BASE_URL + "/" + sprintDto.getSprintId() + "/stats", HttpMethod.GET, request, SprintDto.class);
      } catch (Exception e) {
        e.printStackTrace();
      }

      if(responseEntity != null && responseEntity.getBody() != null) {
        TaigaSprint taigaSprint = new TaigaSprint(sprintDto.getSprintId(), projectCourseDto.getProjectId(), projectCourseDto.getCourseName(), projectCourseDto.getTeamName(),
            sprintDto.getSprintName(), sprintDto.getIsClosed(), LocalDate.parse(sprintDto.getEstimatedStart()), LocalDate.parse(sprintDto.getEstimatedFinish()));
        taigaSprints.add(taigaSprint);

        Double points = sprintDto.getTotalPoints();
        Map<LocalDate, Double> fullBurndownMap = populatefullBurndownMap(sprintDto.getUserStories());
        for (SprintDaysDto item : responseEntity.getBody().getDays()) {
          LocalDate date = LocalDate.parse(item.getDate());
          points = fullBurndownMap.containsKey(date) ? points - fullBurndownMap.get(date) : points;
          TaigaSprintDaysId taigaSprintsId = new TaigaSprintDaysId(sprintDto.getSprintId(), date);
          //taigaSprintDays.add(new TaigaSprintDays(taigaSprintsId, item.getActualPoints(), item.getEstimatedPoints(), points));
        }
      }
    }

    taigaSprintRepository.save(taigaSprints);
    taigaSprintDaysRepository.save(taigaSprintDays);
  }

  private Map<LocalDate, Double> populatefullBurndownMap(UserStoryDto[] userStories) {
    Map<LocalDate, Double> map = new HashMap<>();

    for(UserStoryDto userStory : userStories) {
      if (userStory.getIsClosed()) {
        Instant instant = Instant.parse(userStory.getFinishDate());
        LocalDate finishedDate = LocalDate.ofInstant(instant, ZoneOffset.UTC);

        if (map.containsKey(finishedDate)) {
          map.put(finishedDate, map.get(finishedDate) + userStory.getPoints());
        } else {
          map.put(finishedDate, userStory.getPoints());
        }
      }
    }

    return map;
  }
  private Map<String, Long> getAllCustomAttributes(Long projectId, HttpEntity<String> request) {
    Map<String, Long> resultMap = new HashMap<>();
    ResponseEntity<CustomAttributesDto[]> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(ALL_CUSTOM_ATT_BASE_URL + projectId, HttpMethod.GET, request, CustomAttributesDto[].class);
      if(responseEntity.getBody() != null) {
        for(CustomAttributesDto item : responseEntity.getBody()) {
          resultMap.put(item.getAttributeName(), item.getAttributeId());
        }
      }
      return  resultMap;
    } catch (Exception e) {
      e.printStackTrace();
      return resultMap;
    }
  }



}
