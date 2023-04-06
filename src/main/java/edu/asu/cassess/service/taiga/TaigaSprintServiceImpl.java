package edu.asu.cassess.service.taiga;

import edu.asu.cassess.dto.ProjectCourseDto;
import edu.asu.cassess.dto.sprint.AllSprintsDto;
import edu.asu.cassess.dto.sprint.CustomAttributesDto;
import edu.asu.cassess.dto.sprint.CustomAttributesValuesDto;
import edu.asu.cassess.dto.sprint.PointsDto;
import edu.asu.cassess.dto.sprint.SprintDaysDto;
import edu.asu.cassess.dto.sprint.SprintDto;
import edu.asu.cassess.dto.sprint.UserStoryDto;
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
import java.util.Objects;
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
  private static final String DEBT_ESTIMATION = "TechnicalDebtEstimation";
  private static final String SPRINT_BASE_URL = "https://api.taiga.io/api/v1/milestones";
  private static final String CUSTOM_ATT_BASE_URL = "https://api.taiga.io/api/v1/userstories/custom-attributes-values/";
  private static final String ALL_CUSTOM_ATT_BASE_URL = "https://api.taiga.io/api/v1/userstory-custom-attributes?project=";
  private final RestTemplate restTemplate;

  public TaigaSprintServiceImpl() {
    this.restTemplate = new RestTemplate();
  }

  @Override
  public List<TaigaSprint> getSprintNames(String courseName, String teamName) {
    return taigaSprintRepository.findByCourseNameAndTeamNameOrderBySprintNameDesc(courseName, teamName);

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
        responseEntity = restTemplate.exchange(SPRINT_BASE_URL + "?project=" + projectCourseDto.getProjectId(), HttpMethod.GET, request, AllSprintsDto[].class);
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
        responseEntity = restTemplate.exchange(SPRINT_BASE_URL + "/" + sprintDto.getSprintId() + "/stats", HttpMethod.GET, request, SprintDto.class);
      } catch (Exception e) {
        e.printStackTrace();
      }

      if(responseEntity != null && responseEntity.getBody() != null) {
        TaigaSprint taigaSprint = new TaigaSprint(sprintDto.getSprintId(), projectCourseDto.getProjectId(), projectCourseDto.getCourseName(), projectCourseDto.getTeamName(),
            sprintDto.getSprintName(), sprintDto.getIsClosed(), LocalDate.parse(sprintDto.getEstimatedStart()), LocalDate.parse(sprintDto.getEstimatedFinish()));
        taigaSprints.add(taigaSprint);

        Double fullburndownPoints = sprintDto.getTotalPoints();
        Double customAttributePoints = sprintDto.getTotalPoints();
        Map<LocalDate, PointsDto> userStoryMap = populateUserStoryMap(sprintDto.getUserStories(), projectCourseDto.getProjectId(), request);
        for (SprintDaysDto item : responseEntity.getBody().getDays()) {
          LocalDate date = LocalDate.parse(item.getDate());
          if(userStoryMap.containsKey(date)) {
            fullburndownPoints -= userStoryMap.get(date).getFullBurndownPoints();
            customAttributePoints -= userStoryMap.get(date).getCustomAttributePoints();
          }
          TaigaSprintDaysId taigaSprintsId = new TaigaSprintDaysId(sprintDto.getSprintId(), date);
          taigaSprintDays.add(new TaigaSprintDays(taigaSprintsId, item.getActualPoints(), item.getEstimatedPoints(), fullburndownPoints, customAttributePoints));
        }
      }
    }

    taigaSprintRepository.save(taigaSprints);
    taigaSprintDaysRepository.save(taigaSprintDays);
  }

  private Map<LocalDate, PointsDto> populateUserStoryMap(UserStoryDto[] userStories, Long projectId, HttpEntity<String> request) {
    Map<LocalDate, PointsDto> resultMap = new HashMap<>();
    Map<String, Long> allAttributesMap = getAllCustomAttributes(projectId, request);

    for(UserStoryDto userStory : userStories) {
      if (Boolean.TRUE.equals(userStory.getIsClosed())) {
        Instant instant = Instant.parse(userStory.getFinishDate());
        LocalDate finishedDate = LocalDate.ofInstant(instant, ZoneOffset.UTC);
        Double debtEstimationValue = getDebtEstimationValue(userStory.getStoryId(), allAttributesMap.get(DEBT_ESTIMATION), request);

        if(!resultMap.containsKey(finishedDate)) {
          resultMap.put(finishedDate, new PointsDto(0.0, 0.0));
        }
        PointsDto pointsDto = resultMap.get(finishedDate);
        pointsDto.setFullBurndownPoints(pointsDto.getFullBurndownPoints() + userStory.getPoints());
        pointsDto.setCustomAttributePoints(pointsDto.getCustomAttributePoints() + debtEstimationValue);
      }
    }

    return resultMap;
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

  private Double getDebtEstimationValue(Long storyId, Long debtEstimationId, HttpEntity<String> request) {
    ResponseEntity<CustomAttributesValuesDto> responseEntity = null;
    Double result = 0.0;
    try {
      responseEntity = restTemplate.exchange(CUSTOM_ATT_BASE_URL + storyId, HttpMethod.GET, request, CustomAttributesValuesDto.class);
      if(responseEntity.getBody() != null) {
        result = responseEntity.getBody().getCustomAttributesValues().get(debtEstimationId);
      }
      return Objects.isNull(result) ? 0.0 : result;
    } catch (Exception e) {
      e.printStackTrace();
      return result;
    }
  }
}
