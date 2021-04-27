package au.com.simpsons.digital.quest.service;

import java.util.ArrayList;

import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

/**
 * Interface class to define the methods used for the business logic
 * @author Ramesh
 *
 */
//@Tag(name="Quest")
public interface QuestServiceIF {

	/**
	 * Returns the count for each text provided as the input from the file.
	 * Exception is thrown when issues with accessing the input file or if input text doesn't match the sample request.
	 *
	 * @param  inputValue  input test to be fetched
	 * @return ServerResponse 
	 */
	Mono<ServerResponse> getSearchTextCount(ArrayList<String> inputValue);

	/**
	 * Returns the list of words from the file based on the input value .
	 * Exception is thrown when issues with accessing the input file .
	 *
	 * @param  count number of values to be fetched
	 * @return ServerResponse 
	 */
	Mono<ServerResponse> getTopTextCount(String count);
}
