package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.DroitDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Droit}.
 */
public interface DroitService {

    /**
     * Save a droit.
     *
     * @param droitDTO the entity to save.
     * @return the persisted entity.
     */
    DroitDTO save(DroitDTO droitDTO);

    /**
     * Get all the droits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DroitDTO> findAll(Pageable pageable);

    /**
     * Get all the droits with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<DroitDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" droit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DroitDTO> findOne(Long id);

    /**
     * Delete the "id" droit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the droit corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DroitDTO> search(String query, Pageable pageable);
}
