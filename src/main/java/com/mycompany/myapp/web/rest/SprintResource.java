package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.SprintService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.SprintDTO;
import com.mycompany.myapp.service.dto.SprintCriteria;
import com.mycompany.myapp.service.SprintQueryService;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.Sprint}.
 */
@RestController
@RequestMapping("/api")
public class SprintResource {

    private final Logger log = LoggerFactory.getLogger(SprintResource.class);

    private static final String ENTITY_NAME = "sprint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SprintService sprintService;

    private final SprintQueryService sprintQueryService;

    public SprintResource(SprintService sprintService, SprintQueryService sprintQueryService) {
        this.sprintService = sprintService;
        this.sprintQueryService = sprintQueryService;
    }

    /**
     * {@code POST  /sprints} : Create a new sprint.
     *
     * @param sprintDTO the sprintDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sprintDTO, or with status {@code 400 (Bad Request)} if the sprint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sprints")
    public ResponseEntity<SprintDTO> createSprint(@Valid @RequestBody SprintDTO sprintDTO) throws URISyntaxException {
        log.debug("REST request to save Sprint : {}", sprintDTO);
        if (sprintDTO.getId() != null) {
            throw new BadRequestAlertException("A new sprint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SprintDTO result = sprintService.save(sprintDTO);
        return ResponseEntity.created(new URI("/api/sprints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sprints} : Updates an existing sprint.
     *
     * @param sprintDTO the sprintDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sprintDTO,
     * or with status {@code 400 (Bad Request)} if the sprintDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sprintDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sprints")
    public ResponseEntity<SprintDTO> updateSprint(@Valid @RequestBody SprintDTO sprintDTO) throws URISyntaxException {
        log.debug("REST request to update Sprint : {}", sprintDTO);
        if (sprintDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SprintDTO result = sprintService.save(sprintDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sprintDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sprints} : get all the sprints.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sprints in body.
     */
    @GetMapping("/sprints")
    public ResponseEntity<List<SprintDTO>> getAllSprints(SprintCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get Sprints by criteria: {}", criteria);
        Page<SprintDTO> page = sprintQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /sprints/count} : count all the sprints.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/sprints/count")
    public ResponseEntity<Long> countSprints(SprintCriteria criteria) {
        log.debug("REST request to count Sprints by criteria: {}", criteria);
        return ResponseEntity.ok().body(sprintQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sprints/:id} : get the "id" sprint.
     *
     * @param id the id of the sprintDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sprintDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sprints/{id}")
    public ResponseEntity<SprintDTO> getSprint(@PathVariable Long id) {
        log.debug("REST request to get Sprint : {}", id);
        Optional<SprintDTO> sprintDTO = sprintService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sprintDTO);
    }

    /**
     * {@code DELETE  /sprints/:id} : delete the "id" sprint.
     *
     * @param id the id of the sprintDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sprints/{id}")
    public ResponseEntity<Void> deleteSprint(@PathVariable Long id) {
        log.debug("REST request to delete Sprint : {}", id);
        sprintService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/sprints?query=:query} : search for the sprint corresponding
     * to the query.
     *
     * @param query the query of the sprint search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/sprints")
    public ResponseEntity<List<SprintDTO>> searchSprints(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Sprints for query {}", query);
        Page<SprintDTO> page = sprintService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
