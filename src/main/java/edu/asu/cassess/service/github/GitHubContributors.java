package edu.asu.cassess.service.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name = "contributors")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubContributors {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    public GitHubAuthor author;

    public Weeks[] weeks;

    public GitHubContributors() {
    }

    public Weeks[] getWeeks() {
        return weeks;
    }

    public void setWeeks(Weeks[] weeks) {
        this.weeks = weeks;
    }

    public GitHubAuthor getAuthor() {return author;}

    public void setAuthor(GitHubAuthor author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "GitHubContributors{" +
                "weeks=" + weeks +
                ", author=" + author +
                '}';
    }

}
