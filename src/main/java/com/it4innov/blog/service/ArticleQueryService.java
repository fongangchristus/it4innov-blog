package com.it4innov.blog.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.it4innov.blog.domain.Article;
import com.it4innov.blog.domain.*; // for static metamodels
import com.it4innov.blog.repository.ArticleRepository;
import com.it4innov.blog.service.dto.ArticleCriteria;
import com.it4innov.blog.service.dto.ArticleDTO;
import com.it4innov.blog.service.mapper.ArticleMapper;

/**
 * Service for executing complex queries for {@link Article} entities in the database.
 * The main input is a {@link ArticleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ArticleDTO} or a {@link Page} of {@link ArticleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArticleQueryService extends QueryService<Article> {

    private final Logger log = LoggerFactory.getLogger(ArticleQueryService.class);

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    public ArticleQueryService(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    /**
     * Return a {@link List} of {@link ArticleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ArticleDTO> findByCriteria(ArticleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Article> specification = createSpecification(criteria);
        return articleMapper.toDto(articleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ArticleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArticleDTO> findByCriteria(ArticleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Article> specification = createSpecification(criteria);
        return articleRepository.findAll(specification, page)
            .map(articleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArticleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Article> specification = createSpecification(criteria);
        return articleRepository.count(specification);
    }

    /**
     * Function to convert {@link ArticleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Article> createSpecification(ArticleCriteria criteria) {
        Specification<Article> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Article_.id));
            }
            if (criteria.getLibele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibele(), Article_.libele));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Article_.description));
            }
            if (criteria.getDateCreation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreation(), Article_.dateCreation));
            }
            if (criteria.getPublier() != null) {
                specification = specification.and(buildSpecification(criteria.getPublier(), Article_.publier));
            }
            if (criteria.getDocMDPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocMDPath(), Article_.docMDPath));
            }
            if (criteria.getCategorieId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategorieId(),
                    root -> root.join(Article_.categorie, JoinType.LEFT).get(Categorie_.id)));
            }
            if (criteria.getAuteurId() != null) {
                specification = specification.and(buildSpecification(criteria.getAuteurId(),
                    root -> root.join(Article_.auteur, JoinType.LEFT).get(Auteur_.id)));
            }
            if (criteria.getTagId() != null) {
                specification = specification.and(buildSpecification(criteria.getTagId(),
                    root -> root.join(Article_.tags, JoinType.LEFT).get(Tag_.id)));
            }
        }
        return specification;
    }
}
