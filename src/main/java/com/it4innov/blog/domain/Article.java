package com.it4innov.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Article.
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "libele", nullable = false)
    private String libele;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "couverture")
    private byte[] couverture;

    @Column(name = "couverture_content_type")
    private String couvertureContentType;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @NotNull
    @Column(name = "publier", nullable = false)
    private Boolean publier;

    @Column(name = "doc_md_path")
    private String docMDPath;

    @ManyToOne
    @JsonIgnoreProperties(value = "articles", allowSetters = true)
    private Categorie categorie;

    @ManyToOne
    @JsonIgnoreProperties(value = "articles", allowSetters = true)
    private Auteur auteur;

    @ManyToMany(mappedBy = "articles")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Tag> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibele() {
        return libele;
    }

    public Article libele(String libele) {
        this.libele = libele;
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getDescription() {
        return description;
    }

    public Article description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getCouverture() {
        return couverture;
    }

    public Article couverture(byte[] couverture) {
        this.couverture = couverture;
        return this;
    }

    public void setCouverture(byte[] couverture) {
        this.couverture = couverture;
    }

    public String getCouvertureContentType() {
        return couvertureContentType;
    }

    public Article couvertureContentType(String couvertureContentType) {
        this.couvertureContentType = couvertureContentType;
        return this;
    }

    public void setCouvertureContentType(String couvertureContentType) {
        this.couvertureContentType = couvertureContentType;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public Article dateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean isPublier() {
        return publier;
    }

    public Article publier(Boolean publier) {
        this.publier = publier;
        return this;
    }

    public void setPublier(Boolean publier) {
        this.publier = publier;
    }

    public String getDocMDPath() {
        return docMDPath;
    }

    public Article docMDPath(String docMDPath) {
        this.docMDPath = docMDPath;
        return this;
    }

    public void setDocMDPath(String docMDPath) {
        this.docMDPath = docMDPath;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public Article categorie(Categorie categorie) {
        this.categorie = categorie;
        return this;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Auteur getAuteur() {
        return auteur;
    }

    public Article auteur(Auteur auteur) {
        this.auteur = auteur;
        return this;
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Article tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Article addTag(Tag tag) {
        this.tags.add(tag);
        tag.getArticles().add(this);
        return this;
    }

    public Article removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getArticles().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return id != null && id.equals(((Article) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", description='" + getDescription() + "'" +
            ", couverture='" + getCouverture() + "'" +
            ", couvertureContentType='" + getCouvertureContentType() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", publier='" + isPublier() + "'" +
            ", docMDPath='" + getDocMDPath() + "'" +
            "}";
    }
}
