package edu.asu.cassess.persist.entity.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupList {

    private boolean ok;
    private List<GroupObject> groups;
    private String warning;
    private String error;

    public GroupList() {

    }

    /**
     * @return the ok
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * @param ok the ok to set
     */
    public void setOk(boolean ok) {
        this.ok = ok;
    }

    /**
     * @return the groups
     */
    public List<GroupObject> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(List<GroupObject> groups) {
        this.groups = groups;
    }

    /**
     * @return the warning
     */
    public String getWarning() {
        return warning;
    }

    /**
     * @param warning the warning to set
     */
    public void setWarning(String warning) {
        this.warning = warning;
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }
}
