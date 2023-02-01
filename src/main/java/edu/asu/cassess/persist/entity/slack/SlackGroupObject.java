package edu.asu.cassess.persist.entity.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackGroupObject {

    @Column(name = "sub_value")
    private String value;
    @Column(name = "sub_creator")
    private String creator;
    @Column(name = "sub_last_set")
    private long last_set;

    public SlackGroupObject() {

    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
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
     * @return the last_set
     */
    public long getLast_set() {
        return last_set;
    }

    /**
     * @param last_set the last_set to set
     */
    public void setLast_set(long last_set) {
        this.last_set = last_set;
    }

}
