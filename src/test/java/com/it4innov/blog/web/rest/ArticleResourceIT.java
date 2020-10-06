package com.it4innov.blog.web.rest;

import com.it4innov.blog.BlogApp;
import com.it4innov.blog.domain.Article;
import com.it4innov.blog.domain.Categorie;
import com.it4innov.blog.domain.Auteur;
import com.it4innov.blog.domain.Tag;
import com.it4innov.blog.repository.ArticleRepository;
import com.it4innov.blog.service.ArticleService;
import com.it4innov.blog.service.dto.ArticleDTO;
import com.it4innov.blog.service.mapper.ArticleMapper;
import com.it4innov.blog.service.dto.ArticleCriteria;
import com.it4innov.blog.service.ArticleQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ArticleResource} REST controller.
 */
@SpringBootTest(classes = BlogApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ArticleResourceIT {

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_COUVERTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_COUVERTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_COUVERTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_COUVERTURE_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_PUBLIER = false;
    private static final Boolean UPDATED_PUBLIER = true;

    private static final String DEFAULT_DOC_MD_PATH = "AAAAAAAAAA";
    private static final String UPDATED_DOC_MD_PATH = "BBBBBBBBBB";

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleQueryService articleQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleMockMvc;

    private Article article;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Article createEntity(EntityManager em) {
        Article article = new Article()
            .libele(DEFAULT_LIBELE)
            .description(DEFAULT_DESCRIPTION)
            .couverture(DEFAULT_COUVERTURE)
            .couvertureContentType(DEFAULT_COUVERTURE_CONTENT_TYPE)
            .dateCreation(DEFAULT_DATE_CREATION)
            .publier(DEFAULT_PUBLIER)
            .docMDPath(DEFAULT_DOC_MD_PATH);
        return article;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Article createUpdatedEntity(EntityManager em) {
        Article article = new Article()
            .libele(UPDATED_LIBELE)
            .description(UPDATED_DESCRIPTION)
            .couverture(UPDATED_COUVERTURE)
            .couvertureContentType(UPDATED_COUVERTURE_CONTENT_TYPE)
            .dateCreation(UPDATED_DATE_CREATION)
            .publier(UPDATED_PUBLIER)
            .docMDPath(UPDATED_DOC_MD_PATH);
        return article;
    }

    @BeforeEach
    public void initTest() {
        article = createEntity(em);
    }

    @Test
    @Transactional
    public void createArticle() throws Exception {
        int databaseSizeBeforeCreate = articleRepository.findAll().size();
        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);
        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(articleDTO)))
            .andExpect(status().isCreated());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate + 1);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testArticle.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testArticle.getCouverture()).isEqualTo(DEFAULT_COUVERTURE);
        assertThat(testArticle.getCouvertureContentType()).isEqualTo(DEFAULT_COUVERTURE_CONTENT_TYPE);
        assertThat(testArticle.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testArticle.isPublier()).isEqualTo(DEFAULT_PUBLIER);
        assertThat(testArticle.getDocMDPath()).isEqualTo(DEFAULT_DOC_MD_PATH);
    }

    @Test
    @Transactional
    public void createArticleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = articleRepository.findAll().size();

        // Create the Article with an existing ID
        article.setId(1L);
        ArticleDTO articleDTO = articleMapper.toDto(article);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(articleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibeleIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        // set the field null
        article.setLibele(null);

        // Create the Article, which fails.
        ArticleDTO articleDTO = articleMapper.toDto(article);


        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(articleDTO)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPublierIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        // set the field null
        article.setPublier(null);

        // Create the Article, which fails.
        ArticleDTO articleDTO = articleMapper.toDto(article);


        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(articleDTO)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArticles() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList
        restArticleMockMvc.perform(get("/api/articles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].couvertureContentType").value(hasItem(DEFAULT_COUVERTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].couverture").value(hasItem(Base64Utils.encodeToString(DEFAULT_COUVERTURE))))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].publier").value(hasItem(DEFAULT_PUBLIER.booleanValue())))
            .andExpect(jsonPath("$.[*].docMDPath").value(hasItem(DEFAULT_DOC_MD_PATH)));
    }
    
    @Test
    @Transactional
    public void getArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get the article
        restArticleMockMvc.perform(get("/api/articles/{id}", article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(article.getId().intValue()))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.couvertureContentType").value(DEFAULT_COUVERTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.couverture").value(Base64Utils.encodeToString(DEFAULT_COUVERTURE)))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.publier").value(DEFAULT_PUBLIER.booleanValue()))
            .andExpect(jsonPath("$.docMDPath").value(DEFAULT_DOC_MD_PATH));
    }


    @Test
    @Transactional
    public void getArticlesByIdFiltering() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        Long id = article.getId();

        defaultArticleShouldBeFound("id.equals=" + id);
        defaultArticleShouldNotBeFound("id.notEquals=" + id);

        defaultArticleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultArticleShouldNotBeFound("id.greaterThan=" + id);

        defaultArticleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultArticleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllArticlesByLibeleIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where libele equals to DEFAULT_LIBELE
        defaultArticleShouldBeFound("libele.equals=" + DEFAULT_LIBELE);

        // Get all the articleList where libele equals to UPDATED_LIBELE
        defaultArticleShouldNotBeFound("libele.equals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllArticlesByLibeleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where libele not equals to DEFAULT_LIBELE
        defaultArticleShouldNotBeFound("libele.notEquals=" + DEFAULT_LIBELE);

        // Get all the articleList where libele not equals to UPDATED_LIBELE
        defaultArticleShouldBeFound("libele.notEquals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllArticlesByLibeleIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where libele in DEFAULT_LIBELE or UPDATED_LIBELE
        defaultArticleShouldBeFound("libele.in=" + DEFAULT_LIBELE + "," + UPDATED_LIBELE);

        // Get all the articleList where libele equals to UPDATED_LIBELE
        defaultArticleShouldNotBeFound("libele.in=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllArticlesByLibeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where libele is not null
        defaultArticleShouldBeFound("libele.specified=true");

        // Get all the articleList where libele is null
        defaultArticleShouldNotBeFound("libele.specified=false");
    }
                @Test
    @Transactional
    public void getAllArticlesByLibeleContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where libele contains DEFAULT_LIBELE
        defaultArticleShouldBeFound("libele.contains=" + DEFAULT_LIBELE);

        // Get all the articleList where libele contains UPDATED_LIBELE
        defaultArticleShouldNotBeFound("libele.contains=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllArticlesByLibeleNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where libele does not contain DEFAULT_LIBELE
        defaultArticleShouldNotBeFound("libele.doesNotContain=" + DEFAULT_LIBELE);

        // Get all the articleList where libele does not contain UPDATED_LIBELE
        defaultArticleShouldBeFound("libele.doesNotContain=" + UPDATED_LIBELE);
    }


    @Test
    @Transactional
    public void getAllArticlesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where description equals to DEFAULT_DESCRIPTION
        defaultArticleShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the articleList where description equals to UPDATED_DESCRIPTION
        defaultArticleShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllArticlesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where description not equals to DEFAULT_DESCRIPTION
        defaultArticleShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the articleList where description not equals to UPDATED_DESCRIPTION
        defaultArticleShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllArticlesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultArticleShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the articleList where description equals to UPDATED_DESCRIPTION
        defaultArticleShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllArticlesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where description is not null
        defaultArticleShouldBeFound("description.specified=true");

        // Get all the articleList where description is null
        defaultArticleShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllArticlesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where description contains DEFAULT_DESCRIPTION
        defaultArticleShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the articleList where description contains UPDATED_DESCRIPTION
        defaultArticleShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllArticlesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where description does not contain DEFAULT_DESCRIPTION
        defaultArticleShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the articleList where description does not contain UPDATED_DESCRIPTION
        defaultArticleShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllArticlesByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where dateCreation equals to DEFAULT_DATE_CREATION
        defaultArticleShouldBeFound("dateCreation.equals=" + DEFAULT_DATE_CREATION);

        // Get all the articleList where dateCreation equals to UPDATED_DATE_CREATION
        defaultArticleShouldNotBeFound("dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    public void getAllArticlesByDateCreationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where dateCreation not equals to DEFAULT_DATE_CREATION
        defaultArticleShouldNotBeFound("dateCreation.notEquals=" + DEFAULT_DATE_CREATION);

        // Get all the articleList where dateCreation not equals to UPDATED_DATE_CREATION
        defaultArticleShouldBeFound("dateCreation.notEquals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    public void getAllArticlesByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where dateCreation in DEFAULT_DATE_CREATION or UPDATED_DATE_CREATION
        defaultArticleShouldBeFound("dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION);

        // Get all the articleList where dateCreation equals to UPDATED_DATE_CREATION
        defaultArticleShouldNotBeFound("dateCreation.in=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    public void getAllArticlesByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where dateCreation is not null
        defaultArticleShouldBeFound("dateCreation.specified=true");

        // Get all the articleList where dateCreation is null
        defaultArticleShouldNotBeFound("dateCreation.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByPublierIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where publier equals to DEFAULT_PUBLIER
        defaultArticleShouldBeFound("publier.equals=" + DEFAULT_PUBLIER);

        // Get all the articleList where publier equals to UPDATED_PUBLIER
        defaultArticleShouldNotBeFound("publier.equals=" + UPDATED_PUBLIER);
    }

    @Test
    @Transactional
    public void getAllArticlesByPublierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where publier not equals to DEFAULT_PUBLIER
        defaultArticleShouldNotBeFound("publier.notEquals=" + DEFAULT_PUBLIER);

        // Get all the articleList where publier not equals to UPDATED_PUBLIER
        defaultArticleShouldBeFound("publier.notEquals=" + UPDATED_PUBLIER);
    }

    @Test
    @Transactional
    public void getAllArticlesByPublierIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where publier in DEFAULT_PUBLIER or UPDATED_PUBLIER
        defaultArticleShouldBeFound("publier.in=" + DEFAULT_PUBLIER + "," + UPDATED_PUBLIER);

        // Get all the articleList where publier equals to UPDATED_PUBLIER
        defaultArticleShouldNotBeFound("publier.in=" + UPDATED_PUBLIER);
    }

    @Test
    @Transactional
    public void getAllArticlesByPublierIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where publier is not null
        defaultArticleShouldBeFound("publier.specified=true");

        // Get all the articleList where publier is null
        defaultArticleShouldNotBeFound("publier.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByDocMDPathIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where docMDPath equals to DEFAULT_DOC_MD_PATH
        defaultArticleShouldBeFound("docMDPath.equals=" + DEFAULT_DOC_MD_PATH);

        // Get all the articleList where docMDPath equals to UPDATED_DOC_MD_PATH
        defaultArticleShouldNotBeFound("docMDPath.equals=" + UPDATED_DOC_MD_PATH);
    }

    @Test
    @Transactional
    public void getAllArticlesByDocMDPathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where docMDPath not equals to DEFAULT_DOC_MD_PATH
        defaultArticleShouldNotBeFound("docMDPath.notEquals=" + DEFAULT_DOC_MD_PATH);

        // Get all the articleList where docMDPath not equals to UPDATED_DOC_MD_PATH
        defaultArticleShouldBeFound("docMDPath.notEquals=" + UPDATED_DOC_MD_PATH);
    }

    @Test
    @Transactional
    public void getAllArticlesByDocMDPathIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where docMDPath in DEFAULT_DOC_MD_PATH or UPDATED_DOC_MD_PATH
        defaultArticleShouldBeFound("docMDPath.in=" + DEFAULT_DOC_MD_PATH + "," + UPDATED_DOC_MD_PATH);

        // Get all the articleList where docMDPath equals to UPDATED_DOC_MD_PATH
        defaultArticleShouldNotBeFound("docMDPath.in=" + UPDATED_DOC_MD_PATH);
    }

    @Test
    @Transactional
    public void getAllArticlesByDocMDPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where docMDPath is not null
        defaultArticleShouldBeFound("docMDPath.specified=true");

        // Get all the articleList where docMDPath is null
        defaultArticleShouldNotBeFound("docMDPath.specified=false");
    }
                @Test
    @Transactional
    public void getAllArticlesByDocMDPathContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where docMDPath contains DEFAULT_DOC_MD_PATH
        defaultArticleShouldBeFound("docMDPath.contains=" + DEFAULT_DOC_MD_PATH);

        // Get all the articleList where docMDPath contains UPDATED_DOC_MD_PATH
        defaultArticleShouldNotBeFound("docMDPath.contains=" + UPDATED_DOC_MD_PATH);
    }

    @Test
    @Transactional
    public void getAllArticlesByDocMDPathNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where docMDPath does not contain DEFAULT_DOC_MD_PATH
        defaultArticleShouldNotBeFound("docMDPath.doesNotContain=" + DEFAULT_DOC_MD_PATH);

        // Get all the articleList where docMDPath does not contain UPDATED_DOC_MD_PATH
        defaultArticleShouldBeFound("docMDPath.doesNotContain=" + UPDATED_DOC_MD_PATH);
    }


    @Test
    @Transactional
    public void getAllArticlesByCategorieIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);
        Categorie categorie = CategorieResourceIT.createEntity(em);
        em.persist(categorie);
        em.flush();
        article.setCategorie(categorie);
        articleRepository.saveAndFlush(article);
        Long categorieId = categorie.getId();

        // Get all the articleList where categorie equals to categorieId
        defaultArticleShouldBeFound("categorieId.equals=" + categorieId);

        // Get all the articleList where categorie equals to categorieId + 1
        defaultArticleShouldNotBeFound("categorieId.equals=" + (categorieId + 1));
    }


    @Test
    @Transactional
    public void getAllArticlesByAuteurIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);
        Auteur auteur = AuteurResourceIT.createEntity(em);
        em.persist(auteur);
        em.flush();
        article.setAuteur(auteur);
        articleRepository.saveAndFlush(article);
        Long auteurId = auteur.getId();

        // Get all the articleList where auteur equals to auteurId
        defaultArticleShouldBeFound("auteurId.equals=" + auteurId);

        // Get all the articleList where auteur equals to auteurId + 1
        defaultArticleShouldNotBeFound("auteurId.equals=" + (auteurId + 1));
    }


    @Test
    @Transactional
    public void getAllArticlesByTagIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);
        Tag tag = TagResourceIT.createEntity(em);
        em.persist(tag);
        em.flush();
        article.addTag(tag);
        articleRepository.saveAndFlush(article);
        Long tagId = tag.getId();

        // Get all the articleList where tag equals to tagId
        defaultArticleShouldBeFound("tagId.equals=" + tagId);

        // Get all the articleList where tag equals to tagId + 1
        defaultArticleShouldNotBeFound("tagId.equals=" + (tagId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArticleShouldBeFound(String filter) throws Exception {
        restArticleMockMvc.perform(get("/api/articles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].couvertureContentType").value(hasItem(DEFAULT_COUVERTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].couverture").value(hasItem(Base64Utils.encodeToString(DEFAULT_COUVERTURE))))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].publier").value(hasItem(DEFAULT_PUBLIER.booleanValue())))
            .andExpect(jsonPath("$.[*].docMDPath").value(hasItem(DEFAULT_DOC_MD_PATH)));

        // Check, that the count call also returns 1
        restArticleMockMvc.perform(get("/api/articles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArticleShouldNotBeFound(String filter) throws Exception {
        restArticleMockMvc.perform(get("/api/articles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArticleMockMvc.perform(get("/api/articles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingArticle() throws Exception {
        // Get the article
        restArticleMockMvc.perform(get("/api/articles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // Update the article
        Article updatedArticle = articleRepository.findById(article.getId()).get();
        // Disconnect from session so that the updates on updatedArticle are not directly saved in db
        em.detach(updatedArticle);
        updatedArticle
            .libele(UPDATED_LIBELE)
            .description(UPDATED_DESCRIPTION)
            .couverture(UPDATED_COUVERTURE)
            .couvertureContentType(UPDATED_COUVERTURE_CONTENT_TYPE)
            .dateCreation(UPDATED_DATE_CREATION)
            .publier(UPDATED_PUBLIER)
            .docMDPath(UPDATED_DOC_MD_PATH);
        ArticleDTO articleDTO = articleMapper.toDto(updatedArticle);

        restArticleMockMvc.perform(put("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(articleDTO)))
            .andExpect(status().isOk());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testArticle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArticle.getCouverture()).isEqualTo(UPDATED_COUVERTURE);
        assertThat(testArticle.getCouvertureContentType()).isEqualTo(UPDATED_COUVERTURE_CONTENT_TYPE);
        assertThat(testArticle.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testArticle.isPublier()).isEqualTo(UPDATED_PUBLIER);
        assertThat(testArticle.getDocMDPath()).isEqualTo(UPDATED_DOC_MD_PATH);
    }

    @Test
    @Transactional
    public void updateNonExistingArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleMockMvc.perform(put("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(articleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        int databaseSizeBeforeDelete = articleRepository.findAll().size();

        // Delete the article
        restArticleMockMvc.perform(delete("/api/articles/{id}", article.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
