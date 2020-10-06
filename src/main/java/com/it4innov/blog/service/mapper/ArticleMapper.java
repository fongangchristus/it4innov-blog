package com.it4innov.blog.service.mapper;


import com.it4innov.blog.domain.*;
import com.it4innov.blog.service.dto.ArticleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Article} and its DTO {@link ArticleDTO}.
 */
@Mapper(componentModel = "spring", uses = {CategorieMapper.class, AuteurMapper.class})
public interface ArticleMapper extends EntityMapper<ArticleDTO, Article> {

    @Mapping(source = "categorie.id", target = "categorieId")
    @Mapping(source = "auteur.id", target = "auteurId")
    ArticleDTO toDto(Article article);

    @Mapping(source = "categorieId", target = "categorie")
    @Mapping(source = "auteurId", target = "auteur")
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "removeTag", ignore = true)
    Article toEntity(ArticleDTO articleDTO);

    default Article fromId(Long id) {
        if (id == null) {
            return null;
        }
        Article article = new Article();
        article.setId(id);
        return article;
    }
}
