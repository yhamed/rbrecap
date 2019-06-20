package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RbRecapApp;
import com.mycompany.myapp.domain.Sprint;
import com.mycompany.myapp.domain.UserStory;
import com.mycompany.myapp.repository.SprintRepository;
import com.mycompany.myapp.repository.search.SprintSearchRepository;
import com.mycompany.myapp.service.SprintService;
import com.mycompany.myapp.service.dto.SprintDTO;
import com.mycompany.myapp.service.mapper.SprintMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.SprintCriteria;
import com.mycompany.myapp.service.SprintQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link SprintResource} REST controller.
 */
@SpringBootTest(classes = RbRecapApp.class)
public class SprintResourceIT {

    private static final Integer DEFAULT_SPRINT_NUMBER = 0;
    private static final Integer UPDATED_SPRINT_NUMBER = 1;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private SprintMapper sprintMapper;

    @Autowired
    private SprintService sprintService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.SprintSearchRepositoryMockConfiguration
     */
    @Autowired
    private SprintSearchRepository mockSprintSearchRepository;

    @Autowired
    private SprintQueryService sprintQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSprintMockMvc;

    private Sprint sprint;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SprintResource sprintResource = new SprintResource(sprintService, sprintQueryService);
        this.restSprintMockMvc = MockMvcBuilders.standaloneSetup(sprintResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sprint createEntity(EntityManager em) {
        Sprint sprint = new Sprint()
            .sprintNumber(DEFAULT_SPRINT_NUMBER)
            .active(DEFAULT_ACTIVE);
        return sprint;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sprint createUpdatedEntity(EntityManager em) {
        Sprint sprint = new Sprint()
            .sprintNumber(UPDATED_SPRINT_NUMBER)
            .active(UPDATED_ACTIVE);
        return sprint;
    }

    @BeforeEach
    public void initTest() {
        sprint = createEntity(em);
    }

    @Test
    @Transactional
    public void createSprint() throws Exception {
        int databaseSizeBeforeCreate = sprintRepository.findAll().size();

        // Create the Sprint
        SprintDTO sprintDTO = sprintMapper.toDto(sprint);
        restSprintMockMvc.perform(post("/api/sprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintDTO)))
            .andExpect(status().isCreated());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeCreate + 1);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getSprintNumber()).isEqualTo(DEFAULT_SPRINT_NUMBER);
        assertThat(testSprint.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Sprint in Elasticsearch
        verify(mockSprintSearchRepository, times(1)).save(testSprint);
    }

    @Test
    @Transactional
    public void createSprintWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sprintRepository.findAll().size();

        // Create the Sprint with an existing ID
        sprint.setId(1L);
        SprintDTO sprintDTO = sprintMapper.toDto(sprint);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSprintMockMvc.perform(post("/api/sprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeCreate);

        // Validate the Sprint in Elasticsearch
        verify(mockSprintSearchRepository, times(0)).save(sprint);
    }


    @Test
    @Transactional
    public void checkSprintNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = sprintRepository.findAll().size();
        // set the field null
        sprint.setSprintNumber(null);

        // Create the Sprint, which fails.
        SprintDTO sprintDTO = sprintMapper.toDto(sprint);

        restSprintMockMvc.perform(post("/api/sprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintDTO)))
            .andExpect(status().isBadRequest());

        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSprints() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList
        restSprintMockMvc.perform(get("/api/sprints?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].sprintNumber").value(hasItem(DEFAULT_SPRINT_NUMBER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get the sprint
        restSprintMockMvc.perform(get("/api/sprints/{id}", sprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sprint.getId().intValue()))
            .andExpect(jsonPath("$.sprintNumber").value(DEFAULT_SPRINT_NUMBER))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintNumber equals to DEFAULT_SPRINT_NUMBER
        defaultSprintShouldBeFound("sprintNumber.equals=" + DEFAULT_SPRINT_NUMBER);

        // Get all the sprintList where sprintNumber equals to UPDATED_SPRINT_NUMBER
        defaultSprintShouldNotBeFound("sprintNumber.equals=" + UPDATED_SPRINT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintNumberIsInShouldWork() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintNumber in DEFAULT_SPRINT_NUMBER or UPDATED_SPRINT_NUMBER
        defaultSprintShouldBeFound("sprintNumber.in=" + DEFAULT_SPRINT_NUMBER + "," + UPDATED_SPRINT_NUMBER);

        // Get all the sprintList where sprintNumber equals to UPDATED_SPRINT_NUMBER
        defaultSprintShouldNotBeFound("sprintNumber.in=" + UPDATED_SPRINT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintNumber is not null
        defaultSprintShouldBeFound("sprintNumber.specified=true");

        // Get all the sprintList where sprintNumber is null
        defaultSprintShouldNotBeFound("sprintNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintNumber greater than or equals to DEFAULT_SPRINT_NUMBER
        defaultSprintShouldBeFound("sprintNumber.greaterOrEqualThan=" + DEFAULT_SPRINT_NUMBER);

        // Get all the sprintList where sprintNumber greater than or equals to UPDATED_SPRINT_NUMBER
        defaultSprintShouldNotBeFound("sprintNumber.greaterOrEqualThan=" + UPDATED_SPRINT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintNumber less than or equals to DEFAULT_SPRINT_NUMBER
        defaultSprintShouldNotBeFound("sprintNumber.lessThan=" + DEFAULT_SPRINT_NUMBER);

        // Get all the sprintList where sprintNumber less than or equals to UPDATED_SPRINT_NUMBER
        defaultSprintShouldBeFound("sprintNumber.lessThan=" + UPDATED_SPRINT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllSprintsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where active equals to DEFAULT_ACTIVE
        defaultSprintShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the sprintList where active equals to UPDATED_ACTIVE
        defaultSprintShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllSprintsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultSprintShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the sprintList where active equals to UPDATED_ACTIVE
        defaultSprintShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllSprintsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where active is not null
        defaultSprintShouldBeFound("active.specified=true");

        // Get all the sprintList where active is null
        defaultSprintShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllSprintsByUserStoryIsEqualToSomething() throws Exception {
        // Initialize the database
        UserStory userStory = UserStoryResourceIT.createEntity(em);
        em.persist(userStory);
        em.flush();
        sprint.addUserStory(userStory);
        sprintRepository.saveAndFlush(sprint);
        Long userStoryId = userStory.getId();

        // Get all the sprintList where userStory equals to userStoryId
        defaultSprintShouldBeFound("userStoryId.equals=" + userStoryId);

        // Get all the sprintList where userStory equals to userStoryId + 1
        defaultSprintShouldNotBeFound("userStoryId.equals=" + (userStoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSprintShouldBeFound(String filter) throws Exception {
        restSprintMockMvc.perform(get("/api/sprints?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].sprintNumber").value(hasItem(DEFAULT_SPRINT_NUMBER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restSprintMockMvc.perform(get("/api/sprints/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSprintShouldNotBeFound(String filter) throws Exception {
        restSprintMockMvc.perform(get("/api/sprints?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSprintMockMvc.perform(get("/api/sprints/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSprint() throws Exception {
        // Get the sprint
        restSprintMockMvc.perform(get("/api/sprints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();

        // Update the sprint
        Sprint updatedSprint = sprintRepository.findById(sprint.getId()).get();
        // Disconnect from session so that the updates on updatedSprint are not directly saved in db
        em.detach(updatedSprint);
        updatedSprint
            .sprintNumber(UPDATED_SPRINT_NUMBER)
            .active(UPDATED_ACTIVE);
        SprintDTO sprintDTO = sprintMapper.toDto(updatedSprint);

        restSprintMockMvc.perform(put("/api/sprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintDTO)))
            .andExpect(status().isOk());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getSprintNumber()).isEqualTo(UPDATED_SPRINT_NUMBER);
        assertThat(testSprint.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Sprint in Elasticsearch
        verify(mockSprintSearchRepository, times(1)).save(testSprint);
    }

    @Test
    @Transactional
    public void updateNonExistingSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();

        // Create the Sprint
        SprintDTO sprintDTO = sprintMapper.toDto(sprint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSprintMockMvc.perform(put("/api/sprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Sprint in Elasticsearch
        verify(mockSprintSearchRepository, times(0)).save(sprint);
    }

    @Test
    @Transactional
    public void deleteSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        int databaseSizeBeforeDelete = sprintRepository.findAll().size();

        // Delete the sprint
        restSprintMockMvc.perform(delete("/api/sprints/{id}", sprint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Sprint in Elasticsearch
        verify(mockSprintSearchRepository, times(1)).deleteById(sprint.getId());
    }

    @Test
    @Transactional
    public void searchSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);
        when(mockSprintSearchRepository.search(queryStringQuery("id:" + sprint.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(sprint), PageRequest.of(0, 1), 1));
        // Search the sprint
        restSprintMockMvc.perform(get("/api/_search/sprints?query=id:" + sprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].sprintNumber").value(hasItem(DEFAULT_SPRINT_NUMBER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sprint.class);
        Sprint sprint1 = new Sprint();
        sprint1.setId(1L);
        Sprint sprint2 = new Sprint();
        sprint2.setId(sprint1.getId());
        assertThat(sprint1).isEqualTo(sprint2);
        sprint2.setId(2L);
        assertThat(sprint1).isNotEqualTo(sprint2);
        sprint1.setId(null);
        assertThat(sprint1).isNotEqualTo(sprint2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SprintDTO.class);
        SprintDTO sprintDTO1 = new SprintDTO();
        sprintDTO1.setId(1L);
        SprintDTO sprintDTO2 = new SprintDTO();
        assertThat(sprintDTO1).isNotEqualTo(sprintDTO2);
        sprintDTO2.setId(sprintDTO1.getId());
        assertThat(sprintDTO1).isEqualTo(sprintDTO2);
        sprintDTO2.setId(2L);
        assertThat(sprintDTO1).isNotEqualTo(sprintDTO2);
        sprintDTO1.setId(null);
        assertThat(sprintDTO1).isNotEqualTo(sprintDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sprintMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sprintMapper.fromId(null)).isNull();
    }
}
