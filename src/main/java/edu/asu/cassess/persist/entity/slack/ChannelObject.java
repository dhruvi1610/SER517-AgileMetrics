package edu.asu.cassess.persist.entity.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "slack_channel")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelObject {

    @Id
    private String id;
    private String name;
    private boolean is_channel;
    private long created;
    private String creator;
    private boolean is_archived;
    private boolean is_general;
    private boolean is_member;
    private String last_read;
    private int unread_count;
    private int unread_count_display;
    @OrderColumn(name = "slack_channel_member_sequence")
    @ElementCollection
    private String[] members;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "topic_value")),
            @AttributeOverride(name = "creator", column = @Column(name = "topic_creator")),
            @AttributeOverride(name = "last_set", column = @Column(name = "topic_last_set"))
    })
    private SlackGroupObject topic;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "purpose_value")),
            @AttributeOverride(name = "creator", column = @Column(name = "purpose_creator")),
            @AttributeOverride(name = "last_set", column = @Column(name = "purpose_last_set"))
    })
    private SlackGroupObject purpose;
    @OrderColumn(name = "slack_channel_pn_sequence")
    @ElementCollection
    private String[] previous_names;
    private long num_members;

    public ChannelObject() {

    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the is_channel
     */
    public boolean isIs_channel() {
        return is_channel;
    }

    /**
     * @param is_channel the is_channel to set
     */
    public void setIs_channel(boolean is_channel) {
        this.is_channel = is_channel;
    }

    /**
     * @return the created
     */
    public long getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(long created) {
        this.created = created;
    }

    /**
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @return the is_archived
     */
    public boolean isIs_archived() {
        return is_archived;
    }

    /**
     * @param is_archived the is_archived to set
     */
    public void setIs_archived(boolean is_archived) {
        this.is_archived = is_archived;
    }

    /**
     * @return the is_general
     */
    public boolean isIs_general() {
        return is_general;
    }

    /**
     * @param is_general the is_general to set
     */
    public void setIs_general(boolean is_general) {
        this.is_general = is_general;
    }

    /**
     * @return the is_member
     */
    public boolean isIs_member() {
        return is_member;
    }

    /**
     * @param is_member the is_member to set
     */
    public void setIs_member(boolean is_member) {
        this.is_member = is_member;
    }

    /**
     * @return the last_read
     */
    public String getLast_read() {
        return last_read;
    }

    /**
     * @param last_read the last_read to set
     */
    public void setLast_read(String last_read) {
        this.last_read = last_read;
    }


    /**
     * @return the unread_count
     */
    public int getUnread_count() {
        return unread_count;
    }

    /**
     * @param unread_count the unread_count to set
     */
    public void setUnread_count(int unread_count) {
        this.unread_count = unread_count;
    }

    /**
     * @return the unread_count_display
     */
    public int getUnread_count_display() {
        return unread_count_display;
    }

    /**
     * @param unread_count_display the unread_count_display to set
     */
    public void setUnread_count_display(int unread_count_display) {
        this.unread_count_display = unread_count_display;
    }

    /**
     * @return the members
     */
    public String[] getMembers() {
        return members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(String[] members) {
        this.members = members;
    }

    /**
     * @return the topic
     */
    public SlackGroupObject getTopic() {
        return topic;
    }

    /**
     * @param topic the topic to set
     */
    public void setTopic(SlackGroupObject topic) {
        this.topic = topic;
    }

    /**
     * @return the purpose
     */
    public SlackGroupObject getPurpose() {
        return purpose;
    }

    /**
     * @param purpose the purpose to set
     */
    public void setPurpose(SlackGroupObject purpose) {
        this.purpose = purpose;
    }

    /**
     * @return the previous_names
     */
    public String[] getPrevious_names() {
        return previous_names;
    }

    /**
     * @param previous_names the previous_names to set
     */
    public void setPrevious_names(String[] previous_names) {
        this.previous_names = previous_names;
    }

    /**
     * @return the num_members
     */
    public long getNum_members() {
        return num_members;
    }

    /**
     * @param num_members the num_members to set
     */
    public void setNum_members(long num_members) {
        this.num_members = num_members;
    }
}
