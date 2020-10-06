package com.it4innov.blog.web.rest;

import com.it4innov.blog.BlogApp;
import com.it4innov.blog.domain.Auteur;
import com.it4innov.blog.domain.Article;
import com.it4innov.blog.repository.AuteurRepository;
import com.it4innov.blog.service.AuteurService;
import com.it4innov.blog.service.dto.AuteurDTO;
import com.it4innov.blog.service.mapper.AuteurMapper;
import com.it4innov.blog.service.dto.AuteurCriteria;
import com.it4innov.blog.service.AuteurQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AuteurResource} REST controller.
 */
@SpringBootTest(classes = BlogApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AuteurResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PROFILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROFILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROFILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROFILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_TWEETER = "AAAAAAAAAA";
    private static final String UPDATED_TWEETER = "BBBBBBBBBB";

    private static final String DEFAULT_LINKEDIN = "AAAAAAAAAA";
    private static final String UPDATED_LINKEDIN = "BBBBBBBBBB";

    private static final String DEFAULT_FACEBOOK = "AAAAAAAAAA";
    private static final String UPDATED_FACEBOOK = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SLOGAN = "AAAAAAAAAA";
    private static final String UPDATED_SLOGAN = "BBBBBBBBBB";

    @Autowired
    private AuteurRepository auteurRepository;

    @Autowired
    private AuteurMapper auteurMapper;

    @Autowired
    private AuteurService auteurService;

    @Autowired
    private AuteurQueryService auteurQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuteurMockMvc;

    private Auteur auteur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Auteur createEntity(EntityManager em) {
        Auteur auteur = new Auteur()
            .login(DEFAULT_LOGIN)
            .firstname(DEFAULT_FIRSTNAME)
            .password(DEFAULT_PASSWORD)
            .profile(DEFAULT_PROFILE)
            .profileContentType(DEFAULT_PROFILE_CONTENT_TYPE)
            .tweeter(DEFAULT_TWEETER)
            .linkedin(DEFAULT_LINKEDIN)
            .facebook(DEFAULT_FACEBOOK)
            .description(DEFAULT_DESCRIPTION)
            .slogan(DEFAULT_SLOGAN);
        return auteur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Auteur createUpdatedEntity(EntityManager em) {
        Auteur auteur = new Auteur()
            .login(UPDATED_LOGIN)
            .firstname(UPDATED_FIRSTNAME)
            .password(UPDATED_PASSWORD)
            .profile(UPDATED_PROFILE)
            .profileContentType(UPDATED_PROFILE_CONTENT_TYPE)
            .tweeter(UPDATED_TWEETER)
            .linkedin(UPDATED_LINKEDIN)
            .facebook(UPDATED_FACEBOOK)
            .description(UPDATED_DESCRIPTION)
            .slogan(UPDATED_SLOGAN);
        return auteur;
    }

    @BeforeEach
    public void initTest() {
        auteur = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuteur() throws Exception {
        int databaseSizeBeforeCreate = auteurRepository.findAll().size();
        // Create the Auteur
        AuteurDTO auteurDTO = auteurMapper.toDto(auteur);
        restAuteurMockMvc.perform(post("/api/auteurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auteurDTO)))
            .andExpect(status().isCreated());

        // Validate the Auteur in the database
        List<Auteur> auteurList = auteurRepository.findAll();
        assertThat(auteurList).hasSize(databaseSizeBeforeCreate + 1);
        Auteur testAuteur = auteurList.get(auteurList.size() - 1);
        assertThat(testAuteur.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testAuteur.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testAuteur.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testAuteur.getProfile()).isEqualTo(DEFAULT_PROFILE);
        assertThat(testAuteur.getProfileContentType()).isEqualTo(DEFAULT_PROFILE_CONTENT_TYPE);
        assertThat(testAuteur.getTweeter()).isEqualTo(DEFAULT_TWEETER);
        assertThat(testAuteur.getLinkedin()).isEqualTo(DEFAULT_LINKEDIN);
        assertThat(testAuteur.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testAuteur.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAuteur.getSlogan()).isEqualTo(DEFAULT_SLOGAN);
    }

    @Test
    @Transactional
    public void createAuteurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = auteurRepository.findAll().size();

        // Create the Auteur with an existing ID
        auteur.setId(1L);
        AuteurDTO auteurDTO = auteurMapper.toDto(auteur);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuteurMockMvc.perform(post("/api/auteurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auteurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Auteur in the database
        List<Auteur> auteurList = auteurRepository.findAll();
        assertThat(auteurList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = auteurRepository.findAll().size();
        // set the field null
        auteur.setLogin(null);

        // Create the Auteur, which fails.
        AuteurDTO auteurDTO = auteurMapper.toDto(auteur);


        restAuteurMockMvc.perform(post("/api/auteurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auteurDTO)))
            .andExpect(status().isBadRequest());

        List<Auteur> auteurList = auteurRepository.findAll();
        assertThat(auteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = auteurRepository.findAll().size();
        // set the field null
        auteur.setPassword(null);

        // Create the Auteur, which fails.
        AuteurDTO auteurDTO = auteurMapper.toDto(auteur);


        restAuteurMockMvc.perform(post("/api/auteurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auteurDTO)))
            .andExpect(status().isBadRequest());

        List<Auteur> auteurList = auteurRepository.findAll();
        assertThat(auteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuteurs() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList
        restAuteurMockMvc.perform(get("/api/auteurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].profileContentType").value(hasItem(DEFAULT_PROFILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profile").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROFILE))))
            .andExpect(jsonPath("$.[*].tweeter").value(hasItem(DEFAULT_TWEETER)))
            .andExpect(jsonPath("$.[*].linkedin").value(hasItem(DEFAULT_LINKEDIN)))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].slogan").value(hasItem(DEFAULT_SLOGAN)));
    }
    
    @Test
    @Transactional
    public void getAuteur() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get the auteur
        restAuteurMockMvc.perform(get("/api/auteurs/{id}", auteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auteur.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.profileContentType").value(DEFAULT_PROFILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.profile").value(Base64Utils.encodeToString(DEFAULT_PROFILE)))
            .andExpect(jsonPath("$.tweeter").value(DEFAULT_TWEETER))
            .andExpect(jsonPath("$.linkedin").value(DEFAULT_LINKEDIN))
            .andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.slogan").value(DEFAULT_SLOGAN));
    }


    @Test
    @Transactional
    public void getAuteursByIdFiltering() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        Long id = auteur.getId();

        defaultAuteurShouldBeFound("id.equals=" + id);
        defaultAuteurShouldNotBeFound("id.notEquals=" + id);

        defaultAuteurShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAuteurShouldNotBeFound("id.greaterThan=" + id);

        defaultAuteurShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAuteurShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAuteursByLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where login equals to DEFAULT_LOGIN
        defaultAuteurShouldBeFound("login.equals=" + DEFAULT_LOGIN);

        // Get all the auteurList where login equals to UPDATED_LOGIN
        defaultAuteurShouldNotBeFound("login.equals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllAuteursByLoginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where login not equals to DEFAULT_LOGIN
        defaultAuteurShouldNotBeFound("login.notEquals=" + DEFAULT_LOGIN);

        // Get all the auteurList where login not equals to UPDATED_LOGIN
        defaultAuteurShouldBeFound("login.notEquals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllAuteursByLoginIsInShouldWork() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where login in DEFAULT_LOGIN or UPDATED_LOGIN
        defaultAuteurShouldBeFound("login.in=" + DEFAULT_LOGIN + "," + UPDATED_LOGIN);

        // Get all the auteurList where login equals to UPDATED_LOGIN
        defaultAuteurShouldNotBeFound("login.in=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllAuteursByLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where login is not null
        defaultAuteurShouldBeFound("login.specified=true");

        // Get all the auteurList where login is null
        defaultAuteurShouldNotBeFound("login.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuteursByLoginContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where login contains DEFAULT_LOGIN
        defaultAuteurShouldBeFound("login.contains=" + DEFAULT_LOGIN);

        // Get all the auteurList where login contains UPDATED_LOGIN
        defaultAuteurShouldNotBeFound("login.contains=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllAuteursByLoginNotContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where login does not contain DEFAULT_LOGIN
        defaultAuteurShouldNotBeFound("login.doesNotContain=" + DEFAULT_LOGIN);

        // Get all the auteurList where login does not contain UPDATED_LOGIN
        defaultAuteurShouldBeFound("login.doesNotContain=" + UPDATED_LOGIN);
    }


    @Test
    @Transactional
    public void getAllAuteursByFirstnameIsEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where firstname equals to DEFAULT_FIRSTNAME
        defaultAuteurShouldBeFound("firstname.equals=" + DEFAULT_FIRSTNAME);

        // Get all the auteurList where firstname equals to UPDATED_FIRSTNAME
        defaultAuteurShouldNotBeFound("firstname.equals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllAuteursByFirstnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where firstname not equals to DEFAULT_FIRSTNAME
        defaultAuteurShouldNotBeFound("firstname.notEquals=" + DEFAULT_FIRSTNAME);

        // Get all the auteurList where firstname not equals to UPDATED_FIRSTNAME
        defaultAuteurShouldBeFound("firstname.notEquals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllAuteursByFirstnameIsInShouldWork() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where firstname in DEFAULT_FIRSTNAME or UPDATED_FIRSTNAME
        defaultAuteurShouldBeFound("firstname.in=" + DEFAULT_FIRSTNAME + "," + UPDATED_FIRSTNAME);

        // Get all the auteurList where firstname equals to UPDATED_FIRSTNAME
        defaultAuteurShouldNotBeFound("firstname.in=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllAuteursByFirstnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where firstname is not null
        defaultAuteurShouldBeFound("firstname.specified=true");

        // Get all the auteurList where firstname is null
        defaultAuteurShouldNotBeFound("firstname.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuteursByFirstnameContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where firstname contains DEFAULT_FIRSTNAME
        defaultAuteurShouldBeFound("firstname.contains=" + DEFAULT_FIRSTNAME);

        // Get all the auteurList where firstname contains UPDATED_FIRSTNAME
        defaultAuteurShouldNotBeFound("firstname.contains=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllAuteursByFirstnameNotContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where firstname does not contain DEFAULT_FIRSTNAME
        defaultAuteurShouldNotBeFound("firstname.doesNotContain=" + DEFAULT_FIRSTNAME);

        // Get all the auteurList where firstname does not contain UPDATED_FIRSTNAME
        defaultAuteurShouldBeFound("firstname.doesNotContain=" + UPDATED_FIRSTNAME);
    }


    @Test
    @Transactional
    public void getAllAuteursByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where password equals to DEFAULT_PASSWORD
        defaultAuteurShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the auteurList where password equals to UPDATED_PASSWORD
        defaultAuteurShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllAuteursByPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where password not equals to DEFAULT_PASSWORD
        defaultAuteurShouldNotBeFound("password.notEquals=" + DEFAULT_PASSWORD);

        // Get all the auteurList where password not equals to UPDATED_PASSWORD
        defaultAuteurShouldBeFound("password.notEquals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllAuteursByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultAuteurShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the auteurList where password equals to UPDATED_PASSWORD
        defaultAuteurShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllAuteursByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where password is not null
        defaultAuteurShouldBeFound("password.specified=true");

        // Get all the auteurList where password is null
        defaultAuteurShouldNotBeFound("password.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuteursByPasswordContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where password contains DEFAULT_PASSWORD
        defaultAuteurShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the auteurList where password contains UPDATED_PASSWORD
        defaultAuteurShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllAuteursByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where password does not contain DEFAULT_PASSWORD
        defaultAuteurShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the auteurList where password does not contain UPDATED_PASSWORD
        defaultAuteurShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }


    @Test
    @Transactional
    public void getAllAuteursByTweeterIsEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where tweeter equals to DEFAULT_TWEETER
        defaultAuteurShouldBeFound("tweeter.equals=" + DEFAULT_TWEETER);

        // Get all the auteurList where tweeter equals to UPDATED_TWEETER
        defaultAuteurShouldNotBeFound("tweeter.equals=" + UPDATED_TWEETER);
    }

    @Test
    @Transactional
    public void getAllAuteursByTweeterIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where tweeter not equals to DEFAULT_TWEETER
        defaultAuteurShouldNotBeFound("tweeter.notEquals=" + DEFAULT_TWEETER);

        // Get all the auteurList where tweeter not equals to UPDATED_TWEETER
        defaultAuteurShouldBeFound("tweeter.notEquals=" + UPDATED_TWEETER);
    }

    @Test
    @Transactional
    public void getAllAuteursByTweeterIsInShouldWork() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where tweeter in DEFAULT_TWEETER or UPDATED_TWEETER
        defaultAuteurShouldBeFound("tweeter.in=" + DEFAULT_TWEETER + "," + UPDATED_TWEETER);

        // Get all the auteurList where tweeter equals to UPDATED_TWEETER
        defaultAuteurShouldNotBeFound("tweeter.in=" + UPDATED_TWEETER);
    }

    @Test
    @Transactional
    public void getAllAuteursByTweeterIsNullOrNotNull() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where tweeter is not null
        defaultAuteurShouldBeFound("tweeter.specified=true");

        // Get all the auteurList where tweeter is null
        defaultAuteurShouldNotBeFound("tweeter.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuteursByTweeterContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where tweeter contains DEFAULT_TWEETER
        defaultAuteurShouldBeFound("tweeter.contains=" + DEFAULT_TWEETER);

        // Get all the auteurList where tweeter contains UPDATED_TWEETER
        defaultAuteurShouldNotBeFound("tweeter.contains=" + UPDATED_TWEETER);
    }

    @Test
    @Transactional
    public void getAllAuteursByTweeterNotContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where tweeter does not contain DEFAULT_TWEETER
        defaultAuteurShouldNotBeFound("tweeter.doesNotContain=" + DEFAULT_TWEETER);

        // Get all the auteurList where tweeter does not contain UPDATED_TWEETER
        defaultAuteurShouldBeFound("tweeter.doesNotContain=" + UPDATED_TWEETER);
    }


    @Test
    @Transactional
    public void getAllAuteursByLinkedinIsEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where linkedin equals to DEFAULT_LINKEDIN
        defaultAuteurShouldBeFound("linkedin.equals=" + DEFAULT_LINKEDIN);

        // Get all the auteurList where linkedin equals to UPDATED_LINKEDIN
        defaultAuteurShouldNotBeFound("linkedin.equals=" + UPDATED_LINKEDIN);
    }

    @Test
    @Transactional
    public void getAllAuteursByLinkedinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where linkedin not equals to DEFAULT_LINKEDIN
        defaultAuteurShouldNotBeFound("linkedin.notEquals=" + DEFAULT_LINKEDIN);

        // Get all the auteurList where linkedin not equals to UPDATED_LINKEDIN
        defaultAuteurShouldBeFound("linkedin.notEquals=" + UPDATED_LINKEDIN);
    }

    @Test
    @Transactional
    public void getAllAuteursByLinkedinIsInShouldWork() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where linkedin in DEFAULT_LINKEDIN or UPDATED_LINKEDIN
        defaultAuteurShouldBeFound("linkedin.in=" + DEFAULT_LINKEDIN + "," + UPDATED_LINKEDIN);

        // Get all the auteurList where linkedin equals to UPDATED_LINKEDIN
        defaultAuteurShouldNotBeFound("linkedin.in=" + UPDATED_LINKEDIN);
    }

    @Test
    @Transactional
    public void getAllAuteursByLinkedinIsNullOrNotNull() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where linkedin is not null
        defaultAuteurShouldBeFound("linkedin.specified=true");

        // Get all the auteurList where linkedin is null
        defaultAuteurShouldNotBeFound("linkedin.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuteursByLinkedinContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where linkedin contains DEFAULT_LINKEDIN
        defaultAuteurShouldBeFound("linkedin.contains=" + DEFAULT_LINKEDIN);

        // Get all the auteurList where linkedin contains UPDATED_LINKEDIN
        defaultAuteurShouldNotBeFound("linkedin.contains=" + UPDATED_LINKEDIN);
    }

    @Test
    @Transactional
    public void getAllAuteursByLinkedinNotContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where linkedin does not contain DEFAULT_LINKEDIN
        defaultAuteurShouldNotBeFound("linkedin.doesNotContain=" + DEFAULT_LINKEDIN);

        // Get all the auteurList where linkedin does not contain UPDATED_LINKEDIN
        defaultAuteurShouldBeFound("linkedin.doesNotContain=" + UPDATED_LINKEDIN);
    }


    @Test
    @Transactional
    public void getAllAuteursByFacebookIsEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where facebook equals to DEFAULT_FACEBOOK
        defaultAuteurShouldBeFound("facebook.equals=" + DEFAULT_FACEBOOK);

        // Get all the auteurList where facebook equals to UPDATED_FACEBOOK
        defaultAuteurShouldNotBeFound("facebook.equals=" + UPDATED_FACEBOOK);
    }

    @Test
    @Transactional
    public void getAllAuteursByFacebookIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where facebook not equals to DEFAULT_FACEBOOK
        defaultAuteurShouldNotBeFound("facebook.notEquals=" + DEFAULT_FACEBOOK);

        // Get all the auteurList where facebook not equals to UPDATED_FACEBOOK
        defaultAuteurShouldBeFound("facebook.notEquals=" + UPDATED_FACEBOOK);
    }

    @Test
    @Transactional
    public void getAllAuteursByFacebookIsInShouldWork() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where facebook in DEFAULT_FACEBOOK or UPDATED_FACEBOOK
        defaultAuteurShouldBeFound("facebook.in=" + DEFAULT_FACEBOOK + "," + UPDATED_FACEBOOK);

        // Get all the auteurList where facebook equals to UPDATED_FACEBOOK
        defaultAuteurShouldNotBeFound("facebook.in=" + UPDATED_FACEBOOK);
    }

    @Test
    @Transactional
    public void getAllAuteursByFacebookIsNullOrNotNull() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where facebook is not null
        defaultAuteurShouldBeFound("facebook.specified=true");

        // Get all the auteurList where facebook is null
        defaultAuteurShouldNotBeFound("facebook.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuteursByFacebookContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where facebook contains DEFAULT_FACEBOOK
        defaultAuteurShouldBeFound("facebook.contains=" + DEFAULT_FACEBOOK);

        // Get all the auteurList where facebook contains UPDATED_FACEBOOK
        defaultAuteurShouldNotBeFound("facebook.contains=" + UPDATED_FACEBOOK);
    }

    @Test
    @Transactional
    public void getAllAuteursByFacebookNotContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where facebook does not contain DEFAULT_FACEBOOK
        defaultAuteurShouldNotBeFound("facebook.doesNotContain=" + DEFAULT_FACEBOOK);

        // Get all the auteurList where facebook does not contain UPDATED_FACEBOOK
        defaultAuteurShouldBeFound("facebook.doesNotContain=" + UPDATED_FACEBOOK);
    }


    @Test
    @Transactional
    public void getAllAuteursByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where description equals to DEFAULT_DESCRIPTION
        defaultAuteurShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the auteurList where description equals to UPDATED_DESCRIPTION
        defaultAuteurShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAuteursByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where description not equals to DEFAULT_DESCRIPTION
        defaultAuteurShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the auteurList where description not equals to UPDATED_DESCRIPTION
        defaultAuteurShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAuteursByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAuteurShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the auteurList where description equals to UPDATED_DESCRIPTION
        defaultAuteurShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAuteursByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where description is not null
        defaultAuteurShouldBeFound("description.specified=true");

        // Get all the auteurList where description is null
        defaultAuteurShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuteursByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where description contains DEFAULT_DESCRIPTION
        defaultAuteurShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the auteurList where description contains UPDATED_DESCRIPTION
        defaultAuteurShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAuteursByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where description does not contain DEFAULT_DESCRIPTION
        defaultAuteurShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the auteurList where description does not contain UPDATED_DESCRIPTION
        defaultAuteurShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllAuteursBySloganIsEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where slogan equals to DEFAULT_SLOGAN
        defaultAuteurShouldBeFound("slogan.equals=" + DEFAULT_SLOGAN);

        // Get all the auteurList where slogan equals to UPDATED_SLOGAN
        defaultAuteurShouldNotBeFound("slogan.equals=" + UPDATED_SLOGAN);
    }

    @Test
    @Transactional
    public void getAllAuteursBySloganIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where slogan not equals to DEFAULT_SLOGAN
        defaultAuteurShouldNotBeFound("slogan.notEquals=" + DEFAULT_SLOGAN);

        // Get all the auteurList where slogan not equals to UPDATED_SLOGAN
        defaultAuteurShouldBeFound("slogan.notEquals=" + UPDATED_SLOGAN);
    }

    @Test
    @Transactional
    public void getAllAuteursBySloganIsInShouldWork() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where slogan in DEFAULT_SLOGAN or UPDATED_SLOGAN
        defaultAuteurShouldBeFound("slogan.in=" + DEFAULT_SLOGAN + "," + UPDATED_SLOGAN);

        // Get all the auteurList where slogan equals to UPDATED_SLOGAN
        defaultAuteurShouldNotBeFound("slogan.in=" + UPDATED_SLOGAN);
    }

    @Test
    @Transactional
    public void getAllAuteursBySloganIsNullOrNotNull() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where slogan is not null
        defaultAuteurShouldBeFound("slogan.specified=true");

        // Get all the auteurList where slogan is null
        defaultAuteurShouldNotBeFound("slogan.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuteursBySloganContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where slogan contains DEFAULT_SLOGAN
        defaultAuteurShouldBeFound("slogan.contains=" + DEFAULT_SLOGAN);

        // Get all the auteurList where slogan contains UPDATED_SLOGAN
        defaultAuteurShouldNotBeFound("slogan.contains=" + UPDATED_SLOGAN);
    }

    @Test
    @Transactional
    public void getAllAuteursBySloganNotContainsSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurList where slogan does not contain DEFAULT_SLOGAN
        defaultAuteurShouldNotBeFound("slogan.doesNotContain=" + DEFAULT_SLOGAN);

        // Get all the auteurList where slogan does not contain UPDATED_SLOGAN
        defaultAuteurShouldBeFound("slogan.doesNotContain=" + UPDATED_SLOGAN);
    }


    @Test
    @Transactional
    public void getAllAuteursByArticleIsEqualToSomething() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);
        Article article = ArticleResourceIT.createEntity(em);
        em.persist(article);
        em.flush();
        auteur.addArticle(article);
        auteurRepository.saveAndFlush(auteur);
        Long articleId = article.getId();

        // Get all the auteurList where article equals to articleId
        defaultAuteurShouldBeFound("articleId.equals=" + articleId);

        // Get all the auteurList where article equals to articleId + 1
        defaultAuteurShouldNotBeFound("articleId.equals=" + (articleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuteurShouldBeFound(String filter) throws Exception {
        restAuteurMockMvc.perform(get("/api/auteurs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].profileContentType").value(hasItem(DEFAULT_PROFILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profile").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROFILE))))
            .andExpect(jsonPath("$.[*].tweeter").value(hasItem(DEFAULT_TWEETER)))
            .andExpect(jsonPath("$.[*].linkedin").value(hasItem(DEFAULT_LINKEDIN)))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].slogan").value(hasItem(DEFAULT_SLOGAN)));

        // Check, that the count call also returns 1
        restAuteurMockMvc.perform(get("/api/auteurs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuteurShouldNotBeFound(String filter) throws Exception {
        restAuteurMockMvc.perform(get("/api/auteurs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuteurMockMvc.perform(get("/api/auteurs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAuteur() throws Exception {
        // Get the auteur
        restAuteurMockMvc.perform(get("/api/auteurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuteur() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        int databaseSizeBeforeUpdate = auteurRepository.findAll().size();

        // Update the auteur
        Auteur updatedAuteur = auteurRepository.findById(auteur.getId()).get();
        // Disconnect from session so that the updates on updatedAuteur are not directly saved in db
        em.detach(updatedAuteur);
        updatedAuteur
            .login(UPDATED_LOGIN)
            .firstname(UPDATED_FIRSTNAME)
            .password(UPDATED_PASSWORD)
            .profile(UPDATED_PROFILE)
            .profileContentType(UPDATED_PROFILE_CONTENT_TYPE)
            .tweeter(UPDATED_TWEETER)
            .linkedin(UPDATED_LINKEDIN)
            .facebook(UPDATED_FACEBOOK)
            .description(UPDATED_DESCRIPTION)
            .slogan(UPDATED_SLOGAN);
        AuteurDTO auteurDTO = auteurMapper.toDto(updatedAuteur);

        restAuteurMockMvc.perform(put("/api/auteurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auteurDTO)))
            .andExpect(status().isOk());

        // Validate the Auteur in the database
        List<Auteur> auteurList = auteurRepository.findAll();
        assertThat(auteurList).hasSize(databaseSizeBeforeUpdate);
        Auteur testAuteur = auteurList.get(auteurList.size() - 1);
        assertThat(testAuteur.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testAuteur.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testAuteur.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testAuteur.getProfile()).isEqualTo(UPDATED_PROFILE);
        assertThat(testAuteur.getProfileContentType()).isEqualTo(UPDATED_PROFILE_CONTENT_TYPE);
        assertThat(testAuteur.getTweeter()).isEqualTo(UPDATED_TWEETER);
        assertThat(testAuteur.getLinkedin()).isEqualTo(UPDATED_LINKEDIN);
        assertThat(testAuteur.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testAuteur.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAuteur.getSlogan()).isEqualTo(UPDATED_SLOGAN);
    }

    @Test
    @Transactional
    public void updateNonExistingAuteur() throws Exception {
        int databaseSizeBeforeUpdate = auteurRepository.findAll().size();

        // Create the Auteur
        AuteurDTO auteurDTO = auteurMapper.toDto(auteur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuteurMockMvc.perform(put("/api/auteurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auteurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Auteur in the database
        List<Auteur> auteurList = auteurRepository.findAll();
        assertThat(auteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuteur() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        int databaseSizeBeforeDelete = auteurRepository.findAll().size();

        // Delete the auteur
        restAuteurMockMvc.perform(delete("/api/auteurs/{id}", auteur.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Auteur> auteurList = auteurRepository.findAll();
        assertThat(auteurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
