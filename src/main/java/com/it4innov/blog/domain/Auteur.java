package com.it4innov.blog.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Auteur.
 */
@Entity
@Table(name = "auteur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Auteur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "firstname")
    private String firstname;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Lob
    @Column(name = "profile")
    private byte[] profile;

    @Column(name = "profile_content_type")
    private String profileContentType;

    @Column(name = "tweeter")
    private String tweeter;

    @Column(name = "linkedin")
    private String linkedin;

    @Column(name = "facebook")
    private String facebook;

    @Column(name = "description")
    private String description;

    @Column(name = "slogan")
    private String slogan;

    @OneToMany(mappedBy = "auteur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Article> articles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public Auteur login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstname() {
        return firstname;
    }

    public Auteur firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPassword() {
        return password;
    }

    public Auteur password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getProfile() {
        return profile;
    }

    public Auteur profile(byte[] profile) {
        this.profile = profile;
        return this;
    }

    public void setProfile(byte[] profile) {
        this.profile = profile;
    }

    public String getProfileContentType() {
        return profileContentType;
    }

    public Auteur profileContentType(String profileContentType) {
        this.profileContentType = profileContentType;
        return this;
    }

    public void setProfileContentType(String profileContentType) {
        this.profileContentType = profileContentType;
    }

    public String getTweeter() {
        return tweeter;
    }

    public Auteur tweeter(String tweeter) {
        this.tweeter = tweeter;
        return this;
    }

    public void setTweeter(String tweeter) {
        this.tweeter = tweeter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public Auteur linkedin(String linkedin) {
        this.linkedin = linkedin;
        return this;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getFacebook() {
        return facebook;
    }

    public Auteur facebook(String facebook) {
        this.facebook = facebook;
        return this;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getDescription() {
        return description;
    }

    public Auteur description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlogan() {
        return slogan;
    }

    public Auteur slogan(String slogan) {
        this.slogan = slogan;
        return this;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public Auteur articles(Set<Article> articles) {
        this.articles = articles;
        return this;
    }

    public Auteur addArticle(Article article) {
        this.articles.add(article);
        article.setAuteur(this);
        return this;
    }

    public Auteur removeArticle(Article article) {
        this.articles.remove(article);
        article.setAuteur(null);
        return this;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Auteur)) {
            return false;
        }
        return id != null && id.equals(((Auteur) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Auteur{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", password='" + getPassword() + "'" +
            ", profile='" + getProfile() + "'" +
            ", profileContentType='" + getProfileContentType() + "'" +
            ", tweeter='" + getTweeter() + "'" +
            ", linkedin='" + getLinkedin() + "'" +
            ", facebook='" + getFacebook() + "'" +
            ", description='" + getDescription() + "'" +
            ", slogan='" + getSlogan() + "'" +
            "}";
    }
}
