package edu.asu.cassess.service.rest;


import edu.asu.cassess.dao.rest.ChannelsServiceDao;
import edu.asu.cassess.persist.entity.rest.Channel;
import edu.asu.cassess.persist.entity.rest.Team;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.ejb.EJB;
import java.util.List;

@Service
public class ChannelService implements IChannelService {

    @EJB
    private ChannelsServiceDao channelServiceDao;

    @Override
    public <T> Object create(Channel channel) {

        return channelServiceDao.create(channel);
    }

    @Override
    public <T> Object update(Channel channel) {

        return channelServiceDao.update(channel);
    }

    @Override
    public <T> Object read(String id, String team, String course) {

        return channelServiceDao.find(id, team, course);
    }

    @Override
    public <T> Object delete(Channel channel) {

        return channelServiceDao.delete(channel);
    }

    @Override
    public <T> List<Channel> listRead() {

        return channelServiceDao.listRead();
    }

    @Override
    public <T> List<Channel> listReadByTeam(String team_name, String course) {

        return channelServiceDao.listReadByTeam(team_name, course);
    }

    @Override
    public JSONObject listCreate(List<Channel> channels) {

        return channelServiceDao.listCreate(channels);
    }

    @Override
    public JSONObject listUpdate(List<Channel> channels) {

        return channelServiceDao.listUpdate(channels);
    }

    @Override
    public <T> Object deleteByTeam(Team team) {

        return channelServiceDao.deleteByTeam(team);
    }
}

