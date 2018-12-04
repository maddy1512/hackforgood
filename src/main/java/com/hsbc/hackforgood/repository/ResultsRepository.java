package com.hsbc.hackforgood.repository;

import com.hsbc.hackforgood.domain.Results;

import com.hsbc.hackforgood.service.dto.ResultChart;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Results entity.
 */
@SuppressWarnings("unused")
public interface ResultsRepository extends JpaRepository<Results,Long> {
    @Query("select new com.hsbc.hackforgood.service.dto.ResultChart(count(r), r.question_id, r.question_name,r.question_result) from Results r group by r.question_id,r.question_result")
    List<ResultChart> getChartData();
}
