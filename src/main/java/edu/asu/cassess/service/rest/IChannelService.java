package edu.asu.cassess.service.rest;

import edu.asu.cassess.persist.entity.rest.Channel;
import edu.asu.cassess.persist.entity.rest.Team;
import org.json.JSONObject;

import java.util.List;

public interface IChannelService {

    <T> Object create(Channel channel);

    <T> Object update(Channel channel);

    <T> Object read(String id, String team_name, String course);

    <T> Object delete(Channel channel);

    <T> List<Channel> listRead();

    <T> List<Channel> listReadByTeam(String team_name, String course);

    JSONObject listCreate(List<Channel> channels);

    JSONObject listUpdate(List<Channel> channels);

    <T> Object deleteByTeam(Team team);
}
