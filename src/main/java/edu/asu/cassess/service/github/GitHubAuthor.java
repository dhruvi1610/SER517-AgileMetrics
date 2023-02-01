package edu.asu.cassess.service.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubAuthor {

    @Column(name = "login")
    private String login;

    public GitHubAuthor() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "GitHubAuthor{" +
                "login='" + login + '\'' +
                '}';
    }
}
