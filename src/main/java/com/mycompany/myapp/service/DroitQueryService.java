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

import com.mycompany.myapp.domain.Droit;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.DroitRepository;
import com.mycompany.myapp.repository.search.DroitSearchRepository;
import com.mycompany.myapp.service.dto.DroitCriteria;
import com.mycompany.myapp.service.dto.DroitDTO;
import com.mycompany.myapp.service.mapper.DroitMapper;

/**
 * Service for executing complex queries for {@link Droit} entities in the database.
 * The main input is a {@link DroitCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DroitDTO} or a {@link Page} of {@link DroitDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DroitQueryService extends QueryService<Droit> {

    private final Logger log = LoggerFactory.getLogger(DroitQueryService.class);

    private final DroitRepository droitRepository;

    private final DroitMapper droitMapper;

    private final DroitSearchRepository droitSearchRepository;

    public DroitQueryService(DroitRepository droitRepository, DroitMapper droitMapper, DroitSearchRepository droitSearchRepository) {
        this.droitRepository = droitRepository;
        this.droitMapper = droitMapper;
        this.droitSearchRepository = droitSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DroitDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DroitDTO> findByCriteria(DroitCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Droit> specification = createSpecification(criteria);
        return droitMapper.toDto(droitRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DroitDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DroitDTO> findByCriteria(DroitCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Droit> specification = createSpecification(criteria);
        return droitRepository.findAll(specification, page)
            .map(droitMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DroitCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Droit> specification = createSpecification(criteria);
        return droitRepository.count(specification);
    }

    /**
     * Function to convert DroitCriteria to a {@link Specification}.
     */
    private Specification<Droit> createSpecification(DroitCriteria criteria) {
        Specification<Droit> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Droit_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Droit_.name));
            }
            if (criteria.getIsSwitch() != null) {
                specification = specification.and(buildSpecification(criteria.getIsSwitch(), Droit_.isSwitch));
            }
            if (criteria.getUserStoriesId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserStoriesId(),
                    root -> root.join(Droit_.userStories, JoinType.LEFT).get(UserStory_.id)));
            }
        }
        return specification;
    }
}
