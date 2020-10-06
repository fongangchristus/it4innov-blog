package com.it4innov.blog.service.mapper;


import com.it4innov.blog.domain.*;
import com.it4innov.blog.service.dto.AuteurDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Auteur} and its DTO {@link AuteurDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuteurMapper extends EntityMapper<AuteurDTO, Auteur> {


    @Mapping(target = "articles", ignore = true)
    @Mapping(target = "removeArticle", ignore = true)
    Auteur toEntity(AuteurDTO auteurDTO);

    default Auteur fromId(Long id) {
        if (id == null) {
            return null;
        }
        Auteur auteur = new Auteur();
        auteur.setId(id);
        return auteur;
    }
}
