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
 * Criteria class for the {@link com.it4innov.blog.domain.Tag} entity. This class is used
 * in {@link com.it4innov.blog.web.rest.TagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TagCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libele;

    private StringFilter description;

    private LongFilter articleId;

    public TagCriteria() {
    }

    public TagCriteria(TagCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libele = other.libele == null ? null : other.libele.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.articleId = other.articleId == null ? null : other.articleId.copy();
    }

    @Override
    public TagCriteria copy() {
        return new TagCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLibele() {
        return libele;
    }

    public void setLibele(StringFilter libele) {
        this.libele = libele;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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
        final TagCriteria that = (TagCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(libele, that.libele) &&
            Objects.equals(description, that.description) &&
            Objects.equals(articleId, that.articleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        libele,
        description,
        articleId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (libele != null ? "libele=" + libele + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (articleId != null ? "articleId=" + articleId + ", " : "") +
            "}";
    }

}
