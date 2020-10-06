package com.it4innov.blog.service.mapper;


import com.it4innov.blog.domain.*;
import com.it4innov.blog.service.dto.TagDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring", uses = {ArticleMapper.class})
public interface TagMapper extends EntityMapper<TagDTO, Tag> {


    @Mapping(target = "removeArticle", ignore = true)

    default Tag fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setId(id);
        return tag;
    }
}
