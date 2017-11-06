package com.questionbank.app;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.questionbank.app.model.ErrorResponseModel;

@Provider
public class EntityExceptionMapper implements ExceptionMapper<EntityException> {

	public EntityExceptionMapper() {
	}

	@Override
	public Response toResponse(EntityException e) {
		ErrorResponseModel error = new ErrorResponseModel();
		error.setErrorCode(e.getErrorCode());
		error.setErrorDescription(e.getErrorDesc());
		return Response.status(e.getStatus()).entity(error).build();
	}
}