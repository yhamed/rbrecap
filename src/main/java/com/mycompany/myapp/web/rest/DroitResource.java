package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.DroitService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.DroitDTO;
import com.mycompany.myapp.service.dto.DroitCriteria;
import com.mycompany.myapp.service.DroitQueryService;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.Droit}.
 */
@RestController
@RequestMapping("/api")
public class DroitResource {

    private final Logger log = LoggerFactory.getLogger(DroitResource.class);

    private static final String ENTITY_NAME = "droit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DroitService droitService;

    private final DroitQueryService droitQueryService;

    public DroitResource(DroitService droitService, DroitQueryService droitQueryService) {
        this.droitService = droitService;
        this.droitQueryService = droitQueryService;
    }

    /**
     * {@code POST  /droits} : Create a new droit.
     *
     * @param droitDTO the droitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new droitDTO, or with status {@code 400 (Bad Request)} if the droit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/droits")
    public ResponseEntity<DroitDTO> createDroit(@Valid @RequestBody DroitDTO droitDTO) throws URISyntaxException {
        log.debug("REST request to save Droit : {}", droitDTO);
        if (droitDTO.getId() != null) {
            throw new BadRequestAlertException("A new droit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DroitDTO result = droitService.save(droitDTO);
        return ResponseEntity.created(new URI("/api/droits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /droits} : Updates an existing droit.
     *
     * @param droitDTO the droitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated droitDTO,
     * or with status {@code 400 (Bad Request)} if the droitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the droitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/droits")
    public ResponseEntity<DroitDTO> updateDroit(@Valid @RequestBody DroitDTO droitDTO) throws URISyntaxException {
        log.debug("REST request to update Droit : {}", droitDTO);
        if (droitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DroitDTO result = droitService.save(droitDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, droitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /droits} : get all the droits.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of droits in body.
     */
    @GetMapping("/droits")
    public ResponseEntity<List<DroitDTO>> getAllDroits(DroitCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get Droits by criteria: {}", criteria);
        Page<DroitDTO> page = droitQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /droits/count} : count all the droits.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/droits/count")
    public ResponseEntity<Long> countDroits(DroitCriteria criteria) {
        log.debug("REST request to count Droits by criteria: {}", criteria);
        return ResponseEntity.ok().body(droitQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /droits/:id} : get the "id" droit.
     *
     * @param id the id of the droitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the droitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/droits/{id}")
    public ResponseEntity<DroitDTO> getDroit(@PathVariable Long id) {
        log.debug("REST request to get Droit : {}", id);
        Optional<DroitDTO> droitDTO = droitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(droitDTO);
    }

    /**
     * {@code DELETE  /droits/:id} : delete the "id" droit.
     *
     * @param id the id of the droitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/droits/{id}")
    public ResponseEntity<Void> deleteDroit(@PathVariable Long id) {
        log.debug("REST request to delete Droit : {}", id);
        droitService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/droits?query=:query} : search for the droit corresponding
     * to the query.
     *
     * @param query the query of the droit search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/droits")
    public ResponseEntity<List<DroitDTO>> searchDroits(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Droits for query {}", query);
        Page<DroitDTO> page = droitService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
