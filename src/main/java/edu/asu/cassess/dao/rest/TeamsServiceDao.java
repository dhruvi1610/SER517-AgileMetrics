    package edu.asu.cassess.dao.rest;

    import com.fasterxml.jackson.core.JsonProcessingException;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.fasterxml.jackson.databind.ObjectWriter;
    import edu.asu.cassess.model.Taiga.Slugs;
    import edu.asu.cassess.model.Taiga.TeamNames;
    import edu.asu.cassess.persist.entity.rest.*;
    import edu.asu.cassess.persist.repo.rest.TeamRepo;
    import edu.asu.cassess.service.rest.IChannelService;
    import edu.asu.cassess.service.rest.IStudentsService;
    import org.json.JSONArray;
    import org.json.JSONObject;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.dao.DataAccessException;
    import org.springframework.stereotype.Component;
    import org.springframework.transaction.annotation.Transactional;

    import javax.persistence.EntityManager;
    import javax.persistence.PersistenceContext;
    import javax.persistence.Query;
    import java.util.List;

    @Component
    public class TeamsServiceDao {

        protected EntityManager entityManager;
        @Autowired
        private TeamRepo teamRepo;
        @Autowired
        private IStudentsService studentsService;
        @Autowired
        private IChannelService channelsService;

        public EntityManager getEntityManager() {
            return entityManager;
        }

        @PersistenceContext
        public void setEntityManager(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        @Transactional
        public <T> Object create(Team teamInput) {
            Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.teams WHERE course = ?1 AND team_name = ?2", Team.class);
            query.setParameter(1, teamInput.getCourse());
            query.setParameter(2, teamInput.getTeam_name());
            List results = query.getResultList();
            if (!results.isEmpty()) {
                return new RestResponse(teamInput.getTeam_name() + " already exists in database");
            } else {
                if (teamInput.getStudents() != null) {
                    studentsService.listCreate(teamInput.getStudents());
                }
                if (teamInput.getChannels() != null) {
                    channelsService.listCreate(teamInput.getChannels());
                }
                teamRepo.save(teamInput);
                return teamInput;
            }
        }

        @Transactional
        public <T> Object update(Team teamInput) {
            Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.teams WHERE course = ?1 AND team_name = ?2", Team.class);
            query.setParameter(1, teamInput.getCourse());
            query.setParameter(2, teamInput.getTeam_name());
            List results = query.getResultList();
            if (!results.isEmpty()) {
                Team team = (Team) results.get(0);
                if (team.getStudents() != null) {
                    studentsService.listUpdate(team.getStudents());
                }
                if (team.getChannels() != null) {
                    channelsService.listUpdate(team.getChannels());
                }
                teamRepo.save(teamInput);
                return teamInput;
            } else {
                return new RestResponse(teamInput.getTeam_name() + " does not exist in database");
            }
        }

        @Transactional
        public <T> Object find(String team_name, String course) {
            Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.teams WHERE course = ?1 AND team_name = ?2", Team.class);
            query.setParameter(1, course);
            query.setParameter(2, team_name);
            List<Team> resultList = query.getResultList();
            if(!resultList.isEmpty()){
                return (Team) resultList.get(0);
            }else {
                return new RestResponse(team_name + " does not exist in database");
            }
        }

        @Transactional
        public <T> Object delete(Team team) {
            if (team != null) {
                Query query = getEntityManager().createNativeQuery("DELETE FROM teams WHERE course = ?1 AND team_name = ?2", Team.class);
                query.setParameter(1, team.getCourse());
                query.setParameter(2, team.getTeam_name());
                studentsService.deleteByTeam(team);
                channelsService.deleteByTeam(team);
                query.executeUpdate();
                return new RestResponse(team.getTeam_name() + " has been removed from the database");
            } else {
                return new RestResponse(team.getTeam_name() + " does not exist in the database");
            }
        }

        @Transactional
        public List<Team> listReadAll() throws DataAccessException {
            Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.teams", Team.class);
            List<Team> resultList = query.getResultList();
            return resultList;
        }

        @Transactional
        public List<Team> listReadByCourse(String course) throws DataAccessException {
            Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.teams WHERE course = ?1", Team.class);
            query.setParameter(1, course);
            List<Team> resultList = query.getResultList();
            return resultList;
        }

        @Transactional
        public List<Slugs> listGetSlugs(String course) throws DataAccessException {
            Query query = getEntityManager().createNativeQuery("SELECT DISTINCT taiga_project_slug FROM cassess.teams WHERE course = ?1", Slugs.class);
            query.setParameter(1, course);
            List<Slugs> resultList = query.getResultList();
            return resultList;
        }

        @Transactional
        public List<TeamNames> listGetTeamNames(String course) throws DataAccessException {
            Query query = getEntityManager().createNativeQuery("SELECT DISTINCT team_name AS 'team' FROM cassess.teams WHERE course = ?1", TeamNames.class);
            query.setParameter(1, course);
            List<TeamNames> resultList = query.getResultList();
            return resultList;
        }

        @Transactional
        public <T> Object findOne(String team_name, String course) {
            Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.teams WHERE course = ?1 AND team_name = ?2", Team.class);
            query.setParameter(1, course);
            query.setParameter(2, team_name);
            List<Team> results = query.getResultList();
            if (!results.isEmpty()) {
                Team team = (Team) results.get(0);
                return team;
            } else {
                return new RestResponse(team_name + " does not exist for this course");
            }
        }

        @Transactional
        public JSONObject listCreate(List<Team> teams) {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            JSONArray successArray = new JSONArray();
            JSONArray failureArray = new JSONArray();
            for (Team teamInput : teams) {
                Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.teams WHERE course = ?1 AND team_name = ?2", Team.class);
                query.setParameter(1, teamInput.getCourse());
                query.setParameter(2, teamInput.getTeam_name());
                List<Student> students = teamInput.getStudents();
                List<Channel> channels = teamInput.getChannels();
                List results = query.getResultList();
                if (!results.isEmpty()) {
                    try {
                        failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(teamInput.getTeam_name() + " already exists in database"))));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                } else {
                    teamRepo.save(teamInput);
                    studentsService.listCreate(students);
                    channelsService.listCreate(channels);
                    try {
                        successArray.put(new JSONObject(ow.writeValueAsString(teamInput)));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
            JSONObject returnJSON = new JSONObject();
            returnJSON.put("success", successArray);
            returnJSON.put("failure", failureArray);
            return returnJSON;
        }

        @Transactional
        public JSONObject listUpdate(List<Team> teams) {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            JSONArray successArray = new JSONArray();
            JSONArray failureArray = new JSONArray();
            for (Team teamInput : teams) {
                Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.teams WHERE course = ?1 AND team_name = ?2", Team.class);
                query.setParameter(1, teamInput.getCourse());
                query.setParameter(2, teamInput.getTeam_name());
                List results = query.getResultList();
                if (results.isEmpty()) {
                    try {
                        failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(teamInput.getTeam_name() + " does not exist in database"))));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                } else {
                    List<Student> students = teamInput.getStudents();
                    teamRepo.save(teamInput);
                    studentsService.listUpdate(students);
                    try {
                        successArray.put(new JSONObject(ow.writeValueAsString(teamInput)));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
            JSONObject returnJSON = new JSONObject();
            returnJSON.put("success", successArray);
            returnJSON.put("failure", failureArray);
            return returnJSON;
        }

        @Transactional
        public <T> Object deleteByCourse(Course course) {
            Query preQuery = getEntityManager().createNativeQuery("SELECT * FROM cassess.teams WHERE course = ?1 ", Team.class);
            preQuery.setParameter(1, course.getCourse());
            List results = preQuery.getResultList();
            if (!results.isEmpty()) {
                List<Team> teams = results;
                for (Team team : teams) {
                    studentsService.deleteByTeam(team);
                    channelsService.deleteByTeam(team);
                }
                Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.teams WHERE course = ?1");
                query.setParameter(1, course.getCourse());
                query.executeUpdate();
                return new RestResponse("All teams in course " + course.getCourse() + " have been removed from the database");
            } else {
                return new RestResponse("No teams in course " + course.getCourse() + " exist in the database");
            }
        }

    }