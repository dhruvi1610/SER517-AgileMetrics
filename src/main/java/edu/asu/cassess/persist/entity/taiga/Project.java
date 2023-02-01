package edu.asu.cassess.persist.entity.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "project")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {

    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Id
    @Column(name = "slug")
    private String slug;

    public Project() {
    }

    public Project(Long id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

}