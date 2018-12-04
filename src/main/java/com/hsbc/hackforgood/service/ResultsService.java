package com.hsbc.hackforgood.service;

import com.hsbc.hackforgood.domain.Results;
import com.hsbc.hackforgood.repository.ResultsRepository;
import com.hsbc.hackforgood.repository.search.ResultsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Results.
 */
@Service
@Transactional
public class ResultsService {

    private final Logger log = LoggerFactory.getLogger(ResultsService.class);
    
    private final ResultsRepository resultsRepository;

    private final ResultsSearchRepository resultsSearchRepository;

    public ResultsService(ResultsRepository resultsRepository, ResultsSearchRepository resultsSearchRepository) {
        this.resultsRepository = resultsRepository;
        this.resultsSearchRepository = resultsSearchRepository;
    }

    /**
     * Save a results.
     *
     * @param results the entity to save
     * @return the persisted entity
     */
    public Results save(Results results) {
        log.debug("Request to save Results : {}", results);
        Results result = resultsRepository.save(results);
        resultsSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the results.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Results> findAll() {
        log.debug("Request to get all Results");
        List<Results> result = resultsRepository.findAll();

        return result;
    }

    /**
     *  Get one results by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Results findOne(Long id) {
        log.debug("Request to get Results : {}", id);
        Results results = resultsRepository.findOne(id);
        return results;
    }

    /**
     *  Delete the  results by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Results : {}", id);
        resultsRepository.delete(id);
        resultsSearchRepository.delete(id);
    }

    /**
     * Search for the results corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Results> search(String query) {
        log.debug("Request to search Results for query {}", query);
        return StreamSupport
            .stream(resultsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
