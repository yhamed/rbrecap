package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.SprintService;
import com.mycompany.myapp.domain.Sprint;
import com.mycompany.myapp.repository.SprintRepository;
import com.mycompany.myapp.repository.search.SprintSearchRepository;
import com.mycompany.myapp.service.dto.SprintDTO;
import com.mycompany.myapp.service.mapper.SprintMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Sprint}.
 */
@Service
@Transactional
public class SprintServiceImpl implements SprintService {

    private final Logger log = LoggerFactory.getLogger(SprintServiceImpl.class);

    private final SprintRepository sprintRepository;

    private final SprintMapper sprintMapper;

    private final SprintSearchRepository sprintSearchRepository;

    public SprintServiceImpl(SprintRepository sprintRepository, SprintMapper sprintMapper, SprintSearchRepository sprintSearchRepository) {
        this.sprintRepository = sprintRepository;
        this.sprintMapper = sprintMapper;
        this.sprintSearchRepository = sprintSearchRepository;
    }

    /**
     * Save a sprint.
     *
     * @param sprintDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SprintDTO save(SprintDTO sprintDTO) {
        log.debug("Request to save Sprint : {}", sprintDTO);
        Sprint sprint = sprintMapper.toEntity(sprintDTO);
        sprint = sprintRepository.save(sprint);
        SprintDTO result = sprintMapper.toDto(sprint);
        sprintSearchRepository.save(sprint);
        return result;
    }

    /**
     * Get all the sprints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SprintDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sprints");
        return sprintRepository.findAll(pageable)
            .map(sprintMapper::toDto);
    }


    /**
     * Get one sprint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SprintDTO> findOne(Long id) {
        log.debug("Request to get Sprint : {}", id);
        return sprintRepository.findById(id)
            .map(sprintMapper::toDto);
    }

    /**
     * Delete the sprint by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sprint : {}", id);
        sprintRepository.deleteById(id);
        sprintSearchRepository.deleteById(id);
    }

    /**
     * Search for the sprint corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SprintDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Sprints for query {}", query);
        return sprintSearchRepository.search(queryStringQuery(query), pageable)
            .map(sprintMapper::toDto);
    }
}
