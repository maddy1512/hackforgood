package com.hsbc.hackforgood.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Results.
 */
@Entity
@Table(name = "results")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "results")
public class Results implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user", nullable = false)
    private Long user;

    @NotNull
    @Column(name = "survey_id", nullable = false)
    private Long survey_id;

    @NotNull
    @Column(name = "question_id", nullable = false)
    private Long question_id;

    @NotNull
    @Column(name = "question_name", nullable = false)
    private String question_name;

    @NotNull
    @Column(name = "question_result", nullable = false)
    private String question_result;

    @NotNull
    @Column(name = "survey_timestamp", nullable = false)
    private LocalDate survey_timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser() {
        return user;
    }

    public Results user(Long user) {
        this.user = user;
        return this;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getSurvey_id() {
        return survey_id;
    }

    public Results survey_id(Long survey_id) {
        this.survey_id = survey_id;
        return this;
    }

    public void setSurvey_id(Long survey_id) {
        this.survey_id = survey_id;
    }

    public Long getQuestion_id() {
        return question_id;
    }

    public Results question_id(Long question_id) {
        this.question_id = question_id;
        return this;
    }

    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_name() {
        return question_name;
    }

    public Results question_name(String question_name) {
        this.question_name = question_name;
        return this;
    }

    public void setQuestion_name(String question_name) {
        this.question_name = question_name;
    }

    public String getQuestion_result() {
        return question_result;
    }

    public Results question_result(String question_result) {
        this.question_result = question_result;
        return this;
    }

    public void setQuestion_result(String question_result) {
        this.question_result = question_result;
    }

    public LocalDate getSurvey_timestamp() {
        return survey_timestamp;
    }

    public Results survey_timestamp(LocalDate survey_timestamp) {
        this.survey_timestamp = survey_timestamp;
        return this;
    }

    public void setSurvey_timestamp(LocalDate survey_timestamp) {
        this.survey_timestamp = survey_timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Results results = (Results) o;
        if (results.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, results.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Results{" +
            "id=" + id +
            ", user='" + user + "'" +
            ", survey_id='" + survey_id + "'" +
            ", question_id='" + question_id + "'" +
            ", question_name='" + question_name + "'" +
            ", question_result='" + question_result + "'" +
            ", survey_timestamp='" + survey_timestamp + "'" +
            '}';
    }
}
