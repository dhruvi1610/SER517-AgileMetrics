package edu.asu.cassess.dao.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.asu.cassess.persist.entity.rest.Channel;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.repo.rest.ChannelRepo;
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
@Transactional
public class ChannelsServiceDao {

    protected EntityManager entityManager;
    @Autowired
    private ChannelRepo channelRepo;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T> Object create(Channel channelInput) {
        Query preQuery = getEntityManager().createNativeQuery("SELECT * FROM cassess.channels WHERE course = ?1 AND team_name = ?2 AND id = ?3", Channel.class);
        preQuery.setParameter(1, channelInput.getCourse());
        preQuery.setParameter(2, channelInput.getTeam_name());
        preQuery.setParameter(3, channelInput.getId());
        List results = preQuery.getResultList();
        if (!results.isEmpty()) {
            return new RestResponse(channelInput.getId() + " already exists in database");
        } else {
            channelRepo.save(channelInput);
            return channelInput;
        }
    }

    public <T> Object update(Channel channelInput) {
        Query preQuery = getEntityManager().createNativeQuery("SELECT * FROM cassess.channels WHERE course = ?1 AND team_name = ?2 AND id = ?3", Channel.class);
        preQuery.setParameter(1, channelInput.getCourse());
        preQuery.setParameter(2, channelInput.getTeam_name());
        preQuery.setParameter(3, channelInput.getId());
        List results = preQuery.getResultList();
        if (!results.isEmpty()) {
            channelRepo.save(channelInput);
            return channelInput;
        } else {
            return new RestResponse(channelInput + " does not exist in database");
        }
    }

    public <T> Object find(String id, String team, String course) {
        Query preQuery = getEntityManager().createNativeQuery("SELECT * FROM cassess.channels WHERE course = ?1 AND team_name = ?2 AND id = ?3", Channel.class);
        preQuery.setParameter(1, course);
        preQuery.setParameter(2, team);
        preQuery.setParameter(3, id);
        List<Channel> results = preQuery.getResultList();
        if (!results.isEmpty()) {
            return (Channel) results.get(0);
        } else {
            return new RestResponse("Channel " + id + " does not exist in database");
        }
    }

    public <T> Object delete(Channel channel) {
        Query preQuery = getEntityManager().createNativeQuery("DELETE FROM cassess.channels WHERE course = ?1 AND team_name = ?2 AND id = ?3", Channel.class);
        preQuery.setParameter(1, channel.getCourse());
        preQuery.setParameter(2, channel.getTeam_name());
        preQuery.setParameter(3, channel.getId());
        preQuery.executeUpdate();
        return new RestResponse(channel.getId() + " has been removed from the database");
    }

    public List<Channel> listRead() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.channels", Channel.class);
        List<Channel> resultList = query.getResultList();
        return resultList;
    }

    public List<Channel> listReadByTeam(String team_name, String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.channels WHERE course = ?1 AND team_name = ?2", Channel.class);
        query.setParameter(1, course);
        query.setParameter(2, team_name);
        List<Channel> resultList = query.getResultList();
        return resultList;
    }


    public JSONObject listCreate(List<Channel> channels) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Channel channelInput : channels) {
            Query preQuery = getEntityManager().createNativeQuery("SELECT * FROM cassess.channels WHERE course = ?1 AND team_name = ?2 AND id = ?3", Channel.class);
            preQuery.setParameter(1, channelInput.getCourse());
            preQuery.setParameter(2, channelInput.getTeam_name());
            preQuery.setParameter(3, channelInput.getId());
            List results = preQuery.getResultList();
            if (!results.isEmpty()) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(channelInput.getId() + " already exists in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                channelRepo.save(channelInput);
                try {
                    successArray.put(new JSONObject(ow.writeValueAsString(channelInput)));
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

    public JSONObject listUpdate(List<Channel> channels) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Channel channelInput : channels) {
            Query preQuery = getEntityManager().createNativeQuery("SELECT * FROM cassess.channels WHERE course = ?1 AND team_name = ?2 AND id = ?3", Channel.class);
            preQuery.setParameter(1, channelInput.getCourse());
            preQuery.setParameter(2, channelInput.getTeam_name());
            preQuery.setParameter(3, channelInput.getId());
            List results = preQuery.getResultList();
            if (results.isEmpty()) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(channelInput.getId() + " does not exist in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                channelRepo.save(channelInput);
                try {
                    successArray.put(new JSONObject(ow.writeValueAsString(channelInput)));
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

    public <T> Object deleteByTeam(Team team) {
        Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.channels WHERE course = ?1 AND team_name = ?2");
        query.setParameter(1, team.getCourse());
        query.setParameter(2, team.getTeam_name());
        query.executeUpdate();
        return new RestResponse("All channels in team " + team.getTeam_name() + " have been removed from the database");
    }

}
