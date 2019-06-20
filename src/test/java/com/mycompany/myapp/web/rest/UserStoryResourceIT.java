package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RbRecapApp;
import com.mycompany.myapp.domain.UserStory;
import com.mycompany.myapp.domain.Sprint;
import com.mycompany.myapp.domain.Tag;
import com.mycompany.myapp.domain.Droit;
import com.mycompany.myapp.repository.UserStoryRepository;
import com.mycompany.myapp.repository.search.UserStorySearchRepository;
import com.mycompany.myapp.service.UserStoryService;
import com.mycompany.myapp.service.dto.UserStoryDTO;
import com.mycompany.myapp.service.mapper.UserStoryMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.UserStoryCriteria;
import com.mycompany.myapp.service.UserStoryQueryService;

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
 * Integration tests for the {@Link UserStoryResource} REST controller.
 */
@SpringBootTest(classes = RbRecapApp.class)
public class UserStoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CHIFFRAGE = 1;
    private static final Integer UPDATED_CHIFFRAGE = 2;

    @Autowired
    private UserStoryRepository userStoryRepository;

    @Autowired
    private UserStoryMapper userStoryMapper;

    @Autowired
    private UserStoryService userStoryService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.UserStorySearchRepositoryMockConfiguration
     */
    @Autowired
    private UserStorySearchRepository mockUserStorySearchRepository;

    @Autowired
    private UserStoryQueryService userStoryQueryService;

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

    private MockMvc restUserStoryMockMvc;

    private UserStory userStory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserStoryResource userStoryResource = new UserStoryResource(userStoryService, userStoryQueryService);
        this.restUserStoryMockMvc = MockMvcBuilders.standaloneSetup(userStoryResource)
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
    public static UserStory createEntity(EntityManager em) {
        UserStory userStory = new UserStory()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .chiffrage(DEFAULT_CHIFFRAGE);
        return userStory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserStory createUpdatedEntity(EntityManager em) {
        UserStory userStory = new UserStory()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .chiffrage(UPDATED_CHIFFRAGE);
        return userStory;
    }

    @BeforeEach
    public void initTest() {
        userStory = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserStory() throws Exception {
        int databaseSizeBeforeCreate = userStoryRepository.findAll().size();

        // Create the UserStory
        UserStoryDTO userStoryDTO = userStoryMapper.toDto(userStory);
        restUserStoryMockMvc.perform(post("/api/user-stories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userStoryDTO)))
            .andExpect(status().isCreated());

        // Validate the UserStory in the database
        List<UserStory> userStoryList = userStoryRepository.findAll();
        assertThat(userStoryList).hasSize(databaseSizeBeforeCreate + 1);
        UserStory testUserStory = userStoryList.get(userStoryList.size() - 1);
        assertThat(testUserStory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserStory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUserStory.getChiffrage()).isEqualTo(DEFAULT_CHIFFRAGE);

        // Validate the UserStory in Elasticsearch
        verify(mockUserStorySearchRepository, times(1)).save(testUserStory);
    }

    @Test
    @Transactional
    public void createUserStoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userStoryRepository.findAll().size();

        // Create the UserStory with an existing ID
        userStory.setId(1L);
        UserStoryDTO userStoryDTO = userStoryMapper.toDto(userStory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserStoryMockMvc.perform(post("/api/user-stories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userStoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserStory in the database
        List<UserStory> userStoryList = userStoryRepository.findAll();
        assertThat(userStoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserStory in Elasticsearch
        verify(mockUserStorySearchRepository, times(0)).save(userStory);
    }


    @Test
    @Transactional
    public void getAllUserStories() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);

        // Get all the userStoryList
        restUserStoryMockMvc.perform(get("/api/user-stories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userStory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].chiffrage").value(hasItem(DEFAULT_CHIFFRAGE)));
    }
    
    @Test
    @Transactional
    public void getUserStory() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);

        // Get the userStory
        restUserStoryMockMvc.perform(get("/api/user-stories/{id}", userStory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userStory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.chiffrage").value(DEFAULT_CHIFFRAGE));
    }

    @Test
    @Transactional
    public void getAllUserStoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);

        // Get all the userStoryList where name equals to DEFAULT_NAME
        defaultUserStoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the userStoryList where name equals to UPDATED_NAME
        defaultUserStoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllUserStoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);

        // Get all the userStoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultUserStoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the userStoryList where name equals to UPDATED_NAME
        defaultUserStoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllUserStoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);

        // Get all the userStoryList where name is not null
        defaultUserStoryShouldBeFound("name.specified=true");

        // Get all the userStoryList where name is null
        defaultUserStoryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserStoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);

        // Get all the userStoryList where description equals to DEFAULT_DESCRIPTION
        defaultUserStoryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the userStoryList where description equals to UPDATED_DESCRIPTION
        defaultUserStoryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUserStoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);

        // Get all the userStoryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultUserStoryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the userStoryList where description equals to UPDATED_DESCRIPTION
        defaultUserStoryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUserStoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);

        // Get all the userStoryList where description is not null
        defaultUserStoryShouldBeFound("description.specified=true");

        // Get all the userStoryList where description is null
        defaultUserStoryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserStoriesByChiffrageIsEqualToSomething() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);

        // Get all the userStoryList where chiffrage equals to DEFAULT_CHIFFRAGE
        defaultUserStoryShouldBeFound("chiffrage.equals=" + DEFAULT_CHIFFRAGE);

        // Get all the userStoryList where chiffrage equals to UPDATED_CHIFFRAGE
        defaultUserStoryShouldNotBeFound("chiffrage.equals=" + UPDATED_CHIFFRAGE);
    }

    @Test
    @Transactional
    public void getAllUserStoriesByChiffrageIsInShouldWork() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);

        // Get all the userStoryList where chiffrage in DEFAULT_CHIFFRAGE or UPDATED_CHIFFRAGE
        defaultUserStoryShouldBeFound("chiffrage.in=" + DEFAULT_CHIFFRAGE + "," + UPDATED_CHIFFRAGE);

        // Get all the userStoryList where chiffrage equals to UPDATED_CHIFFRAGE
        defaultUserStoryShouldNotBeFound("chiffrage.in=" + UPDATED_CHIFFRAGE);
    }

    @Test
    @Transactional
    public void getAllUserStoriesByChiffrageIsNullOrNotNull() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);

        // Get all the userStoryList where chiffrage is not null
        defaultUserStoryShouldBeFound("chiffrage.specified=true");

        // Get all the userStoryList where chiffrage is null
        defaultUserStoryShouldNotBeFound("chiffrage.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserStoriesByChiffrageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);

        // Get all the userStoryList where chiffrage greater than or equals to DEFAULT_CHIFFRAGE
        defaultUserStoryShouldBeFound("chiffrage.greaterOrEqualThan=" + DEFAULT_CHIFFRAGE);

        // Get all the userStoryList where chiffrage greater than or equals to (DEFAULT_CHIFFRAGE + 1)
        defaultUserStoryShouldNotBeFound("chiffrage.greaterOrEqualThan=" + (DEFAULT_CHIFFRAGE + 1));
    }

    @Test
    @Transactional
    public void getAllUserStoriesByChiffrageIsLessThanSomething() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);

        // Get all the userStoryList where chiffrage less than or equals to DEFAULT_CHIFFRAGE
        defaultUserStoryShouldNotBeFound("chiffrage.lessThan=" + DEFAULT_CHIFFRAGE);

        // Get all the userStoryList where chiffrage less than or equals to (DEFAULT_CHIFFRAGE + 1)
        defaultUserStoryShouldBeFound("chiffrage.lessThan=" + (DEFAULT_CHIFFRAGE + 1));
    }


    @Test
    @Transactional
    public void getAllUserStoriesBySprintIsEqualToSomething() throws Exception {
        // Initialize the database
        Sprint sprint = SprintResourceIT.createEntity(em);
        em.persist(sprint);
        em.flush();
        userStory.setSprint(sprint);
        userStoryRepository.saveAndFlush(userStory);
        Long sprintId = sprint.getId();

        // Get all the userStoryList where sprint equals to sprintId
        defaultUserStoryShouldBeFound("sprintId.equals=" + sprintId);

        // Get all the userStoryList where sprint equals to sprintId + 1
        defaultUserStoryShouldNotBeFound("sprintId.equals=" + (sprintId + 1));
    }


    @Test
    @Transactional
    public void getAllUserStoriesByTagsIsEqualToSomething() throws Exception {
        // Initialize the database
        Tag tags = TagResourceIT.createEntity(em);
        em.persist(tags);
        em.flush();
        userStory.addTags(tags);
        userStoryRepository.saveAndFlush(userStory);
        Long tagsId = tags.getId();

        // Get all the userStoryList where tags equals to tagsId
        defaultUserStoryShouldBeFound("tagsId.equals=" + tagsId);

        // Get all the userStoryList where tags equals to tagsId + 1
        defaultUserStoryShouldNotBeFound("tagsId.equals=" + (tagsId + 1));
    }


    @Test
    @Transactional
    public void getAllUserStoriesByDroitsIsEqualToSomething() throws Exception {
        // Initialize the database
        Droit droits = DroitResourceIT.createEntity(em);
        em.persist(droits);
        em.flush();
        userStory.addDroits(droits);
        userStoryRepository.saveAndFlush(userStory);
        Long droitsId = droits.getId();

        // Get all the userStoryList where droits equals to droitsId
        defaultUserStoryShouldBeFound("droitsId.equals=" + droitsId);

        // Get all the userStoryList where droits equals to droitsId + 1
        defaultUserStoryShouldNotBeFound("droitsId.equals=" + (droitsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserStoryShouldBeFound(String filter) throws Exception {
        restUserStoryMockMvc.perform(get("/api/user-stories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userStory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].chiffrage").value(hasItem(DEFAULT_CHIFFRAGE)));

        // Check, that the count call also returns 1
        restUserStoryMockMvc.perform(get("/api/user-stories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserStoryShouldNotBeFound(String filter) throws Exception {
        restUserStoryMockMvc.perform(get("/api/user-stories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserStoryMockMvc.perform(get("/api/user-stories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUserStory() throws Exception {
        // Get the userStory
        restUserStoryMockMvc.perform(get("/api/user-stories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserStory() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);

        int databaseSizeBeforeUpdate = userStoryRepository.findAll().size();

        // Update the userStory
        UserStory updatedUserStory = userStoryRepository.findById(userStory.getId()).get();
        // Disconnect from session so that the updates on updatedUserStory are not directly saved in db
        em.detach(updatedUserStory);
        updatedUserStory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .chiffrage(UPDATED_CHIFFRAGE);
        UserStoryDTO userStoryDTO = userStoryMapper.toDto(updatedUserStory);

        restUserStoryMockMvc.perform(put("/api/user-stories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userStoryDTO)))
            .andExpect(status().isOk());

        // Validate the UserStory in the database
        List<UserStory> userStoryList = userStoryRepository.findAll();
        assertThat(userStoryList).hasSize(databaseSizeBeforeUpdate);
        UserStory testUserStory = userStoryList.get(userStoryList.size() - 1);
        assertThat(testUserStory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserStory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUserStory.getChiffrage()).isEqualTo(UPDATED_CHIFFRAGE);

        // Validate the UserStory in Elasticsearch
        verify(mockUserStorySearchRepository, times(1)).save(testUserStory);
    }

    @Test
    @Transactional
    public void updateNonExistingUserStory() throws Exception {
        int databaseSizeBeforeUpdate = userStoryRepository.findAll().size();

        // Create the UserStory
        UserStoryDTO userStoryDTO = userStoryMapper.toDto(userStory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserStoryMockMvc.perform(put("/api/user-stories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userStoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserStory in the database
        List<UserStory> userStoryList = userStoryRepository.findAll();
        assertThat(userStoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserStory in Elasticsearch
        verify(mockUserStorySearchRepository, times(0)).save(userStory);
    }

    @Test
    @Transactional
    public void deleteUserStory() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);

        int databaseSizeBeforeDelete = userStoryRepository.findAll().size();

        // Delete the userStory
        restUserStoryMockMvc.perform(delete("/api/user-stories/{id}", userStory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<UserStory> userStoryList = userStoryRepository.findAll();
        assertThat(userStoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserStory in Elasticsearch
        verify(mockUserStorySearchRepository, times(1)).deleteById(userStory.getId());
    }

    @Test
    @Transactional
    public void searchUserStory() throws Exception {
        // Initialize the database
        userStoryRepository.saveAndFlush(userStory);
        when(mockUserStorySearchRepository.search(queryStringQuery("id:" + userStory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(userStory), PageRequest.of(0, 1), 1));
        // Search the userStory
        restUserStoryMockMvc.perform(get("/api/_search/user-stories?query=id:" + userStory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userStory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].chiffrage").value(hasItem(DEFAULT_CHIFFRAGE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserStory.class);
        UserStory userStory1 = new UserStory();
        userStory1.setId(1L);
        UserStory userStory2 = new UserStory();
        userStory2.setId(userStory1.getId());
        assertThat(userStory1).isEqualTo(userStory2);
        userStory2.setId(2L);
        assertThat(userStory1).isNotEqualTo(userStory2);
        userStory1.setId(null);
        assertThat(userStory1).isNotEqualTo(userStory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserStoryDTO.class);
        UserStoryDTO userStoryDTO1 = new UserStoryDTO();
        userStoryDTO1.setId(1L);
        UserStoryDTO userStoryDTO2 = new UserStoryDTO();
        assertThat(userStoryDTO1).isNotEqualTo(userStoryDTO2);
        userStoryDTO2.setId(userStoryDTO1.getId());
        assertThat(userStoryDTO1).isEqualTo(userStoryDTO2);
        userStoryDTO2.setId(2L);
        assertThat(userStoryDTO1).isNotEqualTo(userStoryDTO2);
        userStoryDTO1.setId(null);
        assertThat(userStoryDTO1).isNotEqualTo(userStoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userStoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userStoryMapper.fromId(null)).isNull();
    }
}
