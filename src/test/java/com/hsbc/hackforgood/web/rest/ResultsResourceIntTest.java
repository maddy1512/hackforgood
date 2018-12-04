package com.hsbc.hackforgood.web.rest;

import com.hsbc.hackforgood.HackforgoodApp;

import com.hsbc.hackforgood.domain.Results;
import com.hsbc.hackforgood.repository.ResultsRepository;
import com.hsbc.hackforgood.service.ResultsService;
import com.hsbc.hackforgood.repository.search.ResultsSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ResultsResource REST controller.
 *
 * @see ResultsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackforgoodApp.class)
public class ResultsResourceIntTest {

    private static final Long DEFAULT_USER = 1L;
    private static final Long UPDATED_USER = 2L;

    private static final Long DEFAULT_SURVEY_ID = 1L;
    private static final Long UPDATED_SURVEY_ID = 2L;

    private static final Long DEFAULT_QUESTION_ID = 1L;
    private static final Long UPDATED_QUESTION_ID = 2L;

    private static final String DEFAULT_QUESTION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_RESULT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SURVEY_TIMESTAMP = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SURVEY_TIMESTAMP = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ResultsRepository resultsRepository;

    @Autowired
    private ResultsService resultsService;

    @Autowired
    private ResultsSearchRepository resultsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restResultsMockMvc;

