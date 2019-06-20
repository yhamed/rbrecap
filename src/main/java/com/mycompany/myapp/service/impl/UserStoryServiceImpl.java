package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.UserStoryService;
import com.mycompany.myapp.domain.UserStory;
import com.mycompany.myapp.repository.UserStoryRepository;
import com.mycompany.myapp.repository.search.UserStorySearchRepository;
import com.mycompany.myapp.service.dto.UserStoryDTO;
import com.mycompany.myapp.service.mapper.UserStoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link UserStory}.
 */
@Service
@Transactional
public class UserStoryServiceImpl implements UserStoryService {

    private final Logger log = LoggerFactory.getLogger(UserStoryServiceImpl.class);

    private final UserStoryRepository userStoryRepository;

    private final UserStoryMapper userStoryMapper;

    private final UserStorySearchRepository userStorySearchRepository;

    public UserStoryServiceImpl(UserStoryRepository userStoryRepository, UserStoryMapper userStoryMapper, UserStorySearchRepository userStorySearchRepository) {
        this.userStoryRepository = userStoryRepository;
        this.userStoryMapper = userStoryMapper;
        this.userStorySearchRepository = userStorySearchRepository;
    }

    /**
     * Save a userStory.
     *
     * @param userStoryDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UserStoryDTO save(UserStoryDTO userStoryDTO) {
        log.debug("Request to save UserStory : {}", userStoryDTO);
        UserStory userStory = userStoryMapper.toEntity(userStoryDTO);
        userStory = userStoryRepository.save(userStory);
        UserStoryDTO result = userStoryMapper.toDto(userStory);
        userStorySearchRepository.save(userStory);
        return result;
    }

    /**
     * Get all the userStories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserStoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserStories");
        return userStoryRepository.findAll(pageable)
            .map(userStoryMapper::toDto);
    }


    /**
     * Get one userStory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserStoryDTO> findOne(Long id) {
        log.debug("Request to get UserStory : {}", id);
        return userStoryRepository.findById(id)
            .map(userStoryMapper::toDto);
    }

    /**
     * Delete the userStory by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserStory : {}", id);
        userStoryRepository.deleteById(id);
        userStorySearchRepository.deleteById(id);
    }

    /**
     * Search for the userStory corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserStoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserStories for query {}", query);
        return userStorySearchRepository.search(queryStringQuery(query), pageable)
            .map(userStoryMapper::toDto);
    }
}
