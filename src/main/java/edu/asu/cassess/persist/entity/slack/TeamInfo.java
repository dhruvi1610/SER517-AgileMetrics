package edu.asu.cassess.persist.entity.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamInfo {

    private boolean ok;
    private TeamObject team;
    private String error;
    private String warning;

    public TeamInfo() {

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
     * @return the team
     */
    public TeamObject getTeam() {
        return team;
    }

    /**
     * @param team the team to set
     */
    public void setTeam(TeamObject team) {
        this.team = team;
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
}
