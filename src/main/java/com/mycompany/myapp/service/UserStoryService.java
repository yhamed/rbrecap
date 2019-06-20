package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.UserStoryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.UserStory}.
 */
public interface UserStoryService {

    /**
     * Save a userStory.
     *
     * @param userStoryDTO the entity to save.
     * @return the persisted entity.
     */
    UserStoryDTO save(UserStoryDTO userStoryDTO);

    /**
     * Get all the userStories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserStoryDTO> findAll(Pageable pageable);


    /**
     * Get the "id" userStory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserStoryDTO> findOne(Long id);

    /**
     * Delete the "id" userStory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the userStory corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserStoryDTO> search(String query, Pageable pageable);
}
