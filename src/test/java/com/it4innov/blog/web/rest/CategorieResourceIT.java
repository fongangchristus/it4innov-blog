package com.it4innov.blog.web.rest;

import com.it4innov.blog.BlogApp;
import com.it4innov.blog.domain.Categorie;
import com.it4innov.blog.domain.Article;
import com.it4innov.blog.repository.CategorieRepository;
import com.it4innov.blog.service.CategorieService;
import com.it4innov.blog.service.dto.CategorieDTO;
import com.it4innov.blog.service.mapper.CategorieMapper;
import com.it4innov.blog.service.dto.CategorieCriteria;
import com.it4innov.blog.service.CategorieQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CategorieResource} REST controller.
 */
@SpringBootTest(classes = BlogApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CategorieResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private CategorieMapper categorieMapper;

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private CategorieQueryService categorieQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategorieMockMvc;

    private Categorie categorie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categorie createEntity(EntityManager em) {
        Categorie categorie = new Categorie()
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION);
        return categorie;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categorie createUpdatedEntity(EntityManager em) {
        Categorie categorie = new Categorie()
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION);
        return categorie;
    }

    @BeforeEach
    public void initTest() {
        categorie = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategorie() throws Exception {
        int databaseSizeBeforeCreate = categorieRepository.findAll().size();
        // Create the Categorie
        CategorieDTO categorieDTO = categorieMapper.toDto(categorie);
        restCategorieMockMvc.perform(post("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categorieDTO)))
            .andExpect(status().isCreated());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeCreate + 1);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testCategorie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createCategorieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = categorieRepository.findAll().size();

        // Create the Categorie with an existing ID
        categorie.setId(1L);
        CategorieDTO categorieDTO = categorieMapper.toDto(categorie);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategorieMockMvc.perform(post("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categorieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = categorieRepository.findAll().size();
        // set the field null
        categorie.setLibelle(null);

        // Create the Categorie, which fails.
        CategorieDTO categorieDTO = categorieMapper.toDto(categorie);


        restCategorieMockMvc.perform(post("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categorieDTO)))
            .andExpect(status().isBadRequest());

        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategories() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categorieList
        restCategorieMockMvc.perform(get("/api/categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getCategorie() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get the categorie
        restCategorieMockMvc.perform(get("/api/categories/{id}", categorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categorie.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        Long id = categorie.getId();

        defaultCategorieShouldBeFound("id.equals=" + id);
        defaultCategorieShouldNotBeFound("id.notEquals=" + id);

        defaultCategorieShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategorieShouldNotBeFound("id.greaterThan=" + id);

        defaultCategorieShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategorieShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCategoriesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categorieList where libelle equals to DEFAULT_LIBELLE
        defaultCategorieShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the categorieList where libelle equals to UPDATED_LIBELLE
        defaultCategorieShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByLibelleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categorieList where libelle not equals to DEFAULT_LIBELLE
        defaultCategorieShouldNotBeFound("libelle.notEquals=" + DEFAULT_LIBELLE);

        // Get all the categorieList where libelle not equals to UPDATED_LIBELLE
        defaultCategorieShouldBeFound("libelle.notEquals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categorieList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultCategorieShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the categorieList where libelle equals to UPDATED_LIBELLE
        defaultCategorieShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categorieList where libelle is not null
        defaultCategorieShouldBeFound("libelle.specified=true");

        // Get all the categorieList where libelle is null
        defaultCategorieShouldNotBeFound("libelle.specified=false");
    }
                @Test
    @Transactional
    public void getAllCategoriesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categorieList where libelle contains DEFAULT_LIBELLE
        defaultCategorieShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the categorieList where libelle contains UPDATED_LIBELLE
        defaultCategorieShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categorieList where libelle does not contain DEFAULT_LIBELLE
        defaultCategorieShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the categorieList where libelle does not contain UPDATED_LIBELLE
        defaultCategorieShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }


    @Test
    @Transactional
    public void getAllCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categorieList where description equals to DEFAULT_DESCRIPTION
        defaultCategorieShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the categorieList where description equals to UPDATED_DESCRIPTION
        defaultCategorieShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categorieList where description not equals to DEFAULT_DESCRIPTION
        defaultCategorieShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the categorieList where description not equals to UPDATED_DESCRIPTION
        defaultCategorieShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categorieList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCategorieShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the categorieList where description equals to UPDATED_DESCRIPTION
        defaultCategorieShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categorieList where description is not null
        defaultCategorieShouldBeFound("description.specified=true");

        // Get all the categorieList where description is null
        defaultCategorieShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllCategoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categorieList where description contains DEFAULT_DESCRIPTION
        defaultCategorieShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the categorieList where description contains UPDATED_DESCRIPTION
        defaultCategorieShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categorieList where description does not contain DEFAULT_DESCRIPTION
        defaultCategorieShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the categorieList where description does not contain UPDATED_DESCRIPTION
        defaultCategorieShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllCategoriesByArticleIsEqualToSomething() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);
        Article article = ArticleResourceIT.createEntity(em);
        em.persist(article);
        em.flush();
        categorie.addArticle(article);
        categorieRepository.saveAndFlush(categorie);
        Long articleId = article.getId();

        // Get all the categorieList where article equals to articleId
        defaultCategorieShouldBeFound("articleId.equals=" + articleId);

        // Get all the categorieList where article equals to articleId + 1
        defaultCategorieShouldNotBeFound("articleId.equals=" + (articleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategorieShouldBeFound(String filter) throws Exception {
        restCategorieMockMvc.perform(get("/api/categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCategorieMockMvc.perform(get("/api/categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategorieShouldNotBeFound(String filter) throws Exception {
        restCategorieMockMvc.perform(get("/api/categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategorieMockMvc.perform(get("/api/categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCategorie() throws Exception {
        // Get the categorie
        restCategorieMockMvc.perform(get("/api/categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategorie() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();

        // Update the categorie
        Categorie updatedCategorie = categorieRepository.findById(categorie.getId()).get();
        // Disconnect from session so that the updates on updatedCategorie are not directly saved in db
        em.detach(updatedCategorie);
        updatedCategorie
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION);
        CategorieDTO categorieDTO = categorieMapper.toDto(updatedCategorie);

        restCategorieMockMvc.perform(put("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categorieDTO)))
            .andExpect(status().isOk());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCategorie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();

        // Create the Categorie
        CategorieDTO categorieDTO = categorieMapper.toDto(categorie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorieMockMvc.perform(put("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categorieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCategorie() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        int databaseSizeBeforeDelete = categorieRepository.findAll().size();

        // Delete the categorie
        restCategorieMockMvc.perform(delete("/api/categories/{id}", categorie.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
