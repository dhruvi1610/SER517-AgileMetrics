package edu.asu.cassess.service.taiga;

import edu.asu.cassess.dto.ProjectCourseDto;
import edu.asu.cassess.dto.sprint.AllSprintsDto;
import edu.asu.cassess.dto.sprint.CustomAttributesDto;
import edu.asu.cassess.dto.sprint.CustomAttributesValuesDto;
import edu.asu.cassess.dto.sprint.PointsDto;
import edu.asu.cassess.dto.sprint.SprintDaysDto;
import edu.asu.cassess.dto.sprint.SprintDto;
import edu.asu.cassess.dto.sprint.TaskDto;
import edu.asu.cassess.dto.sprint.TaskStatsDto;
import edu.asu.cassess.dto.sprint.TaskStatusDateDto;
import edu.asu.cassess.dto.sprint.TaskHistoryDto;
import edu.asu.cassess.dto.sprint.UserStoryDto;
import edu.asu.cassess.persist.entity.taiga.TaigaSprint;
import edu.asu.cassess.persist.entity.taiga.TaigaSprintDays;
import edu.asu.cassess.persist.entity.taiga.TaigaSprintDaysId;
import edu.asu.cassess.persist.repo.taiga.ITaigaSprintDaysRepository;
import edu.asu.cassess.persist.repo.taiga.ITaigaSprintRepository;
import edu.asu.cassess.utils.DateUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
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
  private static final String SPRINT_BASE_URL = "https://api.taiga.io/api/v1/milestones";
  private static final String CUSTOM_ATT_BASE_URL = "https://api.taiga.io/api/v1/userstories/custom-attributes-values/";
  private static final String ALL_CUSTOM_ATT_BASE_URL = "https://api.taiga.io/api/v1/userstory-custom-attributes?project=";
  private static final String PROJECT_TASKS_URL = "https://api.taiga.io/api/v1/tasks?project=";
  private static final String TASK_HISTORY_URL = "https://api.taiga.io/api/v1/history/task/";
  private final RestTemplate restTemplate;

  private Map<String, Long> allCustomAttributes;
  private boolean sprintHasCustomAttribute;
  private Double totalCustomAttributeValue;

  public TaigaSprintServiceImpl() {
    this.restTemplate = new RestTemplate();
  }

  @Override
  public List<TaigaSprint> getSprints(String courseName, String teamName) {
    return taigaSprintRepository.findByCourseNameAndTeamNameOrderBySprintNameDesc(courseName, teamName);
  }

  @Override
  public List<TaigaSprintDays> getSprintDaysBySprintId(Long sprintId) {
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

  @Override
  public void saveTaskHistoryOfCourse(String courseName) {
    List<ProjectCourseDto> projectCourseDtos = projectService.getProjectInfoOfActiveCourses(courseName);
    persistTaskHistory(projectCourseDtos, false);
  }

  @Override
  public void updateTaskHistoryofActiveSprints() {
    List<ProjectCourseDto> projectCourseDtos = projectService.getProjectInfoOfActiveCourses();
    persistTaskHistory(projectCourseDtos, true);
  }

  private void fetchAndPersistProjectSprints(List<ProjectCourseDto> projectCourseDtos, boolean isUpdateActiveSprints) {
    Map<TaigaSprintDaysId, TaigaSprintDays> sprintDaysMap = taigaSprintDaysRepository.findAll().stream()
            .collect(Collectors.toMap(TaigaSprintDays::getTaigaSprintDaysId, item -> item));;
    for (ProjectCourseDto projectCourseDto: projectCourseDtos) {
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(projectCourseDto.getTaigaToken());
      headers.add("x-disable-pagination", "True");
      HttpEntity<String> request = new HttpEntity<>(headers);

      List<AllSprintsDto> allSprints = getAllSprintsOfProject(projectCourseDto, request);
      if(isUpdateActiveSprints) {
        allSprints = allSprints.stream().filter(item -> !item.getIsClosed()).collect(Collectors.toList());
      }
      persistTaigaSprintsDetails(projectCourseDto, request, allSprints, sprintDaysMap);
    }
  }

  private List<AllSprintsDto> getAllSprintsOfProject(ProjectCourseDto projectCourseDto, HttpEntity<String> request) {
    List<AllSprintsDto> result = new ArrayList<>();
    ResponseEntity<AllSprintsDto[]> responseEntity = null;
    try {
      responseEntity = restTemplate.exchange(SPRINT_BASE_URL + "?project=" + projectCourseDto.getProjectId(), HttpMethod.GET, request, AllSprintsDto[].class);
      if (responseEntity.getBody() != null) {
        result = List.of(responseEntity.getBody());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return  result;
  }

  private void persistTaigaSprintsDetails(ProjectCourseDto projectCourseDto, HttpEntity<String> request, List<AllSprintsDto> allSprints,
                                          Map<TaigaSprintDaysId, TaigaSprintDays> sprintDaysMap) {
    List<TaigaSprint> taigaSprints = new ArrayList<>();
    List<TaigaSprintDays> taigaSprintDays = new ArrayList<>();
    allCustomAttributes = getAllCustomAttributes(projectCourseDto.getProjectId(), request);
    for(AllSprintsDto sprintDto : allSprints) {
      ResponseEntity<SprintDto> responseEntity = null;
      try {
        responseEntity = restTemplate.exchange(SPRINT_BASE_URL + "/" + sprintDto.getSprintId() + "/stats", HttpMethod.GET, request, SprintDto.class);
      } catch (Exception e) {
        e.printStackTrace();
      }

      if(responseEntity != null && responseEntity.getBody() != null) {
        Map<LocalDate, PointsDto> userStoryMap = populateUserStoryMap(sprintDto.getUserStories(), projectCourseDto.getTaigaCustomAttribute(), request);
        Double fullburndownPoints = sprintDto.getTotalPoints();
        Double customAttributePoints = totalCustomAttributeValue;
        for (SprintDaysDto item : responseEntity.getBody().getDays()) {
          LocalDate date = LocalDate.parse(item.getDate());
          if(userStoryMap.containsKey(date)) {
            fullburndownPoints -= userStoryMap.get(date).getFullBurndownPoints();
            customAttributePoints -= userStoryMap.get(date).getCustomAttributePoints();
          }
          TaigaSprintDaysId taigaSprintsId = new TaigaSprintDaysId(sprintDto.getSprintId(), date);
          TaigaSprintDays taigaSprint = sprintDaysMap.getOrDefault(taigaSprintsId, new TaigaSprintDays());
          taigaSprint.setTaigaSprintDaysId(taigaSprintsId);
          taigaSprint.setActualPoints(item.getActualPoints());
          taigaSprint.setEstimatedPoints(item.getEstimatedPoints());
          taigaSprint.setFullBurndownPoints(fullburndownPoints);
          taigaSprint.setCustomAttributePoints(customAttributePoints);
          taigaSprintDays.add(taigaSprint);
        }

        TaigaSprint taigaSprint = new TaigaSprint(sprintDto.getSprintId(), projectCourseDto.getProjectId(), projectCourseDto.getCourseName(), projectCourseDto.getTeamName(),
                sprintDto.getSprintName(), sprintDto.getIsClosed(), LocalDate.parse(sprintDto.getEstimatedStart()), LocalDate.parse(sprintDto.getEstimatedFinish()), sprintHasCustomAttribute);
        taigaSprints.add(taigaSprint);
      }
    }

    taigaSprintRepository.save(taigaSprints);
    taigaSprintDaysRepository.save(taigaSprintDays);
  }

  private Map<LocalDate, PointsDto> populateUserStoryMap(UserStoryDto[] userStories, String customAttribute, HttpEntity<String> request) {
    Map<LocalDate, PointsDto> resultMap = new HashMap<>();
    sprintHasCustomAttribute = false;
    totalCustomAttributeValue = 0.0;

    for(UserStoryDto userStory : userStories) {
      if (Boolean.TRUE.equals(userStory.getIsClosed())) {
        LocalDate finishedDate = DateUtil.stringToDate(userStory.getFinishDate());
        Double customAttributeValue = getCustomAttributeValue(userStory.getStoryId(), customAttribute, request);

        if(customAttributeValue == null) {
          customAttributeValue = 0.0;
        } else {
          sprintHasCustomAttribute = true;
        }

        totalCustomAttributeValue += customAttributeValue;
        if(!resultMap.containsKey(finishedDate)) {
          resultMap.put(finishedDate, new PointsDto(0.0, 0.0));
        }
        PointsDto pointsDto = resultMap.get(finishedDate);
        pointsDto.setFullBurndownPoints(pointsDto.getFullBurndownPoints() + userStory.getPoints());
        pointsDto.setCustomAttributePoints(pointsDto.getCustomAttributePoints() + customAttributeValue);
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

  private Double getCustomAttributeValue(Long storyId, String customAttribute, HttpEntity<String> request) {
    if(Objects.isNull(customAttribute)) {
      return null;
    }
    ResponseEntity<CustomAttributesValuesDto> responseEntity = null;
    try {
      Double result = null;
      responseEntity = restTemplate.exchange(CUSTOM_ATT_BASE_URL + storyId, HttpMethod.GET, request, CustomAttributesValuesDto.class);
      if(responseEntity.getBody() != null) {
        result = responseEntity.getBody().getCustomAttributesValues().get(allCustomAttributes.get(customAttribute));
      }
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private void persistTaskHistory(List<ProjectCourseDto> projectCourseDtos, boolean isUpdate) {
    Set<Long> activeSprintIds =  taigaSprintRepository.findActiveSprintIds();
    for (ProjectCourseDto projectCourseDto: projectCourseDtos) {
      if(!Objects.equals(projectCourseDto.getCourseName(), "Test Course")) continue;
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(projectCourseDto.getTaigaToken());
      headers.add("x-disable-pagination", "True");
      HttpEntity<String> request = new HttpEntity<>(headers);

      List<TaskDto> tasks = getAllTasksOfProject(projectCourseDto.getProjectId(), request);
      if(isUpdate) {
        tasks = tasks.stream().filter(item -> activeSprintIds.contains(item.getSprintId())).collect(Collectors.toList());
      }
      List<TaskStatusDateDto> taskStatusDates = tasks.stream()
              .map(item -> getTaskHistory(item, request)).collect(Collectors.toList());
      Map<Long, List<TaskStatusDateDto>> sprintTasksMap = taskStatusDates.stream()
              .collect(Collectors.groupingBy(TaskStatusDateDto::getSprintId));
      Map<TaigaSprintDaysId, TaskStatsDto> taskStatsMap = updateSprintTasks(sprintTasksMap);

      List<TaigaSprintDays> result = new ArrayList<>();
      Map<TaigaSprintDaysId, TaigaSprintDays> sprintDaysMap = taigaSprintDaysRepository.findAll().stream()
              .collect(Collectors.toMap(TaigaSprintDays::getTaigaSprintDaysId, item -> item));;
      taskStatsMap.keySet().forEach(key -> {
        TaigaSprintDays sprintDays = sprintDaysMap.get(key);
        TaskStatsDto taskStats = taskStatsMap.get(key);
        sprintDays.setTaigaSprintDaysId(key);
        sprintDays.setNewTasks(taskStats.getNewTasks());
        sprintDays.setInProgressTasks(taskStats.getInProgressTasks());
        sprintDays.setClosedTasks(taskStats.getClosedTasks());
        result.add(sprintDays);
      });
      taigaSprintDaysRepository.save(result);
    }
  }

  private List<TaskDto> getAllTasksOfProject(Long projectId, HttpEntity<String> request) {
    List<TaskDto> result = new ArrayList<>();
    ResponseEntity<TaskDto[]> responseEntity = null;

    try {
      responseEntity = restTemplate.exchange(PROJECT_TASKS_URL + projectId, HttpMethod.GET, request, TaskDto[].class);
      if(responseEntity.getBody() != null) {
        result = List.of(responseEntity.getBody());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return result;
  }

  private TaskStatusDateDto getTaskHistory(TaskDto task, HttpEntity<String> request) {
    ResponseEntity<TaskHistoryDto[]> responseEntity = null;
    TaskStatusDateDto taskStatusDate = new TaskStatusDateDto(task.getTaskId(), task.getSprintId(), DateUtil.stringToDate(task.getCreatedDate()));
    if(Boolean.TRUE.equals(task.getIsClosed())) {
      taskStatusDate.setClosedDate(DateUtil.stringToDate(task.getFinishedDate()));
    }
    List<TaskHistoryDto> taskHistories = new ArrayList<>();
    try {
      responseEntity = restTemplate.exchange(TASK_HISTORY_URL + task.getTaskId(), HttpMethod.GET, request, TaskHistoryDto[].class);
      if(responseEntity.getBody() != null) {
        taskHistories = List.of(responseEntity.getBody());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (TaskHistoryDto taskHistory : taskHistories) {
      String[] status = null;
      if(taskHistory.getStatusChange() != null) {
        status = taskHistory.getStatusChange().getStatus();
      }
      if(status != null && status[1] != null) {
        if (status[1].equalsIgnoreCase("In Progress")) {
          taskStatusDate.setInProgressDate(DateUtil.stringToDate(taskHistory.getStatusChangeDate()));
        } else if (status[1].equalsIgnoreCase("Ready For Test")) {
          taskStatusDate.setReadyToTestDate(DateUtil.stringToDate(taskHistory.getStatusChangeDate()));
        }
      }
    }

    return taskStatusDate;
  }

  private Map<TaigaSprintDaysId, TaskStatsDto> updateSprintTasks(Map<Long, List<TaskStatusDateDto>> sprintTasksMap) {
    Map<TaigaSprintDaysId, TaskStatsDto> taskStatsMap = new HashMap<>();
    for(Entry<Long, List<TaskStatusDateDto>> entry : sprintTasksMap.entrySet()) {
      TaigaSprint sprint = taigaSprintRepository.findOne(entry.getKey());
      List<TaskStatusDateDto> taskStatusDates = entry.getValue();

      LocalDate currentDate = sprint.getEstimatedStart();
      while(DateUtil.isDateLesserThanEqual(currentDate, sprint.getEstimatedFinish())) {
        for(TaskStatusDateDto taskDate : taskStatusDates) {
          TaigaSprintDaysId taigaSprintDaysId = new TaigaSprintDaysId(taskDate.getSprintId(), currentDate);
          taskStatsMap.putIfAbsent(taigaSprintDaysId, new TaskStatsDto());
          TaskStatsDto taskStats = taskStatsMap.get(taigaSprintDaysId);

          if(taskDate.getCreatedDate().isAfter(currentDate)) {
            continue;
          }

          if(taskDate.getReadyToTestDate() != null) {
            taskDate.setInProgressDate(taskDate.getReadyToTestDate());
          }

          if(taskDate.getInProgressDate() == null && taskDate.getClosedDate() == null) {
            if (DateUtil.isDateGreaterThanEqual(currentDate, taskDate.getCreatedDate())) {
              taskStats.setNewTasks(taskStats.getNewTasks() + 1);
            }
          } else if (taskDate.getInProgressDate() != null && taskDate.getClosedDate() == null) {
            if(DateUtil.isDateBetween(currentDate, taskDate.getCreatedDate(), taskDate.getInProgressDate())) {
              taskStats.setNewTasks(taskStats.getNewTasks() + 1);
            } else if (DateUtil.isDateGreaterThanEqual(currentDate, taskDate.getInProgressDate())) {
              taskStats.setInProgressTasks(taskStats.getInProgressTasks() + 1);
            }
          } else if (taskDate.getInProgressDate() == null && taskDate.getClosedDate() != null) {
            if(DateUtil.isDateBetween(currentDate, taskDate.getCreatedDate(), taskDate.getClosedDate())) {
              taskStats.setNewTasks(taskStats.getNewTasks() + 1);
            } else if (DateUtil.isDateGreaterThanEqual(currentDate, taskDate.getClosedDate())) {
              taskStats.setClosedTasks(taskStats.getClosedTasks() + 1);
            }
          } else {
            if(DateUtil.isDateBetween(currentDate, taskDate.getCreatedDate(), taskDate.getInProgressDate())) {
              taskStats.setNewTasks(taskStats.getNewTasks() + 1);
            } else if (DateUtil.isDateBetween(currentDate, taskDate.getInProgressDate(), taskDate.getClosedDate())) {
              taskStats.setInProgressTasks(taskStats.getInProgressTasks() + 1);
            } else if (DateUtil.isDateGreaterThanEqual(currentDate, taskDate.getClosedDate())) {
              taskStats.setClosedTasks(taskStats.getClosedTasks() + 1);
            }
          }
        }

        currentDate = currentDate.plusDays(1);
      }
    }

    return taskStatsMap;
  }
}
