package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RbRecapApp;
import com.mycompany.myapp.domain.Tag;
import com.mycompany.myapp.domain.UserStory;
import com.mycompany.myapp.repository.TagRepository;
import com.mycompany.myapp.repository.search.TagSearchRepository;
import com.mycompany.myapp.service.TagService;
import com.mycompany.myapp.service.dto.TagDTO;
import com.mycompany.myapp.service.mapper.TagMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.TagCriteria;
import com.mycompany.myapp.service.TagQueryService;

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
 * Integration tests for the {@Link TagResource} REST controller.
 */
@SpringBootTest(classes = RbRecapApp.class)
public class TagResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagService tagService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.TagSearchRepositoryMockConfiguration
     */
    @Autowired
    private TagSearchRepository mockTagSearchRepository;

    @Autowired
    private TagQueryService tagQueryService;

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

    private MockMvc restTagMockMvc;

    private Tag tag;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TagResource tagResource = new TagResource(tagService, tagQueryService);
        this.restTagMockMvc = MockMvcBuilders.standaloneSetup(tagResource)
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
    public static Tag createEntity(EntityManager em) {
        Tag tag = new Tag()
            .key(DEFAULT_KEY)
            .name(DEFAULT_NAME);
        return tag;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createUpdatedEntity(EntityManager em) {
        Tag tag = new Tag()
            .key(UPDATED_KEY)
            .name(UPDATED_NAME);
        return tag;
    }

    @BeforeEach
    public void initTest() {
        tag = createEntity(em);
    }

    @Test
    @Transactional
    public void createTag() throws Exception {
        int databaseSizeBeforeCreate = tagRepository.findAll().size();

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);
        restTagMockMvc.perform(post("/api/tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isCreated());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate + 1);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testTag.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Tag in Elasticsearch
        verify(mockTagSearchRepository, times(1)).save(testTag);
    }

    @Test
    @Transactional
    public void createTagWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tagRepository.findAll().size();

        // Create the Tag with an existing ID
        tag.setId(1L);
        TagDTO tagDTO = tagMapper.toDto(tag);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagMockMvc.perform(post("/api/tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate);

        // Validate the Tag in Elasticsearch
        verify(mockTagSearchRepository, times(0)).save(tag);
    }


    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRepository.findAll().size();
        // set the field null
        tag.setKey(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.toDto(tag);

        restTagMockMvc.perform(post("/api/tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRepository.findAll().size();
        // set the field null
        tag.setName(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.toDto(tag);

        restTagMockMvc.perform(post("/api/tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTags() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList
        restTagMockMvc.perform(get("/api/tags?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get the tag
        restTagMockMvc.perform(get("/api/tags/{id}", tag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tag.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllTagsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where key equals to DEFAULT_KEY
        defaultTagShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the tagList where key equals to UPDATED_KEY
        defaultTagShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllTagsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where key in DEFAULT_KEY or UPDATED_KEY
        defaultTagShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the tagList where key equals to UPDATED_KEY
        defaultTagShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllTagsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where key is not null
        defaultTagShouldBeFound("key.specified=true");

        // Get all the tagList where key is null
        defaultTagShouldNotBeFound("key.specified=false");
    }

    @Test
    @Transactional
    public void getAllTagsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where name equals to DEFAULT_NAME
        defaultTagShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tagList where name equals to UPDATED_NAME
        defaultTagShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTagsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTagShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tagList where name equals to UPDATED_NAME
        defaultTagShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTagsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where name is not null
        defaultTagShouldBeFound("name.specified=true");

        // Get all the tagList where name is null
        defaultTagShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllTagsByUserStoryIsEqualToSomething() throws Exception {
        // Initialize the database
        UserStory userStory = UserStoryResourceIT.createEntity(em);
        em.persist(userStory);
        em.flush();
        tag.setUserStory(userStory);
        tagRepository.saveAndFlush(tag);
        Long userStoryId = userStory.getId();

        // Get all the tagList where userStory equals to userStoryId
        defaultTagShouldBeFound("userStoryId.equals=" + userStoryId);

        // Get all the tagList where userStory equals to userStoryId + 1
        defaultTagShouldNotBeFound("userStoryId.equals=" + (userStoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTagShouldBeFound(String filter) throws Exception {
        restTagMockMvc.perform(get("/api/tags?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTagMockMvc.perform(get("/api/tags/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTagShouldNotBeFound(String filter) throws Exception {
        restTagMockMvc.perform(get("/api/tags?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTagMockMvc.perform(get("/api/tags/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTag() throws Exception {
        // Get the tag
        restTagMockMvc.perform(get("/api/tags/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // Update the tag
        Tag updatedTag = tagRepository.findById(tag.getId()).get();
        // Disconnect from session so that the updates on updatedTag are not directly saved in db
        em.detach(updatedTag);
        updatedTag
            .key(UPDATED_KEY)
            .name(UPDATED_NAME);
        TagDTO tagDTO = tagMapper.toDto(updatedTag);

        restTagMockMvc.perform(put("/api/tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isOk());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testTag.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Tag in Elasticsearch
        verify(mockTagSearchRepository, times(1)).save(testTag);
    }

    @Test
    @Transactional
    public void updateNonExistingTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMockMvc.perform(put("/api/tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tag in Elasticsearch
        verify(mockTagSearchRepository, times(0)).save(tag);
    }

    @Test
    @Transactional
    public void deleteTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        int databaseSizeBeforeDelete = tagRepository.findAll().size();

        // Delete the tag
        restTagMockMvc.perform(delete("/api/tags/{id}", tag.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Tag in Elasticsearch
        verify(mockTagSearchRepository, times(1)).deleteById(tag.getId());
    }

    @Test
    @Transactional
    public void searchTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);
        when(mockTagSearchRepository.search(queryStringQuery("id:" + tag.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tag), PageRequest.of(0, 1), 1));
        // Search the tag
        restTagMockMvc.perform(get("/api/_search/tags?query=id:" + tag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tag.class);
        Tag tag1 = new Tag();
        tag1.setId(1L);
        Tag tag2 = new Tag();
        tag2.setId(tag1.getId());
        assertThat(tag1).isEqualTo(tag2);
        tag2.setId(2L);
        assertThat(tag1).isNotEqualTo(tag2);
        tag1.setId(null);
        assertThat(tag1).isNotEqualTo(tag2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagDTO.class);
        TagDTO tagDTO1 = new TagDTO();
        tagDTO1.setId(1L);
        TagDTO tagDTO2 = new TagDTO();
        assertThat(tagDTO1).isNotEqualTo(tagDTO2);
        tagDTO2.setId(tagDTO1.getId());
        assertThat(tagDTO1).isEqualTo(tagDTO2);
        tagDTO2.setId(2L);
        assertThat(tagDTO1).isNotEqualTo(tagDTO2);
        tagDTO1.setId(null);
        assertThat(tagDTO1).isNotEqualTo(tagDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tagMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tagMapper.fromId(null)).isNull();
    }
}
