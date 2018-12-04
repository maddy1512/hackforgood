package com.hsbc.hackforgood.repository;

import com.hsbc.hackforgood.domain.Results;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Results entity.
 */
@SuppressWarnings("unused")
public interface ResultsRepository extends JpaRepository<Results,Long> {

}