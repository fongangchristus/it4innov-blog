package com.it4innov.blog.web.rest;

import com.it4innov.blog.BlogApp;
import com.it4innov.blog.domain.Tag;
import com.it4innov.blog.domain.Article;
import com.it4innov.blog.repository.TagRepository;
import com.it4innov.blog.service.TagService;
import com.it4innov.blog.service.dto.TagDTO;
import com.it4innov.blog.service.mapper.TagMapper;
import com.it4innov.blog.service.dto.TagCriteria;
import com.it4innov.blog.service.TagQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TagResource} REST controller.
 */
@SpringBootTest(classes = BlogApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TagResourceIT {

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private TagRepository tagRepository;

    @Mock
    private TagRepository tagRepositoryMock;

    @Autowired
    private TagMapper tagMapper;

    @Mock
    private TagService tagServiceMock;

    @Autowired
    private TagService tagService;

    @Autowired
    private TagQueryService tagQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTagMockMvc;

    private Tag tag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createEntity(EntityManager em) {
        Tag tag = new Tag()
            .libele(DEFAULT_LIBELE)
            .description(DEFAULT_DESCRIPTION);
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
            .libele(UPDATED_LIBELE)
            .description(UPDATED_DESCRIPTION);
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
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isCreated());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate + 1);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testTag.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
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
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibeleIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRepository.findAll().size();
        // set the field null
        tag.setLibele(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.toDto(tag);


        restTagMockMvc.perform(post("/api/tags")
            .contentType(MediaType.APPLICATION_JSON)
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllTagsWithEagerRelationshipsIsEnabled() throws Exception {
        when(tagServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTagMockMvc.perform(get("/api/tags?eagerload=true"))
            .andExpect(status().isOk());

        verify(tagServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTagsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tagServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTagMockMvc.perform(get("/api/tags?eagerload=true"))
            .andExpect(status().isOk());

        verify(tagServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get the tag
        restTagMockMvc.perform(get("/api/tags/{id}", tag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tag.getId().intValue()))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getTagsByIdFiltering() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        Long id = tag.getId();

        defaultTagShouldBeFound("id.equals=" + id);
        defaultTagShouldNotBeFound("id.notEquals=" + id);

        defaultTagShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTagShouldNotBeFound("id.greaterThan=" + id);

        defaultTagShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTagShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTagsByLibeleIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where libele equals to DEFAULT_LIBELE
        defaultTagShouldBeFound("libele.equals=" + DEFAULT_LIBELE);

        // Get all the tagList where libele equals to UPDATED_LIBELE
        defaultTagShouldNotBeFound("libele.equals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllTagsByLibeleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where libele not equals to DEFAULT_LIBELE
        defaultTagShouldNotBeFound("libele.notEquals=" + DEFAULT_LIBELE);

        // Get all the tagList where libele not equals to UPDATED_LIBELE
        defaultTagShouldBeFound("libele.notEquals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllTagsByLibeleIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where libele in DEFAULT_LIBELE or UPDATED_LIBELE
        defaultTagShouldBeFound("libele.in=" + DEFAULT_LIBELE + "," + UPDATED_LIBELE);

        // Get all the tagList where libele equals to UPDATED_LIBELE
        defaultTagShouldNotBeFound("libele.in=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllTagsByLibeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where libele is not null
        defaultTagShouldBeFound("libele.specified=true");

        // Get all the tagList where libele is null
        defaultTagShouldNotBeFound("libele.specified=false");
    }
                @Test
    @Transactional
    public void getAllTagsByLibeleContainsSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where libele contains DEFAULT_LIBELE
        defaultTagShouldBeFound("libele.contains=" + DEFAULT_LIBELE);

        // Get all the tagList where libele contains UPDATED_LIBELE
        defaultTagShouldNotBeFound("libele.contains=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllTagsByLibeleNotContainsSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where libele does not contain DEFAULT_LIBELE
        defaultTagShouldNotBeFound("libele.doesNotContain=" + DEFAULT_LIBELE);

        // Get all the tagList where libele does not contain UPDATED_LIBELE
        defaultTagShouldBeFound("libele.doesNotContain=" + UPDATED_LIBELE);
    }


    @Test
    @Transactional
    public void getAllTagsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where description equals to DEFAULT_DESCRIPTION
        defaultTagShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the tagList where description equals to UPDATED_DESCRIPTION
        defaultTagShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTagsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where description not equals to DEFAULT_DESCRIPTION
        defaultTagShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the tagList where description not equals to UPDATED_DESCRIPTION
        defaultTagShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTagsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTagShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the tagList where description equals to UPDATED_DESCRIPTION
        defaultTagShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTagsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where description is not null
        defaultTagShouldBeFound("description.specified=true");

        // Get all the tagList where description is null
        defaultTagShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllTagsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where description contains DEFAULT_DESCRIPTION
        defaultTagShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the tagList where description contains UPDATED_DESCRIPTION
        defaultTagShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTagsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where description does not contain DEFAULT_DESCRIPTION
        defaultTagShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the tagList where description does not contain UPDATED_DESCRIPTION
        defaultTagShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllTagsByArticleIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);
        Article article = ArticleResourceIT.createEntity(em);
        em.persist(article);
        em.flush();
        tag.addArticle(article);
        tagRepository.saveAndFlush(tag);
        Long articleId = article.getId();

        // Get all the tagList where article equals to articleId
        defaultTagShouldBeFound("articleId.equals=" + articleId);

        // Get all the tagList where article equals to articleId + 1
        defaultTagShouldNotBeFound("articleId.equals=" + (articleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTagShouldBeFound(String filter) throws Exception {
        restTagMockMvc.perform(get("/api/tags?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restTagMockMvc.perform(get("/api/tags/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTagShouldNotBeFound(String filter) throws Exception {
        restTagMockMvc.perform(get("/api/tags?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTagMockMvc.perform(get("/api/tags/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
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
            .libele(UPDATED_LIBELE)
            .description(UPDATED_DESCRIPTION);
        TagDTO tagDTO = tagMapper.toDto(updatedTag);

        restTagMockMvc.perform(put("/api/tags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isOk());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testTag.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMockMvc.perform(put("/api/tags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        int databaseSizeBeforeDelete = tagRepository.findAll().size();

        // Delete the tag
        restTagMockMvc.perform(delete("/api/tags/{id}", tag.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
