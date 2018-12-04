package com.hsbc.hackforgood.service;

import com.hsbc.hackforgood.domain.Questions;
import com.hsbc.hackforgood.repository.QuestionsRepository;
import com.hsbc.hackforgood.repository.search.QuestionsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Questions.
 */
@Service
@Transactional
public class QuestionsService {

    private final Logger log = LoggerFactory.getLogger(QuestionsService.class);
    
    private final QuestionsRepository questionsRepository;

    private final QuestionsSearchRepository questionsSearchRepository;

    public QuestionsService(QuestionsRepository questionsRepository, QuestionsSearchRepository questionsSearchRepository) {
        this.questionsRepository = questionsRepository;
        this.questionsSearchRepository = questionsSearchRepository;
    }

    /**
     * Save a questions.
     *
     * @param questions the entity to save
     * @return the persisted entity
     */
    public Questions save(Questions questions) {
        log.debug("Request to save Questions : {}", questions);
        Questions result = questionsRepository.save(questions);
        questionsSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the questions.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Questions> findAll() {
        log.debug("Request to get all Questions");
        List<Questions> result = questionsRepository.findAll();

        return result;
    }

    /**
     *  Get one questions by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Questions findOne(Long id) {
        log.debug("Request to get Questions : {}", id);
        Questions questions = questionsRepository.findOne(id);
        return questions;
    }

    /**
     *  Delete the  questions by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Questions : {}", id);
        questionsRepository.delete(id);
        questionsSearchRepository.delete(id);
    }

    /**
     * Search for the questions corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Questions> search(String query) {
        log.debug("Request to search Questions for query {}", query);
        return StreamSupport
            .stream(questionsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
