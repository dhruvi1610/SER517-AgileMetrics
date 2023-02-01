package edu.asu.cassess.service.taiga;

import edu.asu.cassess.dao.taiga.IProjectQueryDao;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.taiga.*;
import edu.asu.cassess.persist.repo.taiga.MemberRepo;
import edu.asu.cassess.service.rest.ICourseService;
import edu.asu.cassess.service.rest.ITeamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional
public class MembersService implements IMembersService {

    private RestTemplate restTemplate;

    private String membershipListURL;

    @Autowired
    private MemberRepo MemberDao;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ITeamsService teamsService;

    @Autowired
    private IProjectQueryDao projectService;

    public MembersService() {
        restTemplate = new RestTemplate();
        membershipListURL = "https://api.taiga.io/api/v1/memberships?project=";
    }

    @Override
    public List<TaigaMember> getMembers(Long projectId, String token, int page, String team, String course) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set("Authorization", "Bearer " + token);

        //System.out.println("Headers: " + headers);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<List<TaigaMember>> memberList = restTemplate.exchange(membershipListURL + projectId + "&page=" + page,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<TaigaMember>>() {
                });

        //ResponseEntity<List<MemberData>> memberList = restTemplate.getForEntity(membershipListURL, List<>.class, request);

        List<TaigaMember> members = memberList.getBody();

        for (int i = 0; i < members.size(); i++) {
            if(members.get(i).getUser_email() != null) {
                members.get(i).setTeam(team);
                members.get(i).setCourse(course);
                System.out.println(members.get(i).getId());
                MemberDao.save(new MemberData(new MemberDataID(members.get(i).getId(), members.get(i).getUser_email(), members.get(i).getTeam(), course), members.get(i).getFull_name(),
                        members.get(i).getProject_name(), members.get(i).getProject_slug(), members.get(i).getRole_name()));
            }
        }

        if (memberList.getHeaders().containsKey("x-pagination-next")) {
            page++;
            return getMembers(projectId, token, page, team, course);
        } else {

            return members;
        }
    }

    /* Method to provide single operation on
    updating the member_data table based on the course, student and project tables
     */
    @Override
    public void updateMembership(String course) {
        System.out.println("Updating Members");
        Course tempCourse = (Course) courseService.read(course);
        String token = tempCourse.getTaiga_token();
        List<Team> teams = teamsService.listReadByCourse(course);
        for(Team team : teams) {
            String slug = team.getTaiga_project_slug();
            Object object = projectService.getTaigaProject(slug);
            if (object.getClass() == Project.class) {
                Project project = (Project) object;
                Long slugId = project.getId();
                if (token != null && slugId != null) {
                    //System.out.println("SlugId: " + slugId);
                    getMembers(slugId, token, 1, team.getTeam_name(), course);
                }
            }
        }

    }
}
