package au.com.simpsons.digital.quest.routing;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.exampleobject.Builder.exampleOjectBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import au.com.simpsons.digital.quest.model.SearchTextRequest;
import au.com.simpsons.digital.quest.model.SearchTextResponse;
import au.com.simpsons.digital.quest.service.QuestServiceIF;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


/**
 * Routing Class, the starting point to access API for the application
 * @author Ramesh
 *
 */

@Configuration
@Slf4j
public class RoutingConfiguration{

	@Value("${api.to.get.search.text.count.Url}")
	private String apiToGetSearchTextCountUrl;

	@Value("${api.to.get.top.text.count.Url}")
	private String apiToGetTopTextCountUrl;


	private final QuestServiceIF service;

	public RoutingConfiguration(QuestServiceIF service) {
		this.service = service;
	}

	@Bean
	RouterFunction<ServerResponse> routerFunction() {
		return route().POST(apiToGetSearchTextCountUrl, accept(MediaType.APPLICATION_JSON), this::getSearchTextCount,  findSearchTextCountOpenAPI()).build()
				.and(route().GET(apiToGetTopTextCountUrl, this::getTopTextCount, findTopTextCountOpenAPI()).build());
	}



	private Consumer<Builder> findSearchTextCountOpenAPI() {
		return ops -> ops.tag("searchTextCount")
				.operationId("getSearchTextCount").summary("Search the input text count").tags(new String[] { "Search Text Count List" })
				.requestBody(requestBodyBuilder().required(true).description("Json Request Body").content(contentBuilder().mediaType("application/json").schema(schemaBuilder().implementation(SearchTextRequest.class)).example(exampleOjectBuilder().value("{\"searchText\":[\"Duis\" , \"Sed\", \"Donec\" , \"Augue\", \"Pellentesque\" , \"123\"]}"))))
				.response(responseBuilder().responseCode("200").description("Successful operation").content(contentBuilder().mediaType("application/json").schema(schemaBuilder().implementation(SearchTextResponse.class)).example(exampleOjectBuilder().value("{\"counts\":[{\"Duis\":11},{\"Sed\":16},{\"Donec\":8},{\"Augue\":7},{\"Pellentesque\":6},{\"123\":0}]}"))))
				.response(responseBuilder().responseCode("400").description("Input request is invalid"))
				.response(responseBuilder().responseCode("500").description("Not able to access the input file"));
	}

	private Consumer<Builder> findTopTextCountOpenAPI() {
		return ops -> ops.tag("topTextCount")
				.operationId("getTopTextCount").summary("List the top text with their count").tags(new String[] { "List Top Text Count" })
				.parameter(parameterBuilder().in(ParameterIn.PATH).name("count").description("Top-Text-Count to list").implementation(Long.class))
				.response(responseBuilder().responseCode("200").description("Successful operation").content(contentBuilder().mediaType("text/csv").schema(schemaBuilder().implementation(String.class)).example(exampleOjectBuilder().value("VEL|17\nEGET|17\nSED|16\n"))))
				.response(responseBuilder().responseCode("400").description("Input value is not a positive Long Number"))
				.response(responseBuilder().responseCode("500").description("Not able to access the input file"));
	}

	private Mono<ServerResponse> getSearchTextCount(ServerRequest request) {
		log.debug("starting getSearchTextCount via routing ");
		return request.bodyToMono(HashMap.class)
				.flatMap(searchRequest -> {
					log.debug("starting getSearchTextCount via routing input value {}", searchRequest);
					log.debug("getSearchTextCount searchText value {}", searchRequest.get("searchText"));
					SearchTextRequest searchText = new SearchTextRequest();
					searchText.setSearchText((ArrayList<String>)searchRequest.get("searchText"));
					return  service.getSearchTextCount(searchText.getSearchText());
				});

	}

	private Mono<ServerResponse> getTopTextCount(ServerRequest request) {
		log.debug("starting getTopTextCount via routing inputValue : {}", request.pathVariable("count") );
		return Mono.just(request)
				.flatMap(inputValue -> service.getTopTextCount(request.pathVariable("count")));
	}
}
