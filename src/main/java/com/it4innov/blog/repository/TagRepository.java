package com.it4innov.blog.repository;

import com.it4innov.blog.domain.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Tag entity.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    @Query(value = "select distinct tag from Tag tag left join fetch tag.articles",
        countQuery = "select count(distinct tag) from Tag tag")
    Page<Tag> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct tag from Tag tag left join fetch tag.articles")
    List<Tag> findAllWithEagerRelationships();

    @Query("select tag from Tag tag left join fetch tag.articles where tag.id =:id")
    Optional<Tag> findOneWithEagerRelationships(@Param("id") Long id);
}
