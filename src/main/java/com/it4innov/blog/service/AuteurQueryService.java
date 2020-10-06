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

import com.it4innov.blog.domain.Auteur;
import com.it4innov.blog.domain.*; // for static metamodels
import com.it4innov.blog.repository.AuteurRepository;
import com.it4innov.blog.service.dto.AuteurCriteria;
import com.it4innov.blog.service.dto.AuteurDTO;
import com.it4innov.blog.service.mapper.AuteurMapper;

/**
 * Service for executing complex queries for {@link Auteur} entities in the database.
 * The main input is a {@link AuteurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AuteurDTO} or a {@link Page} of {@link AuteurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuteurQueryService extends QueryService<Auteur> {

    private final Logger log = LoggerFactory.getLogger(AuteurQueryService.class);

    private final AuteurRepository auteurRepository;

    private final AuteurMapper auteurMapper;

    public AuteurQueryService(AuteurRepository auteurRepository, AuteurMapper auteurMapper) {
        this.auteurRepository = auteurRepository;
        this.auteurMapper = auteurMapper;
    }

    /**
     * Return a {@link List} of {@link AuteurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuteurDTO> findByCriteria(AuteurCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Auteur> specification = createSpecification(criteria);
        return auteurMapper.toDto(auteurRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AuteurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuteurDTO> findByCriteria(AuteurCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Auteur> specification = createSpecification(criteria);
        return auteurRepository.findAll(specification, page)
            .map(auteurMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuteurCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Auteur> specification = createSpecification(criteria);
        return auteurRepository.count(specification);
    }

    /**
     * Function to convert {@link AuteurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Auteur> createSpecification(AuteurCriteria criteria) {
        Specification<Auteur> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Auteur_.id));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), Auteur_.login));
            }
            if (criteria.getFirstname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstname(), Auteur_.firstname));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Auteur_.password));
            }
            if (criteria.getTweeter() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTweeter(), Auteur_.tweeter));
            }
            if (criteria.getLinkedin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLinkedin(), Auteur_.linkedin));
            }
            if (criteria.getFacebook() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFacebook(), Auteur_.facebook));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Auteur_.description));
            }
            if (criteria.getSlogan() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlogan(), Auteur_.slogan));
            }
            if (criteria.getArticleId() != null) {
                specification = specification.and(buildSpecification(criteria.getArticleId(),
                    root -> root.join(Auteur_.articles, JoinType.LEFT).get(Article_.id)));
            }
        }
        return specification;
    }
}
