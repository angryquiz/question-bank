package com.questionbank.app.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel
public class ErrorResponseModel {
	
	@ApiModelProperty(position = 1, required = true, value = "errorCode")
	private int errorCode;

	@ApiModelProperty(position = 2, required = true, value = "errorDescription")
	private String errorDescription;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}	
}
