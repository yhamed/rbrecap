package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.DroitService;
import com.mycompany.myapp.domain.Droit;
import com.mycompany.myapp.repository.DroitRepository;
import com.mycompany.myapp.repository.search.DroitSearchRepository;
import com.mycompany.myapp.service.dto.DroitDTO;
import com.mycompany.myapp.service.mapper.DroitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Droit}.
 */
@Service
@Transactional
public class DroitServiceImpl implements DroitService {

    private final Logger log = LoggerFactory.getLogger(DroitServiceImpl.class);

    private final DroitRepository droitRepository;

    private final DroitMapper droitMapper;

    private final DroitSearchRepository droitSearchRepository;

    public DroitServiceImpl(DroitRepository droitRepository, DroitMapper droitMapper, DroitSearchRepository droitSearchRepository) {
        this.droitRepository = droitRepository;
        this.droitMapper = droitMapper;
        this.droitSearchRepository = droitSearchRepository;
    }

    /**
     * Save a droit.
     *
     * @param droitDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DroitDTO save(DroitDTO droitDTO) {
        log.debug("Request to save Droit : {}", droitDTO);
        Droit droit = droitMapper.toEntity(droitDTO);
        droit = droitRepository.save(droit);
        DroitDTO result = droitMapper.toDto(droit);
        droitSearchRepository.save(droit);
        return result;
    }

    /**
     * Get all the droits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DroitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Droits");
        return droitRepository.findAll(pageable)
            .map(droitMapper::toDto);
    }

    /**
     * Get all the droits with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DroitDTO> findAllWithEagerRelationships(Pageable pageable) {
        return droitRepository.findAllWithEagerRelationships(pageable).map(droitMapper::toDto);
    }
    

    /**
     * Get one droit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DroitDTO> findOne(Long id) {
        log.debug("Request to get Droit : {}", id);
        return droitRepository.findOneWithEagerRelationships(id)
            .map(droitMapper::toDto);
    }

    /**
     * Delete the droit by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Droit : {}", id);
        droitRepository.deleteById(id);
        droitSearchRepository.deleteById(id);
    }

    /**
     * Search for the droit corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DroitDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Droits for query {}", query);
        return droitSearchRepository.search(queryStringQuery(query), pageable)
            .map(droitMapper::toDto);
    }
}
