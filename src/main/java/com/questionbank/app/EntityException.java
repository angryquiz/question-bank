package com.questionbank.app;

import java.io.Serializable;

import javax.ws.rs.core.Response;

public class EntityException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;

	private Response.Status status;
	private int errorCode;
	private String errorDesc;
	
	public EntityException(String message) {
		super(message);
	}
	
	public EntityException(Response.Status status, int errorCode, String errorDesc) {
		super(errorDesc);
		this.status = status;
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;	
	}

	public Response.Status getStatus() {
		return status;
	}

	public void setStatus(Response.Status status) {
		this.status = status;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	
}