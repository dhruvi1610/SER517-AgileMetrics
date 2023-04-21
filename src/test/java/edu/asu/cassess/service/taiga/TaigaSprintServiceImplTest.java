package edu.asu.cassess.service.taiga;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.asu.cassess.persist.entity.taiga.TaigaSprint;
import edu.asu.cassess.persist.entity.taiga.TaigaSprintDays;
import edu.asu.cassess.persist.entity.taiga.TaigaSprintDaysId;
import edu.asu.cassess.persist.repo.taiga.ITaigaSprintDaysRepository;
import edu.asu.cassess.persist.repo.taiga.ITaigaSprintRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaigaSprintServiceImplTest {

  @Mock
  ITaigaSprintRepository taigaSprintRepository;

  @Mock
  ITaigaSprintDaysRepository taigaSprintDaysRepository;

  @InjectMocks
  TaigaSprintServiceImpl taigaSprintService;

  List<TaigaSprint> sprints = new ArrayList<>();
  List<TaigaSprintDays> sprintDays = new ArrayList<>();

  @BeforeEach
  void setUp() {
    sprints.add(new TaigaSprint(3647L, 6017L, "SER 517", "Test Team", "Sprint 2",
        false, LocalDate.now(), LocalDate.now().plusYears(2), false));
    sprints.add(new TaigaSprint(3646L, 6017L, "SER 517", "Test Team", "Sprint 1",
        true, LocalDate.now().minusYears(1), LocalDate.now(), false));

    sprintDays.add(new TaigaSprintDays(new TaigaSprintDaysId(3646L, LocalDate.now().minusDays(1)),
        80.0, 75.0, 45.0, 20.0, 5, 3, 2));
    sprintDays.add(new TaigaSprintDays(new TaigaSprintDaysId(3646L, LocalDate.now()),
        70.0, 65.0, 25.0, 10.0, 5, 1, 4));
  }

  @Test
  @DisplayName("JUnit test for fetch file taiga sprints")
  void getSprints() {
    String course = "SER 517";
    String team = "Test Team";
    when(taigaSprintRepository.findByCourseNameAndTeamNameOrderBySprintNameDesc(course, team)).thenReturn(sprints);

    assertEquals(2, taigaSprintService.getSprints(course, team).size());
    assertSame(sprints.get(0), taigaSprintService.getSprints(course, team).get(0));
    assertEquals(3646L, taigaSprintService.getSprints(course, team).get(1).getSprintId());
  }

  @Test
  @DisplayName("JUnit test for fetch file taiga sprint's stats by sprintId")
  void getSprintDaysBySprintId() {
    Long sprintId = 3646L;
    when(taigaSprintDaysRepository.findByTaigaSprintDaysIdSprintIdOrderByTaigaSprintDaysIdDateAsc(sprintId)).thenReturn(sprintDays);

    assertEquals(2, taigaSprintService.getSprintDaysBySprintId(sprintId).size());
    assertSame(sprintDays.get(1), taigaSprintService.getSprintDaysBySprintId(sprintId).get(1));
  }
}