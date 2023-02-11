package edu.asu.cassess.persist.entity.security;


import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    private Long id;


    @Column(name = "first_name")
    private String firstName;

    @Column(name = "family_name")
    private String familyName;

    @Column(name = "e_mail")
    private String email;

    @Transient
    private String phone;

    @Column(name = "language")
    private String language;

    @Transient
    private String pictureId;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Transient
    private Date birthDate;

    @Column(name = "enabled", columnDefinition = "BIT", length = 1)
    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_authority", joinColumns = {@JoinColumn(name = "id_user", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "id_authority", table = "authority", referencedColumnName = "id")})
    private Set<Authority> authorities = new HashSet<Authority>();

    public User(String firstName, String familyName, String email, String phone, String language, String pictureId, String login, String password, Date birthDate) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.email = email;
        this.phone = phone;
        this.language = language;
        this.pictureId = pictureId;
        this.login = login;
        this.password = password;
        this.birthDate = birthDate;
        this.enabled = true;
    }

    public User(String firstName, String familyName, String email, String phone, String language, String pictureId, String login, String password, Date birthDate, long id) {
        this.id = id;
        this.firstName = firstName;
        this.familyName = familyName;
        this.email = email;
        this.phone = phone;
        this.language = language;
        this.pictureId = pictureId;
        this.login = login;
        this.password = password;
        this.birthDate = birthDate;
        this.enabled = true;
    }

    public User() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled() {
        this.enabled = true;
    }

    public void setDisabled() {
        this.enabled = false;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }


}
