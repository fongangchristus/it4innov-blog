package com.it4innov.blog.repository;

import com.it4innov.blog.domain.Auteur;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Auteur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuteurRepository extends JpaRepository<Auteur, Long>, JpaSpecificationExecutor<Auteur> {
}
