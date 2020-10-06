package com.it4innov.blog.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.it4innov.blog.domain.Article} entity.
 */
public class ArticleDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String libele;

    private String description;

    @Lob
    private byte[] couverture;

    private String couvertureContentType;
    private Instant dateCreation;

    @NotNull
    private Boolean publier;

    private String docMDPath;


    private Long categorieId;

    private Long auteurId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getCouverture() {
        return couverture;
    }

    public void setCouverture(byte[] couverture) {
        this.couverture = couverture;
    }

    public String getCouvertureContentType() {
        return couvertureContentType;
    }

    public void setCouvertureContentType(String couvertureContentType) {
        this.couvertureContentType = couvertureContentType;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean isPublier() {
        return publier;
    }

    public void setPublier(Boolean publier) {
        this.publier = publier;
    }

    public String getDocMDPath() {
        return docMDPath;
    }

    public void setDocMDPath(String docMDPath) {
        this.docMDPath = docMDPath;
    }

    public Long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
    }

    public Long getAuteurId() {
        return auteurId;
    }

    public void setAuteurId(Long auteurId) {
        this.auteurId = auteurId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleDTO)) {
            return false;
        }

        return id != null && id.equals(((ArticleDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticleDTO{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", description='" + getDescription() + "'" +
            ", couverture='" + getCouverture() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", publier='" + isPublier() + "'" +
            ", docMDPath='" + getDocMDPath() + "'" +
            ", categorieId=" + getCategorieId() +
            ", auteurId=" + getAuteurId() +
            "}";
    }
}
