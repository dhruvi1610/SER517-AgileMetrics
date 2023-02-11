package edu.asu.cassess.persist.entity.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserList {

    private boolean ok;
    private List<UserObject> members;

    public UserList() {

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
     * @return the members
     */
    public List<UserObject> getMembers() {
        return members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(List<UserObject> members) {
        this.members = members;
    }
}
