package com.it4innov.blog.service.mapper;


import com.it4innov.blog.domain.*;
import com.it4innov.blog.service.dto.CategorieDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Categorie} and its DTO {@link CategorieDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CategorieMapper extends EntityMapper<CategorieDTO, Categorie> {


    @Mapping(target = "articles", ignore = true)
    @Mapping(target = "removeArticle", ignore = true)
    Categorie toEntity(CategorieDTO categorieDTO);

    default Categorie fromId(Long id) {
        if (id == null) {
            return null;
        }
        Categorie categorie = new Categorie();
        categorie.setId(id);
        return categorie;
    }
}
