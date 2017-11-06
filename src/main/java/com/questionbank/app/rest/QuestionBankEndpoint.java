package com.questionbank.app.rest;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.questionbank.app.EntityException;
import com.questionbank.app.model.Question;
import com.questionbank.app.model.QuestionDAO;
import com.questionbank.app.model.QuestionList;
import com.questionbank.app.rest.config.ApplicationConfiguration;
import com.questionbank.app.service.QuestionBankService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * REST endpoint for user manipulation.
 * https://github.com/JakubStas/Spring4WithSwagger/tree/master/src/main mvn
 * -Djetty.port=8888 jetty:run
 */
@Api(value = "questions", description = "Endpoint for question bank management")
@Path("/question-bank")
public class QuestionBankEndpoint {

	@Inject
	private ApplicationConfiguration appConfig;

	@Inject
	private QuestionBankService questionBankService;

	@Context
	private UriInfo uriInfo;

	
	@SuppressWarnings("unused")
	@GET
	@Path("/search/summary/{query}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns question summary", notes = "Returns list of questions", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Get summary results", response = String.class),
			@ApiResponse(code = 404, message = "No result found."),
			@ApiResponse(code = 500, message = "Internal server error") })
	public Response searchSummary(
			@ApiParam(name = "query", value = "Query", required = true) @PathParam("query") String query) {

		// https://chanchal.wordpress.com/2015/12/29/calling-elasticsearch-apis-using-jax-rs-client-jersey-client-jackson/

		try {
			String input = "{\"query\":{\"query_string\":{\"query\":\"" + query + "\"}}, \"_source\": { \"exclude\": [ \"questions\" ] }}";

			final ClientConfig config = new DefaultClientConfig();
			final Client client = Client.create(config);
			WebResource webResource = client.resource(appConfig.getElasticSearchUrl());
			ClientResponse response = webResource.path("/questionbank/questions/_search").queryParam("size","100")
					.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, input);
			return Response.ok(response.getEntity(String.class), MediaType.APPLICATION_JSON_TYPE).build();
		} catch (Exception e) {
			// https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
			throw new EntityException(Status.INTERNAL_SERVER_ERROR, 999,
					e.getMessage() + ". Tech Support");
		}

	}
	
	/*
	 * 
	 * POST
	 * https://host.es.amazonaws.
	 * com/questionbank/config/AVy5aB_CqWM7T6lWvbeM/_update?fields=_source
	 * 
	 * { "script" : { "inline": "ctx._source.questionSetNum += params.count",
	 * "lang": "painless", "params" : { "count" : 1 } } }
	 * 
	 * { "_index": "questionbank", "_type": "config", "_id":
	 * "AVy5aB_CqWM7T6lWvbeM", "_version": 2, "result": "updated", "_shards": {
	 * "total": 2, "successful": 1, "failed": 0 }, "get": { "found": true,
	 * "_source": { "questionSetNum": 103301992 } } }
	 * 
	 * 
	 */

	@SuppressWarnings("unused")
	@GET
	@Path("/getQuestionSetNum")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get Incremented Question Set Number", notes = "Returns a questionSetNumber to use.", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful retrieval", response = String.class),
			@ApiResponse(code = 404, message = "Question Set Number  does not exist"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public Response getQuestionSetNum() {
		// https://chanchal.wordpress.com/2015/12/29/calling-elasticsearch-apis-using-jax-rs-client-jersey-client-jackson/
		try {
			String input = "{\"script\" : { \"inline\": \"ctx._source.questionSetNum += params.count\", \"lang\": \"painless\", \"params\" : { \"count\" : 1 } } }";
			final ClientConfig config = new DefaultClientConfig();
			final Client client = Client.create(config);
			WebResource webResource = client.resource(appConfig.getElasticSearchUrl());
			ClientResponse response = webResource
					.path("/questionbank/config/AVy5aB_CqWM7T6lWvbeM/_update").queryParam("fields", "_source")
					.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, input);
			return Response.ok(response.getEntity(String.class), MediaType.APPLICATION_JSON_TYPE).build();
		} catch (Exception e) {
			// https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
			throw new EntityException(Status.INTERNAL_SERVER_ERROR, 999,
					e.getMessage() + ". Tech Support");
		}

	}

	/**
	 * Upload question in json format
	 */
	@PUT
	@Path("/questionbank/questions")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Updates users avatar", notes = "Provides means to upload new versions of avatar based on username")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful retrieval of users avatar"),
			@ApiResponse(code = 404, message = "User with given username does not exist"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public Response uploadJSONQuestion(Question json) {

		try {
			// https://crispcode.wordpress.com/2012/07/10/java-client-to-upload-files-on-jersey-rest-web-service/
			final ClientConfig config = new DefaultClientConfig();
			final Client client = Client.create(config);
			WebResource webResource = client.resource(appConfig.getElasticSearchUrl() + "/questionbank/questions");
			webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(ClientResponse.class,
					json);
			// ListWrapper listWrapper = response.getEntity(ListWrapper.class);
			return Response.status(Status.OK).build();

		} catch (Exception e) {
			// https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
			throw new EntityException(Status.INTERNAL_SERVER_ERROR, 999,
					e.getMessage() + ". Tech Support");
		}
	}

	/**
	 * Upload question.
	 */
	@PUT
	@Path("/upload/{owner}/{questionName}/{tags}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@ApiImplicitParams(@ApiImplicitParam(dataType = "file", name = "questionFile", paramType = "body"))
	@ApiOperation(value = "Updates users avatar", notes = "Provides means to upload new versions of avatar based on username")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful retrieval of users avatar"),
			@ApiResponse(code = 404, message = "User with given username does not exist"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public Response uploadQuestion(
			@ApiParam(name = "questionName", value = "Question name", required = true) @PathParam("questionName") String questionName,
			@ApiParam(name = "owner", value = "Owner name", required = true) @PathParam("owner") String owner,
			@ApiParam(name = "tags", value = "Tags", required = true) @PathParam("tags") String tags,
			@ApiParam(access = "hidden") @FormDataParam("questionFile") InputStream questionFileInputStream) {

		if (!questionBankService.isValidName(questionName)) {
			throw new EntityException(Status.NOT_FOUND, 101,
					"Invalid Question Name. Letters, numbers, dashes and underscores are the allowed values");
		}

		if (!questionBankService.isValidName(owner)) {
			throw new EntityException(Status.NOT_FOUND, 102,
					"Invalid Owner Name. Letters, numbers, dashes and underscores are the allowed values");
		}

		try {
			List<QuestionDAO> items = questionBankService.loadData(questionFileInputStream);

			Question q = new Question();
			q.setQuestionName(questionName);
			if (!StringUtils.isEmpty(tags)) {
				if (tags.contains(",")) {
					q.setQuestionTag(Arrays.asList(tags.split(",")));
				} else {
					q.setQuestionTag(Arrays.asList(tags));
				}
			}
			// http://stackoverflow.com/questions/19112357/java-simpledateformatyyyy-mm-ddthhmmssz-gives-timezone-as-ist
			q.setDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").format(new Date()));
			q.setOwner(owner);
			q.setQuestions(items);

			// https://crispcode.wordpress.com/2012/07/10/java-client-to-upload-files-on-jersey-rest-web-service/
			final ClientConfig config = new DefaultClientConfig();
			final Client client = Client.create(config);
			WebResource webResource = client.resource(appConfig.getElasticSearchUrl() + "/questionbank/questions");
			webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(ClientResponse.class,
					q);
			// ListWrapper listWrapper = response.getEntity(ListWrapper.class);
			return Response.status(Status.OK).build();

		} catch (Exception e) {
			// https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
			throw new EntityException(Status.INTERNAL_SERVER_ERROR, 999,
					e.getMessage() + ". Tech Support");
		}
	}

	@SuppressWarnings("unused")
	@GET
	@Path("/search/{query}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns user details", notes = "Returns a complete list of users details with a date of last modification.", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful retrieval of user detail", response = String.class),
			@ApiResponse(code = 404, message = "User with given username does not exist"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public Response search(
			@ApiParam(name = "query", value = "Query", required = true) @PathParam("query") String query) {

		// https://chanchal.wordpress.com/2015/12/29/calling-elasticsearch-apis-using-jax-rs-client-jersey-client-jackson/

		try {
			String input = "{\"query\":{\"query_string\":{\"query\":\"" + query + "\"}}}";

			final ClientConfig config = new DefaultClientConfig();
			final Client client = Client.create(config);
			WebResource webResource = client.resource(appConfig.getElasticSearchUrl());
			ClientResponse response = webResource.path("/questionbank/questions/_search")
					.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, input);
			String str = response.getEntity(String.class);
			String questionName = "aaa";
			// String questionName =
			// response.getSource().get("questionName").asText();

			final List<Question> questions = new ArrayList<Question>();

			Question q = new Question();
			q.setQuestionName(questionName);
			q.setQuestionTag(Arrays.asList("a", "b"));
			questions.add(q);

			final QuestionList questionsList = new QuestionList(questions);

			return Response.ok(str, MediaType.APPLICATION_JSON_TYPE).build();

		} catch (Exception e) {
			// https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
			throw new EntityException(Status.INTERNAL_SERVER_ERROR, 999,
					e.getMessage() + ". Tech Support");
		}

	}

}
