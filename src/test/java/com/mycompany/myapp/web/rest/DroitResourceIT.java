package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RbRecapApp;
import com.mycompany.myapp.domain.Droit;
import com.mycompany.myapp.domain.UserStory;
import com.mycompany.myapp.repository.DroitRepository;
import com.mycompany.myapp.repository.search.DroitSearchRepository;
import com.mycompany.myapp.service.DroitService;
import com.mycompany.myapp.service.dto.DroitDTO;
import com.mycompany.myapp.service.mapper.DroitMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.DroitCriteria;
import com.mycompany.myapp.service.DroitQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import java.util.ArrayList;
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
 * Integration tests for the {@Link DroitResource} REST controller.
 */
@SpringBootTest(classes = RbRecapApp.class)
public class DroitResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SWITCH = false;
    private static final Boolean UPDATED_IS_SWITCH = true;

    @Autowired
    private DroitRepository droitRepository;

    @Mock
    private DroitRepository droitRepositoryMock;

    @Autowired
    private DroitMapper droitMapper;

    @Mock
    private DroitService droitServiceMock;

    @Autowired
    private DroitService droitService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.DroitSearchRepositoryMockConfiguration
     */
    @Autowired
    private DroitSearchRepository mockDroitSearchRepository;

    @Autowired
    private DroitQueryService droitQueryService;

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

    private MockMvc restDroitMockMvc;

    private Droit droit;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DroitResource droitResource = new DroitResource(droitService, droitQueryService);
        this.restDroitMockMvc = MockMvcBuilders.standaloneSetup(droitResource)
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
    public static Droit createEntity(EntityManager em) {
        Droit droit = new Droit()
            .name(DEFAULT_NAME)
            .isSwitch(DEFAULT_IS_SWITCH);
        return droit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Droit createUpdatedEntity(EntityManager em) {
        Droit droit = new Droit()
            .name(UPDATED_NAME)
            .isSwitch(UPDATED_IS_SWITCH);
        return droit;
    }

    @BeforeEach
    public void initTest() {
        droit = createEntity(em);
    }

    @Test
    @Transactional
    public void createDroit() throws Exception {
        int databaseSizeBeforeCreate = droitRepository.findAll().size();

        // Create the Droit
        DroitDTO droitDTO = droitMapper.toDto(droit);
        restDroitMockMvc.perform(post("/api/droits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(droitDTO)))
            .andExpect(status().isCreated());

        // Validate the Droit in the database
        List<Droit> droitList = droitRepository.findAll();
        assertThat(droitList).hasSize(databaseSizeBeforeCreate + 1);
        Droit testDroit = droitList.get(droitList.size() - 1);
        assertThat(testDroit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDroit.isIsSwitch()).isEqualTo(DEFAULT_IS_SWITCH);

        // Validate the Droit in Elasticsearch
        verify(mockDroitSearchRepository, times(1)).save(testDroit);
    }

    @Test
    @Transactional
    public void createDroitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = droitRepository.findAll().size();

        // Create the Droit with an existing ID
        droit.setId(1L);
        DroitDTO droitDTO = droitMapper.toDto(droit);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDroitMockMvc.perform(post("/api/droits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(droitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Droit in the database
        List<Droit> droitList = droitRepository.findAll();
        assertThat(droitList).hasSize(databaseSizeBeforeCreate);

        // Validate the Droit in Elasticsearch
        verify(mockDroitSearchRepository, times(0)).save(droit);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = droitRepository.findAll().size();
        // set the field null
        droit.setName(null);

        // Create the Droit, which fails.
        DroitDTO droitDTO = droitMapper.toDto(droit);

        restDroitMockMvc.perform(post("/api/droits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(droitDTO)))
            .andExpect(status().isBadRequest());

        List<Droit> droitList = droitRepository.findAll();
        assertThat(droitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDroits() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);

        // Get all the droitList
        restDroitMockMvc.perform(get("/api/droits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(droit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].isSwitch").value(hasItem(DEFAULT_IS_SWITCH.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllDroitsWithEagerRelationshipsIsEnabled() throws Exception {
        DroitResource droitResource = new DroitResource(droitServiceMock, droitQueryService);
        when(droitServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restDroitMockMvc = MockMvcBuilders.standaloneSetup(droitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDroitMockMvc.perform(get("/api/droits?eagerload=true"))
        .andExpect(status().isOk());

        verify(droitServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDroitsWithEagerRelationshipsIsNotEnabled() throws Exception {
        DroitResource droitResource = new DroitResource(droitServiceMock, droitQueryService);
            when(droitServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restDroitMockMvc = MockMvcBuilders.standaloneSetup(droitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDroitMockMvc.perform(get("/api/droits?eagerload=true"))
        .andExpect(status().isOk());

            verify(droitServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getDroit() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);

        // Get the droit
        restDroitMockMvc.perform(get("/api/droits/{id}", droit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(droit.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.isSwitch").value(DEFAULT_IS_SWITCH.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllDroitsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);

        // Get all the droitList where name equals to DEFAULT_NAME
        defaultDroitShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the droitList where name equals to UPDATED_NAME
        defaultDroitShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDroitsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);

        // Get all the droitList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDroitShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the droitList where name equals to UPDATED_NAME
        defaultDroitShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDroitsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);

        // Get all the droitList where name is not null
        defaultDroitShouldBeFound("name.specified=true");

        // Get all the droitList where name is null
        defaultDroitShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllDroitsByIsSwitchIsEqualToSomething() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);

        // Get all the droitList where isSwitch equals to DEFAULT_IS_SWITCH
        defaultDroitShouldBeFound("isSwitch.equals=" + DEFAULT_IS_SWITCH);

        // Get all the droitList where isSwitch equals to UPDATED_IS_SWITCH
        defaultDroitShouldNotBeFound("isSwitch.equals=" + UPDATED_IS_SWITCH);
    }

    @Test
    @Transactional
    public void getAllDroitsByIsSwitchIsInShouldWork() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);

        // Get all the droitList where isSwitch in DEFAULT_IS_SWITCH or UPDATED_IS_SWITCH
        defaultDroitShouldBeFound("isSwitch.in=" + DEFAULT_IS_SWITCH + "," + UPDATED_IS_SWITCH);

        // Get all the droitList where isSwitch equals to UPDATED_IS_SWITCH
        defaultDroitShouldNotBeFound("isSwitch.in=" + UPDATED_IS_SWITCH);
    }

    @Test
    @Transactional
    public void getAllDroitsByIsSwitchIsNullOrNotNull() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);

        // Get all the droitList where isSwitch is not null
        defaultDroitShouldBeFound("isSwitch.specified=true");

        // Get all the droitList where isSwitch is null
        defaultDroitShouldNotBeFound("isSwitch.specified=false");
    }

    @Test
    @Transactional
    public void getAllDroitsByUserStoriesIsEqualToSomething() throws Exception {
        // Initialize the database
        UserStory userStories = UserStoryResourceIT.createEntity(em);
        em.persist(userStories);
        em.flush();
        droit.addUserStories(userStories);
        droitRepository.saveAndFlush(droit);
        Long userStoriesId = userStories.getId();

        // Get all the droitList where userStories equals to userStoriesId
        defaultDroitShouldBeFound("userStoriesId.equals=" + userStoriesId);

        // Get all the droitList where userStories equals to userStoriesId + 1
        defaultDroitShouldNotBeFound("userStoriesId.equals=" + (userStoriesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDroitShouldBeFound(String filter) throws Exception {
        restDroitMockMvc.perform(get("/api/droits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(droit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isSwitch").value(hasItem(DEFAULT_IS_SWITCH.booleanValue())));

        // Check, that the count call also returns 1
        restDroitMockMvc.perform(get("/api/droits/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDroitShouldNotBeFound(String filter) throws Exception {
        restDroitMockMvc.perform(get("/api/droits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDroitMockMvc.perform(get("/api/droits/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDroit() throws Exception {
        // Get the droit
        restDroitMockMvc.perform(get("/api/droits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDroit() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);

        int databaseSizeBeforeUpdate = droitRepository.findAll().size();

        // Update the droit
        Droit updatedDroit = droitRepository.findById(droit.getId()).get();
        // Disconnect from session so that the updates on updatedDroit are not directly saved in db
        em.detach(updatedDroit);
        updatedDroit
            .name(UPDATED_NAME)
            .isSwitch(UPDATED_IS_SWITCH);
        DroitDTO droitDTO = droitMapper.toDto(updatedDroit);

        restDroitMockMvc.perform(put("/api/droits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(droitDTO)))
            .andExpect(status().isOk());

        // Validate the Droit in the database
        List<Droit> droitList = droitRepository.findAll();
        assertThat(droitList).hasSize(databaseSizeBeforeUpdate);
        Droit testDroit = droitList.get(droitList.size() - 1);
        assertThat(testDroit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDroit.isIsSwitch()).isEqualTo(UPDATED_IS_SWITCH);

        // Validate the Droit in Elasticsearch
        verify(mockDroitSearchRepository, times(1)).save(testDroit);
    }

    @Test
    @Transactional
    public void updateNonExistingDroit() throws Exception {
        int databaseSizeBeforeUpdate = droitRepository.findAll().size();

        // Create the Droit
        DroitDTO droitDTO = droitMapper.toDto(droit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDroitMockMvc.perform(put("/api/droits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(droitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Droit in the database
        List<Droit> droitList = droitRepository.findAll();
        assertThat(droitList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Droit in Elasticsearch
        verify(mockDroitSearchRepository, times(0)).save(droit);
    }

    @Test
    @Transactional
    public void deleteDroit() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);

        int databaseSizeBeforeDelete = droitRepository.findAll().size();

        // Delete the droit
        restDroitMockMvc.perform(delete("/api/droits/{id}", droit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Droit> droitList = droitRepository.findAll();
        assertThat(droitList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Droit in Elasticsearch
        verify(mockDroitSearchRepository, times(1)).deleteById(droit.getId());
    }

    @Test
    @Transactional
    public void searchDroit() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);
        when(mockDroitSearchRepository.search(queryStringQuery("id:" + droit.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(droit), PageRequest.of(0, 1), 1));
        // Search the droit
        restDroitMockMvc.perform(get("/api/_search/droits?query=id:" + droit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(droit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isSwitch").value(hasItem(DEFAULT_IS_SWITCH.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Droit.class);
        Droit droit1 = new Droit();
        droit1.setId(1L);
        Droit droit2 = new Droit();
        droit2.setId(droit1.getId());
        assertThat(droit1).isEqualTo(droit2);
        droit2.setId(2L);
        assertThat(droit1).isNotEqualTo(droit2);
        droit1.setId(null);
        assertThat(droit1).isNotEqualTo(droit2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DroitDTO.class);
        DroitDTO droitDTO1 = new DroitDTO();
        droitDTO1.setId(1L);
        DroitDTO droitDTO2 = new DroitDTO();
        assertThat(droitDTO1).isNotEqualTo(droitDTO2);
        droitDTO2.setId(droitDTO1.getId());
        assertThat(droitDTO1).isEqualTo(droitDTO2);
        droitDTO2.setId(2L);
        assertThat(droitDTO1).isNotEqualTo(droitDTO2);
        droitDTO1.setId(null);
        assertThat(droitDTO1).isNotEqualTo(droitDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(droitMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(droitMapper.fromId(null)).isNull();
    }
}
