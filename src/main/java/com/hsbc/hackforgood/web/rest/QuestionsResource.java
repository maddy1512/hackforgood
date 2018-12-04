package com.hsbc.hackforgood.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hsbc.hackforgood.domain.Questions;
import com.hsbc.hackforgood.service.QuestionsService;
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
 * REST controller for managing Questions.
 */
@RestController
@RequestMapping("/api")
public class QuestionsResource {

    private final Logger log = LoggerFactory.getLogger(QuestionsResource.class);

    private static final String ENTITY_NAME = "questions";
        
    private final QuestionsService questionsService;

    public QuestionsResource(QuestionsService questionsService) {
        this.questionsService = questionsService;
    }

    /**
     * POST  /questions : Create a new questions.
     *
     * @param questions the questions to create
     * @return the ResponseEntity with status 201 (Created) and with body the new questions, or with status 400 (Bad Request) if the questions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/questions")
    @Timed
    public ResponseEntity<Questions> createQuestions(@Valid @RequestBody Questions questions) throws URISyntaxException {
        log.debug("REST request to save Questions : {}", questions);
        if (questions.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new questions cannot already have an ID")).body(null);
        }
        Questions result = questionsService.save(questions);
        return ResponseEntity.created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /questions : Updates an existing questions.
     *
     * @param questions the questions to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated questions,
     * or with status 400 (Bad Request) if the questions is not valid,
     * or with status 500 (Internal Server Error) if the questions couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/questions")
    @Timed
    public ResponseEntity<Questions> updateQuestions(@Valid @RequestBody Questions questions) throws URISyntaxException {
        log.debug("REST request to update Questions : {}", questions);
        if (questions.getId() == null) {
            return createQuestions(questions);
        }
        Questions result = questionsService.save(questions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, questions.getId().toString()))
            .body(result);
    }

    /**
     * GET  /questions : get all the questions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of questions in body
     */
    @GetMapping("/questions")
    @Timed
    public List<Questions> getAllQuestions() {
        log.debug("REST request to get all Questions");
        return questionsService.findAll();
    }

    /**
     * GET  /questions/:id : get the "id" questions.
     *
     * @param id the id of the questions to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the questions, or with status 404 (Not Found)
     */
    @GetMapping("/questions/{id}")
    @Timed
    public ResponseEntity<Questions> getQuestions(@PathVariable Long id) {
        log.debug("REST request to get Questions : {}", id);
        Questions questions = questionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(questions));
    }

    /**
     * DELETE  /questions/:id : delete the "id" questions.
     *
     * @param id the id of the questions to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/questions/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuestions(@PathVariable Long id) {
        log.debug("REST request to delete Questions : {}", id);
        questionsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/questions?query=:query : search for the questions corresponding
     * to the query.
     *
     * @param query the query of the questions search 
     * @return the result of the search
     */
    @GetMapping("/_search/questions")
    @Timed
    public List<Questions> searchQuestions(@RequestParam String query) {
        log.debug("REST request to search Questions for query {}", query);
        return questionsService.search(query);
    }


}
