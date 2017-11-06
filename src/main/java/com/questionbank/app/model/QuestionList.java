package com.questionbank.app.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel
public class QuestionList {

    private List<Question> questions = new ArrayList<Question>();

    public QuestionList() {
    }

    public QuestionList(final Collection<Question> questions) {
        this.questions.addAll(questions);
    }

    @ApiModelProperty(required = true, position = 1)
    public List<Question> getQuestions() {
        return questions;
    }

    @ApiModelProperty(required = true, position = 2)
    public int getCount() {
        return questions.size();
    }
}
