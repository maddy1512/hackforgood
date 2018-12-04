package com.hsbc.hackforgood.service.dto;

public class ResultChart {
    Long count;
    Long questionId;
    String title;
    String result;


    public ResultChart(Long count, Long questionId, String title, String result) {
        this.count = count;
        this.questionId = questionId;
        this.title = title;
        this.result = result;
    }

    public ResultChart() {
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
