package com.hsbc.hackforgood.repository.search;

import com.hsbc.hackforgood.domain.Results;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Results entity.
 */
public interface ResultsSearchRepository extends ElasticsearchRepository<Results, Long> {
}
