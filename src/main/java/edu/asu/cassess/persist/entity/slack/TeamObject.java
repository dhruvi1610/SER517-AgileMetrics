package edu.asu.cassess.persist.entity.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "slack_team")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamObject {

    @Id
    private String id;
    private String name;
    private String domain;
    private String email_domain;
    @Embedded
    private SlackIcon icon;

    public TeamObject() {

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
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * @return the email_domain
     */
    public String getEmail_domain() {
        return email_domain;
    }

    /**
     * @param email_domain the email_domain to set
     */
    public void setEmail_domain(String email_domain) {
        this.email_domain = email_domain;
    }

    /**
     * @return the icon
     */
    public SlackIcon getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(SlackIcon icon) {
        this.icon = icon;
    }
}