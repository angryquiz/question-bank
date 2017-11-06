package com.questionbank.app.model;

import java.io.Serializable;
import java.util.Map;

public class QuestionDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int questionNumber;
	private String question;
	private String answer;
	private int correctAnswerCount;
	private String explanation;
	private String illustration;
	private Map<String,String> selections;
	
	private String testerAnswer;
	private boolean testerCorrect;
	
	private String questionType;
	
	
	//used for Quiz Logic
	private String memberAnswer;
	private boolean correct;
	
	
	
	public String getMemberAnswer() {
		return memberAnswer;
	}

	public void setMemberAnswer(String memberAnswer) {
		this.memberAnswer = memberAnswer;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public boolean isTesterCorrect() {
		return testerCorrect;
	}

	public void setTesterCorrect(boolean testerCorrect) {
		this.testerCorrect = testerCorrect;
	}

	public String getTesterAnswer() {
		return testerAnswer;
	}

	public void setTesterAnswer(String testerAnswer) {
		this.testerAnswer = testerAnswer;
	}


	public Map<String, String> getSelections() {
		return selections;
	}

	public void setSelections(Map<String, String> selections) {
		this.selections = selections;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}
	
	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getCorrectAnswerCount() {
		return correctAnswerCount;
	}
	public void setCorrectAnswerCount(int correctAnswerCount) {
		this.correctAnswerCount = correctAnswerCount;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public String getIllustration() {
		return illustration;
	}
	public void setIllustration(String illustration) {
		this.illustration = illustration;
	}

	@Override
	public String toString() {
		return "QuestionDAO [questionNumber=" + questionNumber + ", question="
				+ question + ", answer=" + answer + ", correctAnswerCount="
				+ correctAnswerCount + ", explanation=" + explanation
				+ ", illustration=" + illustration + ", selections="
				+ selections + ", testerAnswer=" + testerAnswer
				+ ", testerCorrect=" + testerCorrect + ", questionType="
				+ questionType + ", memberAnswer=" + memberAnswer
				+ ", correct=" + correct + "]";
	}

}
