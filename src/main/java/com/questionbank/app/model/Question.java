package com.questionbank.app.model;

import java.util.List;

public class Question {

	private String questionName;
	private long questionSetNum;
	private List<String> questionTag;
	private List<QuestionDAO> questions;
	private String owner;
	private String date;
	
	public long getQuestionSetNum() {
		return questionSetNum;
	}

	public void setQuestionSetNum(long questionSetNum) {
		this.questionSetNum = questionSetNum;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	public List<String> getQuestionTag() {
		return questionTag;
	}

	public void setQuestionTag(List<String> questionTag) {
		this.questionTag = questionTag;
	}

	public List<QuestionDAO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDAO> questions) {
		this.questions = questions;
	}
}
