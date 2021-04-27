package au.com.simpsons.digital.quest.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;

import au.com.simpsons.digital.quest.error.QuestException;
import au.com.simpsons.digital.quest.model.SearchTextResponse;
import au.com.simpsons.digital.quest.service.QuestServiceIF;
import au.com.simpsons.digital.quest.util.ParsedFileData;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Service class to implement the business logics
 * @author Ramesh
 *
 */
@Service
@Slf4j
public class QuestService implements QuestServiceIF {

	@Value("${input.file.path}")
	private String inputFilePath;

	@Override
	public Mono<ServerResponse> getSearchTextCount(ArrayList<String>  inputValue) {
		log.debug("Started getSearchTextCount for {}", inputValue);
		try {

			if (null == inputValue || inputValue.isEmpty()) {
				throw new QuestException(HttpStatus.BAD_REQUEST.value(),"Input request is invalid");
			}

			return getParsedFileData()
					.flatMap(mappedValue ->  {
						var inputTextCountResponse = new SearchTextResponse();

						var count = new ArrayList<Map<String, Long>>();
						inputValue.forEach(value ->{
							var outputValue = new  HashMap<String, Long>();
							if (mappedValue.containsKey(value.toUpperCase())){
								outputValue.put(value, mappedValue.get(value.toUpperCase()));
							} else {
								outputValue.put(value, Long.valueOf(0));
							}
							count.add(outputValue);
						});
						inputTextCountResponse.setCounts(count);

						log.debug("Ended getSearchTextCount for {}", inputTextCountResponse);
						return Mono.just(inputTextCountResponse);
					})
					.flatMap(response -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(response))
					.onErrorResume(e -> {
						log.error("Exception occurred while executing - getSearchTextCount : {}", e.toString());
						QuestException ex = (QuestException)e;
						return  ServerResponse.status(ex.getHttpStatus())
								.contentType(MediaType.APPLICATION_JSON)
								.bodyValue(ex.toString());});

		} catch (QuestException e) {
			log.error("Exception occurred while executing the method getSearchTextCount : {}", e.toString());
			return  ServerResponse.status(e.getHttpStatus())
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(e.toString());
		}

	}

	@Override
	public Mono<ServerResponse> getTopTextCount(String count) {
		log.debug("Started getTopTextCount for {}", count);
		try {
			Long countValue = extractInputValue(count);

			return getParsedFileData()
					.map(mappedValue ->  {
						var sb = new StringBuilder();
						var reverseSortedMap = new LinkedHashMap<String, Long>();
						mappedValue.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
						.forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));						
						reverseSortedMap.entrySet().stream().limit(countValue)
						.forEach(x -> sb.append(x.getKey() + "|" + x.getValue() + "\n"));
						log.debug("Ended getTopTextCount for {}", sb.toString());
						return sb.toString();
					})
					.flatMap(response -> ServerResponse.ok().bodyValue(response))
					.onErrorResume(e -> {
						log.error("Exception occurred while executing - getTopTextCount : {}", e.toString());
						QuestException ex = (QuestException)e;
						return  ServerResponse.status(ex.getHttpStatus())
								.contentType(MediaType.APPLICATION_JSON)
								.bodyValue(ex.toString());});


		} catch (QuestException e) {
			log.error("Exception occurred while executing the method getTopTextCount : {}", e.toString());

			return  ServerResponse.status(e.getHttpStatus())
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(e.toString());
		}

	}

	/**
	 * Returns the Long count value
	 * 
	 * @param  count
	 * @return Long 
	 */

	private Long extractInputValue(String count){
		try {
			Long value = Long.parseLong(count);
			if (value < 0) throw new QuestException(HttpStatus.BAD_REQUEST.value(),"Input value is not a positive Long Number");
			return value;
		} catch ( Exception e ) {
			throw new QuestException(HttpStatus.BAD_REQUEST.value(),"Input value is not a positive Long Number");
		}
	}

	/**
	 * Returns the count for each text provided as the input from the file.
	 * Exception is thrown when issues with accessing the input file or if input text doesn't match the sample request.
	 * 
	 * @return mappedValue 
	 */
	private Mono<HashMap<String, Long>> getParsedFileData() {
		log.debug("Started getParsedFileData ");
		String fileName = "inputFileData";
		if (null != ParsedFileData.get(fileName)) {
			log.debug("getParsedFileData data already exist {}", ParsedFileData.get(fileName).size() );
			return Mono.just(ParsedFileData.get(fileName));
		}
		else {
			log.debug("getParsedFileData file it to be parsed ");
			return parseInputFile()
					.map(inputData -> inputData.replaceAll("[.,]+", "").toUpperCase().split("\\W+"))
					.map(response ->  { 
						var mappedValue = new HashMap<String, Long>(); 
						var wordList = Arrays.asList(response);  

						wordList.stream().forEach(e -> {
							if (mappedValue.containsKey(e)){
								Long count = mappedValue.get(e);
								mappedValue.put(e, count + 1);
							} else {
								mappedValue.put(e, Long.valueOf(1));
							} 
						});

						log.debug("getParsedFileData data added to the object ");
						ParsedFileData.set(fileName, mappedValue);
						return mappedValue;
					});

		}
	}

	/**
	 * Returns the count for each text provided as the input from the file.
	 * Exception is thrown when issues with accessing the input file or if input text doesn't match the sample request.
	 *
	 * @return String 
	 */

	private Mono<String> parseInputFile() {
		log.debug("Started to parseInputFile , {} ",inputFilePath);
		try {
			return Mono.just(Files.readString(Paths.get(inputFilePath)));
		} catch (IOException e) {
			log.error("Error : Issues with accessing the input file for parsing ", e.getMessage());
			return Mono.error( new QuestException(500, "Not able to access the input file"));
		}

	}

}