package edu.asu.cassess.persist.entity;


import java.util.Date;

public class registerUser {

    private String firstName;

    private String familyName;

    private String email;

    private String phone;

    private String language;

    private String pictureId;

    private String login;

    private String password;

    private Date birthDate;

    private Boolean enabled;

    public registerUser(String firstName, String familyName, String email, String login, String password) {

        this.firstName = firstName;
        this.familyName = familyName;
        this.email = email;
        this.phone = null;
        this.language = "en";
        this.pictureId = "";
        this.login = login;
        this.password = password;
        this.birthDate = null;
        this.enabled = true;
    }

    public registerUser() {
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

}

