package edu.asu.cassess.model.Taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Subselect("SELECT taiga_project_slug AS 'slugs' FROM cassess.students WHERE course = ?1")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Slugs {

    @Id
    @Column(name = "taiga_project_slug")
    public String taiga_project_slug;

    public Slugs() {
    }

    public Slugs(String taiga_project_slug) {
        this.taiga_project_slug = taiga_project_slug;
    }

    public String getSlug() {
        return taiga_project_slug;
    }

    public void setSlug(String taiga_project_slug) {
        this.taiga_project_slug = taiga_project_slug;
    }

}
