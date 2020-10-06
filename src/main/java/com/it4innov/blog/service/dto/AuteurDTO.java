package com.it4innov.blog.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.it4innov.blog.domain.Auteur} entity.
 */
public class AuteurDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String login;

    private String firstname;

    @NotNull
    private String password;

    @Lob
    private byte[] profile;

    private String profileContentType;
    private String tweeter;

    private String linkedin;

    private String facebook;

    private String description;

    private String slogan;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getProfile() {
        return profile;
    }

    public void setProfile(byte[] profile) {
        this.profile = profile;
    }

    public String getProfileContentType() {
        return profileContentType;
    }

    public void setProfileContentType(String profileContentType) {
        this.profileContentType = profileContentType;
    }

    public String getTweeter() {
        return tweeter;
    }

    public void setTweeter(String tweeter) {
        this.tweeter = tweeter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuteurDTO)) {
            return false;
        }

        return id != null && id.equals(((AuteurDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuteurDTO{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", password='" + getPassword() + "'" +
            ", profile='" + getProfile() + "'" +
            ", tweeter='" + getTweeter() + "'" +
            ", linkedin='" + getLinkedin() + "'" +
            ", facebook='" + getFacebook() + "'" +
            ", description='" + getDescription() + "'" +
            ", slogan='" + getSlogan() + "'" +
            "}";
    }
}
