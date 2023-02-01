package edu.asu.cassess.persist.entity.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "authority")
public class Authority {

    @Id
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public Authority(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Authority() {

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

}
