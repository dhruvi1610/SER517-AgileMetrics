package edu.asu.cassess.persist.entity.slack;

public class UserInfo {
    private boolean ok;
    private UserObject user;

    public UserInfo() {

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
     * @return the user
     */
    public UserObject getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserObject user) {
        this.user = user;
    }

}
