package com.mycompany.myapp.service;

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

import com.mycompany.myapp.domain.Sprint;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.SprintRepository;
import com.mycompany.myapp.repository.search.SprintSearchRepository;
import com.mycompany.myapp.service.dto.SprintCriteria;
import com.mycompany.myapp.service.dto.SprintDTO;
import com.mycompany.myapp.service.mapper.SprintMapper;

/**
 * Service for executing complex queries for {@link Sprint} entities in the database.
 * The main input is a {@link SprintCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SprintDTO} or a {@link Page} of {@link SprintDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SprintQueryService extends QueryService<Sprint> {

    private final Logger log = LoggerFactory.getLogger(SprintQueryService.class);

    private final SprintRepository sprintRepository;

    private final SprintMapper sprintMapper;

    private final SprintSearchRepository sprintSearchRepository;

    public SprintQueryService(SprintRepository sprintRepository, SprintMapper sprintMapper, SprintSearchRepository sprintSearchRepository) {
        this.sprintRepository = sprintRepository;
        this.sprintMapper = sprintMapper;
        this.sprintSearchRepository = sprintSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SprintDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SprintDTO> findByCriteria(SprintCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sprint> specification = createSpecification(criteria);
        return sprintMapper.toDto(sprintRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SprintDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SprintDTO> findByCriteria(SprintCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sprint> specification = createSpecification(criteria);
        return sprintRepository.findAll(specification, page)
            .map(sprintMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SprintCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sprint> specification = createSpecification(criteria);
        return sprintRepository.count(specification);
    }

    /**
     * Function to convert SprintCriteria to a {@link Specification}.
     */
    private Specification<Sprint> createSpecification(SprintCriteria criteria) {
        Specification<Sprint> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Sprint_.id));
            }
            if (criteria.getSprintNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSprintNumber(), Sprint_.sprintNumber));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Sprint_.active));
            }
            if (criteria.getUserStoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserStoryId(),
                    root -> root.join(Sprint_.userStories, JoinType.LEFT).get(UserStory_.id)));
            }
        }
        return specification;
    }
}
