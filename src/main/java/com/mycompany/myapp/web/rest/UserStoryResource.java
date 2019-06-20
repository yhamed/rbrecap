package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.UserStoryService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.UserStoryDTO;
import com.mycompany.myapp.service.dto.UserStoryCriteria;
import com.mycompany.myapp.service.UserStoryQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.UserStory}.
 */
@RestController
@RequestMapping("/api")
public class UserStoryResource {

    private final Logger log = LoggerFactory.getLogger(UserStoryResource.class);

    private static final String ENTITY_NAME = "userStory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserStoryService userStoryService;

    private final UserStoryQueryService userStoryQueryService;

    public UserStoryResource(UserStoryService userStoryService, UserStoryQueryService userStoryQueryService) {
        this.userStoryService = userStoryService;
        this.userStoryQueryService = userStoryQueryService;
    }

    /**
     * {@code POST  /user-stories} : Create a new userStory.
     *
     * @param userStoryDTO the userStoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userStoryDTO, or with status {@code 400 (Bad Request)} if the userStory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-stories")
    public ResponseEntity<UserStoryDTO> createUserStory(@Valid @RequestBody UserStoryDTO userStoryDTO) throws URISyntaxException {
        log.debug("REST request to save UserStory : {}", userStoryDTO);
        if (userStoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new userStory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserStoryDTO result = userStoryService.save(userStoryDTO);
        return ResponseEntity.created(new URI("/api/user-stories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-stories} : Updates an existing userStory.
     *
     * @param userStoryDTO the userStoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userStoryDTO,
     * or with status {@code 400 (Bad Request)} if the userStoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userStoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-stories")
    public ResponseEntity<UserStoryDTO> updateUserStory(@Valid @RequestBody UserStoryDTO userStoryDTO) throws URISyntaxException {
        log.debug("REST request to update UserStory : {}", userStoryDTO);
        if (userStoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserStoryDTO result = userStoryService.save(userStoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userStoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-stories} : get all the userStories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userStories in body.
     */
    @GetMapping("/user-stories")
    public ResponseEntity<List<UserStoryDTO>> getAllUserStories(UserStoryCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get UserStories by criteria: {}", criteria);
        Page<UserStoryDTO> page = userStoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /user-stories/count} : count all the userStories.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/user-stories/count")
    public ResponseEntity<Long> countUserStories(UserStoryCriteria criteria) {
        log.debug("REST request to count UserStories by criteria: {}", criteria);
        return ResponseEntity.ok().body(userStoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-stories/:id} : get the "id" userStory.
     *
     * @param id the id of the userStoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userStoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-stories/{id}")
    public ResponseEntity<UserStoryDTO> getUserStory(@PathVariable Long id) {
        log.debug("REST request to get UserStory : {}", id);
        Optional<UserStoryDTO> userStoryDTO = userStoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userStoryDTO);
    }

    /**
     * {@code DELETE  /user-stories/:id} : delete the "id" userStory.
     *
     * @param id the id of the userStoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-stories/{id}")
    public ResponseEntity<Void> deleteUserStory(@PathVariable Long id) {
        log.debug("REST request to delete UserStory : {}", id);
        userStoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/user-stories?query=:query} : search for the userStory corresponding
     * to the query.
     *
     * @param query the query of the userStory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/user-stories")
    public ResponseEntity<List<UserStoryDTO>> searchUserStories(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of UserStories for query {}", query);
        Page<UserStoryDTO> page = userStoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