    private Results results;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResultsResource resultsResource = new ResultsResource(resultsService);
        this.restResultsMockMvc = MockMvcBuilders.standaloneSetup(resultsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Results createEntity(EntityManager em) {
        Results results = new Results()
                .user(DEFAULT_USER)
                .survey_id(DEFAULT_SURVEY_ID)
                .question_id(DEFAULT_QUESTION_ID)
                .question_name(DEFAULT_QUESTION_NAME)
                .question_result(DEFAULT_QUESTION_RESULT)
                .survey_timestamp(DEFAULT_SURVEY_TIMESTAMP);
        return results;
    }

    @Before
    public void initTest() {
        resultsSearchRepository.deleteAll();
        results = createEntity(em);
    }

    @Test
    @Transactional
    public void createResults() throws Exception {
        int databaseSizeBeforeCreate = resultsRepository.findAll().size();

        // Create the Results

        restResultsMockMvc.perform(post("/api/results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(results)))
            .andExpect(status().isCreated());

        // Validate the Results in the database
        List<Results> resultsList = resultsRepository.findAll();
        assertThat(resultsList).hasSize(databaseSizeBeforeCreate + 1);
        Results testResults = resultsList.get(resultsList.size() - 1);
        assertThat(testResults.getUser()).isEqualTo(DEFAULT_USER);
        assertThat(testResults.getSurvey_id()).isEqualTo(DEFAULT_SURVEY_ID);
        assertThat(testResults.getQuestion_id()).isEqualTo(DEFAULT_QUESTION_ID);
        assertThat(testResults.getQuestion_name()).isEqualTo(DEFAULT_QUESTION_NAME);
        assertThat(testResults.getQuestion_result()).isEqualTo(DEFAULT_QUESTION_RESULT);
        assertThat(testResults.getSurvey_timestamp()).isEqualTo(DEFAULT_SURVEY_TIMESTAMP);

        // Validate the Results in Elasticsearch
        Results resultsEs = resultsSearchRepository.findOne(testResults.getId());
        assertThat(resultsEs).isEqualToComparingFieldByField(testResults);
    }

    @Test
    @Transactional
    public void createResultsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resultsRepository.findAll().size();

        // Create the Results with an existing ID
        Results existingResults = new Results();
        existingResults.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResultsMockMvc.perform(post("/api/results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingResults)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Results> resultsList = resultsRepository.findAll();
        assertThat(resultsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserIsRequired() throws Exception {
        int databaseSizeBeforeTest = resultsRepository.findAll().size();
        // set the field null
        results.setUser(null);

        // Create the Results, which fails.

        restResultsMockMvc.perform(post("/api/results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(results)))
            .andExpect(status().isBadRequest());

        List<Results> resultsList = resultsRepository.findAll();
        assertThat(resultsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSurvey_idIsRequired() throws Exception {
        int databaseSizeBeforeTest = resultsRepository.findAll().size();
        // set the field null
        results.setSurvey_id(null);

        // Create the Results, which fails.

        restResultsMockMvc.perform(post("/api/results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(results)))
            .andExpect(status().isBadRequest());

        List<Results> resultsList = resultsRepository.findAll();
        assertThat(resultsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuestion_idIsRequired() throws Exception {
        int databaseSizeBeforeTest = resultsRepository.findAll().size();
        // set the field null
        results.setQuestion_id(null);

        // Create the Results, which fails.

        restResultsMockMvc.perform(post("/api/results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(results)))
            .andExpect(status().isBadRequest());

        List<Results> resultsList = resultsRepository.findAll();
        assertThat(resultsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuestion_nameIsRequired() throws Exception {
        int databaseSizeBeforeTest = resultsRepository.findAll().size();
        // set the field null
        results.setQuestion_name(null);

        // Create the Results, which fails.

        restResultsMockMvc.perform(post("/api/results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(results)))
            .andExpect(status().isBadRequest());

        List<Results> resultsList = resultsRepository.findAll();
        assertThat(resultsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuestion_resultIsRequired() throws Exception {
        int databaseSizeBeforeTest = resultsRepository.findAll().size();
        // set the field null
        results.setQuestion_result(null);

        // Create the Results, which fails.

        restResultsMockMvc.perform(post("/api/results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(results)))
            .andExpect(status().isBadRequest());

        List<Results> resultsList = resultsRepository.findAll();
        assertThat(resultsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSurvey_timestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = resultsRepository.findAll().size();
        // set the field null
        results.setSurvey_timestamp(null);

        // Create the Results, which fails.

        restResultsMockMvc.perform(post("/api/results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(results)))
            .andExpect(status().isBadRequest());

        List<Results> resultsList = resultsRepository.findAll();
        assertThat(resultsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResults() throws Exception {
        // Initialize the database
        resultsRepository.saveAndFlush(results);

        // Get all the resultsList
        restResultsMockMvc.perform(get("/api/results?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(results.getId().intValue())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER.intValue())))
            .andExpect(jsonPath("$.[*].survey_id").value(hasItem(DEFAULT_SURVEY_ID.intValue())))
            .andExpect(jsonPath("$.[*].question_id").value(hasItem(DEFAULT_QUESTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].question_name").value(hasItem(DEFAULT_QUESTION_NAME.toString())))
            .andExpect(jsonPath("$.[*].question_result").value(hasItem(DEFAULT_QUESTION_RESULT.toString())))
            .andExpect(jsonPath("$.[*].survey_timestamp").value(hasItem(DEFAULT_SURVEY_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    public void getResults() throws Exception {
        // Initialize the database
        resultsRepository.saveAndFlush(results);

        // Get the results
        restResultsMockMvc.perform(get("/api/results/{id}", results.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(results.getId().intValue()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER.intValue()))
            .andExpect(jsonPath("$.survey_id").value(DEFAULT_SURVEY_ID.intValue()))
            .andExpect(jsonPath("$.question_id").value(DEFAULT_QUESTION_ID.intValue()))
            .andExpect(jsonPath("$.question_name").value(DEFAULT_QUESTION_NAME.toString()))
            .andExpect(jsonPath("$.question_result").value(DEFAULT_QUESTION_RESULT.toString()))
            .andExpect(jsonPath("$.survey_timestamp").value(DEFAULT_SURVEY_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResults() throws Exception {
        // Get the results
        restResultsMockMvc.perform(get("/api/results/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResults() throws Exception {
        // Initialize the database
        resultsService.save(results);

        int databaseSizeBeforeUpdate = resultsRepository.findAll().size();

        // Update the results
        Results updatedResults = resultsRepository.findOne(results.getId());
        updatedResults
                .user(UPDATED_USER)
                .survey_id(UPDATED_SURVEY_ID)
                .question_id(UPDATED_QUESTION_ID)
                .question_name(UPDATED_QUESTION_NAME)
                .question_result(UPDATED_QUESTION_RESULT)
                .survey_timestamp(UPDATED_SURVEY_TIMESTAMP);

        restResultsMockMvc.perform(put("/api/results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedResults)))
            .andExpect(status().isOk());

        // Validate the Results in the database
        List<Results> resultsList = resultsRepository.findAll();
        assertThat(resultsList).hasSize(databaseSizeBeforeUpdate);
        Results testResults = resultsList.get(resultsList.size() - 1);
        assertThat(testResults.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testResults.getSurvey_id()).isEqualTo(UPDATED_SURVEY_ID);
        assertThat(testResults.getQuestion_id()).isEqualTo(UPDATED_QUESTION_ID);
        assertThat(testResults.getQuestion_name()).isEqualTo(UPDATED_QUESTION_NAME);
        assertThat(testResults.getQuestion_result()).isEqualTo(UPDATED_QUESTION_RESULT);
        assertThat(testResults.getSurvey_timestamp()).isEqualTo(UPDATED_SURVEY_TIMESTAMP);

        // Validate the Results in Elasticsearch
        Results resultsEs = resultsSearchRepository.findOne(testResults.getId());
        assertThat(resultsEs).isEqualToComparingFieldByField(testResults);
    }

    @Test
    @Transactional
    public void updateNonExistingResults() throws Exception {
        int databaseSizeBeforeUpdate = resultsRepository.findAll().size();

        // Create the Results

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restResultsMockMvc.perform(put("/api/results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(results)))
            .andExpect(status().isCreated());

        // Validate the Results in the database
        List<Results> resultsList = resultsRepository.findAll();
        assertThat(resultsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteResults() throws Exception {
        // Initialize the database
        resultsService.save(results);

        int databaseSizeBeforeDelete = resultsRepository.findAll().size();

        // Get the results
        restResultsMockMvc.perform(delete("/api/results/{id}", results.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean resultsExistsInEs = resultsSearchRepository.exists(results.getId());
        assertThat(resultsExistsInEs).isFalse();

        // Validate the database is empty
        List<Results> resultsList = resultsRepository.findAll();
        assertThat(resultsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchResults() throws Exception {
        // Initialize the database
        resultsService.save(results);

        // Search the results
        restResultsMockMvc.perform(get("/api/_search/results?query=id:" + results.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(results.getId().intValue())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER.intValue())))
            .andExpect(jsonPath("$.[*].survey_id").value(hasItem(DEFAULT_SURVEY_ID.intValue())))
            .andExpect(jsonPath("$.[*].question_id").value(hasItem(DEFAULT_QUESTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].question_name").value(hasItem(DEFAULT_QUESTION_NAME.toString())))
            .andExpect(jsonPath("$.[*].question_result").value(hasItem(DEFAULT_QUESTION_RESULT.toString())))
            .andExpect(jsonPath("$.[*].survey_timestamp").value(hasItem(DEFAULT_SURVEY_TIMESTAMP.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Results.class);
    }
}
