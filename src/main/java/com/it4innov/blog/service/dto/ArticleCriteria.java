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
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.it4innov.blog.domain.Article} entity. This class is used
 * in {@link com.it4innov.blog.web.rest.ArticleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /articles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ArticleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libele;

    private StringFilter description;

    private InstantFilter dateCreation;

    private BooleanFilter publier;

    private StringFilter docMDPath;

    private LongFilter categorieId;

    private LongFilter auteurId;

    private LongFilter tagId;

    public ArticleCriteria() {
    }

    public ArticleCriteria(ArticleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libele = other.libele == null ? null : other.libele.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.dateCreation = other.dateCreation == null ? null : other.dateCreation.copy();
        this.publier = other.publier == null ? null : other.publier.copy();
        this.docMDPath = other.docMDPath == null ? null : other.docMDPath.copy();
        this.categorieId = other.categorieId == null ? null : other.categorieId.copy();
        this.auteurId = other.auteurId == null ? null : other.auteurId.copy();
        this.tagId = other.tagId == null ? null : other.tagId.copy();
    }

    @Override
    public ArticleCriteria copy() {
        return new ArticleCriteria(this);
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

    public InstantFilter getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(InstantFilter dateCreation) {
        this.dateCreation = dateCreation;
    }

    public BooleanFilter getPublier() {
        return publier;
    }

    public void setPublier(BooleanFilter publier) {
        this.publier = publier;
    }

    public StringFilter getDocMDPath() {
        return docMDPath;
    }

    public void setDocMDPath(StringFilter docMDPath) {
        this.docMDPath = docMDPath;
    }

    public LongFilter getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(LongFilter categorieId) {
        this.categorieId = categorieId;
    }

    public LongFilter getAuteurId() {
        return auteurId;
    }

    public void setAuteurId(LongFilter auteurId) {
        this.auteurId = auteurId;
    }

    public LongFilter getTagId() {
        return tagId;
    }

    public void setTagId(LongFilter tagId) {
        this.tagId = tagId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArticleCriteria that = (ArticleCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(libele, that.libele) &&
            Objects.equals(description, that.description) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(publier, that.publier) &&
            Objects.equals(docMDPath, that.docMDPath) &&
            Objects.equals(categorieId, that.categorieId) &&
            Objects.equals(auteurId, that.auteurId) &&
            Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        libele,
        description,
        dateCreation,
        publier,
        docMDPath,
        categorieId,
        auteurId,
        tagId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (libele != null ? "libele=" + libele + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (dateCreation != null ? "dateCreation=" + dateCreation + ", " : "") +
                (publier != null ? "publier=" + publier + ", " : "") +
                (docMDPath != null ? "docMDPath=" + docMDPath + ", " : "") +
                (categorieId != null ? "categorieId=" + categorieId + ", " : "") +
                (auteurId != null ? "auteurId=" + auteurId + ", " : "") +
                (tagId != null ? "tagId=" + tagId + ", " : "") +
            "}";
    }

}
