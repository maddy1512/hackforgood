package com.hsbc.hackforgood.repository;

import com.hsbc.hackforgood.domain.Results;

import com.hsbc.hackforgood.service.dto.ResultChart;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Results entity.
 */
@SuppressWarnings("unused")
public interface ResultsRepository extends JpaRepository<Results,Long> {
    @Query("select new com.hsbc.hackforgood.service.dto.ResultChart(count(r), r.question_id, r.question_name,r.question_result) from Results r where r.survey_id in :survey group by r.question_id,r.question_result")
    List<ResultChart> getChartData(@Param("survey") List<Long> surveyIds);

    @Query("select distinct r.survey_id from Results r where r.question_result =:filter")
    List<Long> getSurveyIds(@Param("filter") String filter);

    @Query("select distinct r.survey_id from Results r")
    List<Long> getAllSurveyIds();
}
