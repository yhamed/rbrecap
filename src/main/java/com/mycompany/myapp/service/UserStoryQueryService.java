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

import com.mycompany.myapp.domain.UserStory;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.UserStoryRepository;
import com.mycompany.myapp.repository.search.UserStorySearchRepository;
import com.mycompany.myapp.service.dto.UserStoryCriteria;
import com.mycompany.myapp.service.dto.UserStoryDTO;
import com.mycompany.myapp.service.mapper.UserStoryMapper;

/**
 * Service for executing complex queries for {@link UserStory} entities in the database.
 * The main input is a {@link UserStoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserStoryDTO} or a {@link Page} of {@link UserStoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserStoryQueryService extends QueryService<UserStory> {

    private final Logger log = LoggerFactory.getLogger(UserStoryQueryService.class);

    private final UserStoryRepository userStoryRepository;

    private final UserStoryMapper userStoryMapper;

    private final UserStorySearchRepository userStorySearchRepository;

    public UserStoryQueryService(UserStoryRepository userStoryRepository, UserStoryMapper userStoryMapper, UserStorySearchRepository userStorySearchRepository) {
        this.userStoryRepository = userStoryRepository;
        this.userStoryMapper = userStoryMapper;
        this.userStorySearchRepository = userStorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link UserStoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserStoryDTO> findByCriteria(UserStoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserStory> specification = createSpecification(criteria);
        return userStoryMapper.toDto(userStoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserStoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserStoryDTO> findByCriteria(UserStoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserStory> specification = createSpecification(criteria);
        return userStoryRepository.findAll(specification, page)
            .map(userStoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserStoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserStory> specification = createSpecification(criteria);
        return userStoryRepository.count(specification);
    }

    /**
     * Function to convert UserStoryCriteria to a {@link Specification}.
     */
    private Specification<UserStory> createSpecification(UserStoryCriteria criteria) {
        Specification<UserStory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), UserStory_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), UserStory_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), UserStory_.description));
            }
            if (criteria.getChiffrage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getChiffrage(), UserStory_.chiffrage));
            }
            if (criteria.getSprintId() != null) {
                specification = specification.and(buildSpecification(criteria.getSprintId(),
                    root -> root.join(UserStory_.sprint, JoinType.LEFT).get(Sprint_.id)));
            }
            if (criteria.getTagsId() != null) {
                specification = specification.and(buildSpecification(criteria.getTagsId(),
                    root -> root.join(UserStory_.tags, JoinType.LEFT).get(Tag_.id)));
            }
            if (criteria.getDroitsId() != null) {
                specification = specification.and(buildSpecification(criteria.getDroitsId(),
                    root -> root.join(UserStory_.droits, JoinType.LEFT).get(Droit_.id)));
            }
        }
        return specification;
    }
}
