package com.it4innov.blog.service;

import com.it4innov.blog.service.dto.AuteurDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.it4innov.blog.domain.Auteur}.
 */
public interface AuteurService {

    /**
     * Save a auteur.
     *
     * @param auteurDTO the entity to save.
     * @return the persisted entity.
     */
    AuteurDTO save(AuteurDTO auteurDTO);

    /**
     * Get all the auteurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AuteurDTO> findAll(Pageable pageable);


    /**
     * Get the "id" auteur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AuteurDTO> findOne(Long id);

    /**
     * Delete the "id" auteur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
