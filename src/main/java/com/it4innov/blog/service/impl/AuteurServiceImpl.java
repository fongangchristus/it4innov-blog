package com.it4innov.blog.service.impl;

import com.it4innov.blog.service.AuteurService;
import com.it4innov.blog.domain.Auteur;
import com.it4innov.blog.repository.AuteurRepository;
import com.it4innov.blog.service.dto.AuteurDTO;
import com.it4innov.blog.service.mapper.AuteurMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Auteur}.
 */
@Service
@Transactional
public class AuteurServiceImpl implements AuteurService {

    private final Logger log = LoggerFactory.getLogger(AuteurServiceImpl.class);

    private final AuteurRepository auteurRepository;

    private final AuteurMapper auteurMapper;

    public AuteurServiceImpl(AuteurRepository auteurRepository, AuteurMapper auteurMapper) {
        this.auteurRepository = auteurRepository;
        this.auteurMapper = auteurMapper;
    }

    @Override
    public AuteurDTO save(AuteurDTO auteurDTO) {
        log.debug("Request to save Auteur : {}", auteurDTO);
        Auteur auteur = auteurMapper.toEntity(auteurDTO);
        auteur = auteurRepository.save(auteur);
        return auteurMapper.toDto(auteur);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuteurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Auteurs");
        return auteurRepository.findAll(pageable)
            .map(auteurMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AuteurDTO> findOne(Long id) {
        log.debug("Request to get Auteur : {}", id);
        return auteurRepository.findById(id)
            .map(auteurMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Auteur : {}", id);
        auteurRepository.deleteById(id);
    }
}
