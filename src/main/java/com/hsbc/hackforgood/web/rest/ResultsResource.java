package com.hsbc.hackforgood.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hsbc.hackforgood.domain.Results;
import com.hsbc.hackforgood.service.ResultsService;
import com.hsbc.hackforgood.service.dto.ResultChart;
import com.hsbc.hackforgood.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Results.
 */
@RestController
@RequestMapping("/api")
public class ResultsResource {

    private final Logger log = LoggerFactory.getLogger(ResultsResource.class);

    private static final String ENTITY_NAME = "results";
        
    private final ResultsService resultsService;

    public ResultsResource(ResultsService resultsService) {
        this.resultsService = resultsService;
    }

    /**
     * POST  /results : Create a new results.
     *
     * @param results the results to create
     * @return the ResponseEntity with status 201 (Created) and with body the new results, or with status 400 (Bad Request) if the results has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/results")
    @Timed
    public ResponseEntity<Results> createResults(@Valid @RequestBody Results results) throws URISyntaxException {
        log.debug("REST request to save Results : {}", results);
        if (results.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new results cannot already have an ID")).body(null);
        }
        Results result = resultsService.save(results);
        return ResponseEntity.created(new URI("/api/results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/resultsall")
    @Timed
    public ResponseEntity<List<Results>> createMultipleResults(@Valid @RequestBody List<Results> results) throws URISyntaxException {
        log.debug("REST request to save Results : {}", results);
        if (results.size()  == 0) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new results cannot already have an ID")).body(null);
        }
        List<Results> result = resultsService.saveAll(results);
        return ResponseEntity.ok().body(result);
    }

    /**
     * PUT  /results : Updates an existing results.
     *
     * @param results the results to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated results,
     * or with status 400 (Bad Request) if the results is not valid,
     * or with status 500 (Internal Server Error) if the results couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/results")
    @Timed
    public ResponseEntity<Results> updateResults(@Valid @RequestBody Results results) throws URISyntaxException {
        log.debug("REST request to update Results : {}", results);
        if (results.getId() == null) {
            return createResults(results);
        }
        Results result = resultsService.save(results);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, results.getId().toString()))
            .body(result);
    }

    /**
     * GET  /results : get all the results.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of results in body
     */
    @GetMapping("/results")
    @Timed
    public List<Results> getAllResults() {
        log.debug("REST request to get all Results");
        return resultsService.findAll();
    }

    @GetMapping("/chart")
    @Timed
    public List<ResultChart> getChartData() {
        log.debug("REST request to get all chartData");
        return resultsService.getChartData();
    }

    /**
     * GET  /results/:id : get the "id" results.
     *
     * @param id the id of the results to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the results, or with status 404 (Not Found)
     */
    @GetMapping("/results/{id}")
    @Timed
    public ResponseEntity<Results> getResults(@PathVariable Long id) {
        log.debug("REST request to get Results : {}", id);
        Results results = resultsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(results));
    }

    /**
     * DELETE  /results/:id : delete the "id" results.
     *
     * @param id the id of the results to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/results/{id}")
    @Timed
    public ResponseEntity<Void> deleteResults(@PathVariable Long id) {
        log.debug("REST request to delete Results : {}", id);
        resultsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/results?query=:query : search for the results corresponding
     * to the query.
     *
     * @param query the query of the results search 
     * @return the result of the search
     */
    @GetMapping("/_search/results")
    @Timed
    public List<Results> searchResults(@RequestParam String query) {
        log.debug("REST request to search Results for query {}", query);
        return resultsService.search(query);
    }


}
