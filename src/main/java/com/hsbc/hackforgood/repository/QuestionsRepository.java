package com.hsbc.hackforgood.repository;

import com.hsbc.hackforgood.domain.Questions;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Questions entity.
 */
@SuppressWarnings("unused")
public interface QuestionsRepository extends JpaRepository<Questions,Long> {

}
