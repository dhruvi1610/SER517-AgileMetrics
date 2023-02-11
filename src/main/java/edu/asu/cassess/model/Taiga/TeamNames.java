package edu.asu.cassess.model.Taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Subselect("SELECT project_name AS 'team' FROM cassess.students WHERE course = ?1")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamNames {

    @Id
    @Column(name = "team")
    public String team;

    public TeamNames() {
    }

    public TeamNames(String team) {
        this.team = team;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String project_name) {
        this.team = team;
    }

}
