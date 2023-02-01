package edu.asu.cassess.persist.entity.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelsList {

    private boolean ok;
    private List<ChannelObject> channels;
    private String warning;
    private String error;

    public ChannelsList() {

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
     * @return the channels
     */
    public List<ChannelObject> getChannels() {
        return channels;
    }

    /**
     * @param channels the channels to set
     */
    public void setChannels(List<ChannelObject> channels) {
        this.channels = channels;
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
