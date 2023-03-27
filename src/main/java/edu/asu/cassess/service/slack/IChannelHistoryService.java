package edu.asu.cassess.service.slack;

import edu.asu.cassess.model.slack.MessageList;

public interface IChannelHistoryService {
    MessageList getSlackMessages(String channel, String token, long unixOldest, long unixCurrent);

    void getMessageTotals(String channelID, String course, String team);

    void updateMessageTotals(String course);
}
