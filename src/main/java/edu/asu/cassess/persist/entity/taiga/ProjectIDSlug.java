package edu.asu.cassess.persist.entity.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Subselect("SELECT id, slug FROM cassess.project INNER JOIN cassess.students ON cassess.project.slug=cassess.students.taiga_project_slug AND course=?1")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectIDSlug {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "slug")
    private String slug;

    @Column(name = "team")
    private String team;

    public ProjectIDSlug() {
    }

    public ProjectIDSlug(Long id, String slug, String team) {
        this.id = id;
        this.slug = slug;
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

}
