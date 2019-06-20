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

import com.mycompany.myapp.domain.Tag;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.TagRepository;
import com.mycompany.myapp.repository.search.TagSearchRepository;
import com.mycompany.myapp.service.dto.TagCriteria;
import com.mycompany.myapp.service.dto.TagDTO;
import com.mycompany.myapp.service.mapper.TagMapper;

/**
 * Service for executing complex queries for {@link Tag} entities in the database.
 * The main input is a {@link TagCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TagDTO} or a {@link Page} of {@link TagDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TagQueryService extends QueryService<Tag> {

    private final Logger log = LoggerFactory.getLogger(TagQueryService.class);

    private final TagRepository tagRepository;

    private final TagMapper tagMapper;

    private final TagSearchRepository tagSearchRepository;

    public TagQueryService(TagRepository tagRepository, TagMapper tagMapper, TagSearchRepository tagSearchRepository) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.tagSearchRepository = tagSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TagDTO> findByCriteria(TagCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tag> specification = createSpecification(criteria);
        return tagMapper.toDto(tagRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TagDTO> findByCriteria(TagCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tag> specification = createSpecification(criteria);
        return tagRepository.findAll(specification, page)
            .map(tagMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TagCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tag> specification = createSpecification(criteria);
        return tagRepository.count(specification);
    }

    /**
     * Function to convert TagCriteria to a {@link Specification}.
     */
    private Specification<Tag> createSpecification(TagCriteria criteria) {
        Specification<Tag> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Tag_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), Tag_.key));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Tag_.name));
            }
            if (criteria.getUserStoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserStoryId(),
                    root -> root.join(Tag_.userStory, JoinType.LEFT).get(UserStory_.id)));
            }
        }
        return specification;
    }
}
