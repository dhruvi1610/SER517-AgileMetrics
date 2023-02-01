package edu.asu.cassess.persist.entity.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users_authority")
public class UsersAuthority {

    @Id
    @Column(name = "id_user", nullable = false)
    private Long id_user;

    @Column(name = "id_authority", nullable = false)
    private Long id_authority;

    public UsersAuthority() {
    }

    public UsersAuthority(Long id_user, Long id_authority) {

        this.id_user = id_user;
        this.id_authority = id_authority;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public Long getId_authority() {
        return id_authority;
    }

    public void setId_authority(Long id_authority) {
        this.id_authority = id_authority;
    }
}
