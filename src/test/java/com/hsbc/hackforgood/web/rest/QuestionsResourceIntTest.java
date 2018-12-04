package com.hsbc.hackforgood.web.rest;

import com.hsbc.hackforgood.HackforgoodApp;

import com.hsbc.hackforgood.domain.Questions;
import com.hsbc.hackforgood.repository.QuestionsRepository;
import com.hsbc.hackforgood.service.QuestionsService;
import com.hsbc.hackforgood.repository.search.QuestionsSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the QuestionsResource REST controller.
 *
 * @see QuestionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackforgoodApp.class)
public class QuestionsResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CHOICE_X = "AAAAAAAAAA";
    private static final String UPDATED_CHOICE_X = "BBBBBBBBBB";

    private static final String DEFAULT_CHOICE_Y = "AAAAAAAAAA";
    private static final String UPDATED_CHOICE_Y = "BBBBBBBBBB";

    private static final String DEFAULT_CHOICE_Z = "AAAAAAAAAA";
    private static final String UPDATED_CHOICE_Z = "BBBBBBBBBB";

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private QuestionsService questionsService;

    @Autowired
    private QuestionsSearchRepository questionsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restQuestionsMockMvc;

    private Questions questions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QuestionsResource questionsResource = new QuestionsResource(questionsService);
        this.restQuestionsMockMvc = MockMvcBuilders.standaloneSetup(questionsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questions createEntity(EntityManager em) {
        Questions questions = new Questions()
                .type(DEFAULT_TYPE)
                .name(DEFAULT_NAME)
                .title(DEFAULT_TITLE)
                .choice_x(DEFAULT_CHOICE_X)
                .choice_y(DEFAULT_CHOICE_Y)
                .choice_z(DEFAULT_CHOICE_Z);
        return questions;
    }

    @Before
    public void initTest() {
        questionsSearchRepository.deleteAll();
        questions = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestions() throws Exception {
        int databaseSizeBeforeCreate = questionsRepository.findAll().size();

        // Create the Questions

        restQuestionsMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questions)))
            .andExpect(status().isCreated());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeCreate + 1);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testQuestions.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuestions.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testQuestions.getChoice_x()).isEqualTo(DEFAULT_CHOICE_X);
        assertThat(testQuestions.getChoice_y()).isEqualTo(DEFAULT_CHOICE_Y);
        assertThat(testQuestions.getChoice_z()).isEqualTo(DEFAULT_CHOICE_Z);

        // Validate the Questions in Elasticsearch
        Questions questionsEs = questionsSearchRepository.findOne(testQuestions.getId());
        assertThat(questionsEs).isEqualToComparingFieldByField(testQuestions);
    }

    @Test
    @Transactional
    public void createQuestionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionsRepository.findAll().size();

        // Create the Questions with an existing ID
        Questions existingQuestions = new Questions();
        existingQuestions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionsMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingQuestions)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionsRepository.findAll().size();
        // set the field null
        questions.setType(null);

        // Create the Questions, which fails.

        restQuestionsMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questions)))
            .andExpect(status().isBadRequest());

        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionsRepository.findAll().size();
        // set the field null
        questions.setName(null);

        // Create the Questions, which fails.

        restQuestionsMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questions)))
            .andExpect(status().isBadRequest());

        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionsRepository.findAll().size();
        // set the field null
        questions.setTitle(null);

        // Create the Questions, which fails.

        restQuestionsMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questions)))
            .andExpect(status().isBadRequest());

        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList
        restQuestionsMockMvc.perform(get("/api/questions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questions.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].choice_x").value(hasItem(DEFAULT_CHOICE_X.toString())))
            .andExpect(jsonPath("$.[*].choice_y").value(hasItem(DEFAULT_CHOICE_Y.toString())))
            .andExpect(jsonPath("$.[*].choice_z").value(hasItem(DEFAULT_CHOICE_Z.toString())));
    }

    @Test
    @Transactional
    public void getQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get the questions
        restQuestionsMockMvc.perform(get("/api/questions/{id}", questions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(questions.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.choice_x").value(DEFAULT_CHOICE_X.toString()))
            .andExpect(jsonPath("$.choice_y").value(DEFAULT_CHOICE_Y.toString()))
            .andExpect(jsonPath("$.choice_z").value(DEFAULT_CHOICE_Z.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQuestions() throws Exception {
        // Get the questions
        restQuestionsMockMvc.perform(get("/api/questions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestions() throws Exception {
        // Initialize the database
        questionsService.save(questions);

        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();

        // Update the questions
        Questions updatedQuestions = questionsRepository.findOne(questions.getId());
        updatedQuestions
                .type(UPDATED_TYPE)
                .name(UPDATED_NAME)
                .title(UPDATED_TITLE)
                .choice_x(UPDATED_CHOICE_X)
                .choice_y(UPDATED_CHOICE_Y)
                .choice_z(UPDATED_CHOICE_Z);

        restQuestionsMockMvc.perform(put("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuestions)))
            .andExpect(status().isOk());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testQuestions.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuestions.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuestions.getChoice_x()).isEqualTo(UPDATED_CHOICE_X);
        assertThat(testQuestions.getChoice_y()).isEqualTo(UPDATED_CHOICE_Y);
        assertThat(testQuestions.getChoice_z()).isEqualTo(UPDATED_CHOICE_Z);

        // Validate the Questions in Elasticsearch
        Questions questionsEs = questionsSearchRepository.findOne(testQuestions.getId());
        assertThat(questionsEs).isEqualToComparingFieldByField(testQuestions);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();

        // Create the Questions

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQuestionsMockMvc.perform(put("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questions)))
            .andExpect(status().isCreated());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteQuestions() throws Exception {
        // Initialize the database
        questionsService.save(questions);

        int databaseSizeBeforeDelete = questionsRepository.findAll().size();

        // Get the questions
        restQuestionsMockMvc.perform(delete("/api/questions/{id}", questions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean questionsExistsInEs = questionsSearchRepository.exists(questions.getId());
        assertThat(questionsExistsInEs).isFalse();

        // Validate the database is empty
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchQuestions() throws Exception {
        // Initialize the database
        questionsService.save(questions);

        // Search the questions
        restQuestionsMockMvc.perform(get("/api/_search/questions?query=id:" + questions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questions.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].choice_x").value(hasItem(DEFAULT_CHOICE_X.toString())))
            .andExpect(jsonPath("$.[*].choice_y").value(hasItem(DEFAULT_CHOICE_Y.toString())))
            .andExpect(jsonPath("$.[*].choice_z").value(hasItem(DEFAULT_CHOICE_Z.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Questions.class);
    }
}
