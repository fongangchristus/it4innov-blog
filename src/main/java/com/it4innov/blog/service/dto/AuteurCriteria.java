package com.it4innov.blog.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.it4innov.blog.domain.Auteur} entity. This class is used
 * in {@link com.it4innov.blog.web.rest.AuteurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /auteurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AuteurCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter login;

    private StringFilter firstname;

    private StringFilter password;

    private StringFilter tweeter;

    private StringFilter linkedin;

    private StringFilter facebook;

    private StringFilter description;

    private StringFilter slogan;

    private LongFilter articleId;

    public AuteurCriteria() {
    }

    public AuteurCriteria(AuteurCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.login = other.login == null ? null : other.login.copy();
        this.firstname = other.firstname == null ? null : other.firstname.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.tweeter = other.tweeter == null ? null : other.tweeter.copy();
        this.linkedin = other.linkedin == null ? null : other.linkedin.copy();
        this.facebook = other.facebook == null ? null : other.facebook.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.slogan = other.slogan == null ? null : other.slogan.copy();
        this.articleId = other.articleId == null ? null : other.articleId.copy();
    }

    @Override
    public AuteurCriteria copy() {
        return new AuteurCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLogin() {
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    public StringFilter getFirstname() {
        return firstname;
    }

    public void setFirstname(StringFilter firstname) {
        this.firstname = firstname;
    }

    public StringFilter getPassword() {
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public StringFilter getTweeter() {
        return tweeter;
    }

    public void setTweeter(StringFilter tweeter) {
        this.tweeter = tweeter;
    }

    public StringFilter getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(StringFilter linkedin) {
        this.linkedin = linkedin;
    }

    public StringFilter getFacebook() {
        return facebook;
    }

    public void setFacebook(StringFilter facebook) {
        this.facebook = facebook;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getSlogan() {
        return slogan;
    }

    public void setSlogan(StringFilter slogan) {
        this.slogan = slogan;
    }

    public LongFilter getArticleId() {
        return articleId;
    }

    public void setArticleId(LongFilter articleId) {
        this.articleId = articleId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AuteurCriteria that = (AuteurCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(login, that.login) &&
            Objects.equals(firstname, that.firstname) &&
            Objects.equals(password, that.password) &&
            Objects.equals(tweeter, that.tweeter) &&
            Objects.equals(linkedin, that.linkedin) &&
            Objects.equals(facebook, that.facebook) &&
            Objects.equals(description, that.description) &&
            Objects.equals(slogan, that.slogan) &&
            Objects.equals(articleId, that.articleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        login,
        firstname,
        password,
        tweeter,
        linkedin,
        facebook,
        description,
        slogan,
        articleId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuteurCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (login != null ? "login=" + login + ", " : "") +
                (firstname != null ? "firstname=" + firstname + ", " : "") +
                (password != null ? "password=" + password + ", " : "") +
                (tweeter != null ? "tweeter=" + tweeter + ", " : "") +
                (linkedin != null ? "linkedin=" + linkedin + ", " : "") +
                (facebook != null ? "facebook=" + facebook + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (slogan != null ? "slogan=" + slogan + ", " : "") +
                (articleId != null ? "articleId=" + articleId + ", " : "") +
            "}";
    }

}
