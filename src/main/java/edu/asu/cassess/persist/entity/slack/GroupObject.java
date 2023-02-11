package edu.asu.cassess.persist.entity.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "slack_group")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupObject {

    @Id
    private String id;
    private String name;
    private String is_group;
    private long created;
    private String creator;
    private boolean is_archived;
    private boolean is_mpim;
    @OrderColumn(name = "slack_group_members_sequence")
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
    private String last_read;
    private long unread_count;
    private long unread_count_display;

    public GroupObject() {

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
     * @return the is_group
     */
    public String getIs_group() {
        return is_group;
    }

    /**
     * @param is_group the is_group to set
     */
    public void setIs_group(String is_group) {
        this.is_group = is_group;
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
     * @return the is_mpim
     */
    public boolean isIs_mpim() {
        return is_mpim;
    }

    /**
     * @param is_mpim the is_mpim to set
     */
    public void setIs_mpim(boolean is_mpim) {
        this.is_mpim = is_mpim;
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
    public long getUnread_count() {
        return unread_count;
    }

    /**
     * @param unread_count the unread_count to set
     */
    public void setUnread_count(long unread_count) {
        this.unread_count = unread_count;
    }

    /**
     * @return the unread_count_display
     */
    public long getUnread_count_display() {
        return unread_count_display;
    }

    /**
     * @param unread_count_display the unread_count_display to set
     */
    public void setUnread_count_display(long unread_count_display) {
        this.unread_count_display = unread_count_display;
    }

}
