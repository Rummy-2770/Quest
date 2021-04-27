/**
 * 
 */
package au.com.simpsons.digital.quest.service.impl;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import au.com.simpsons.digital.quest.QuestApplication;
import au.com.simpsons.digital.quest.util.ParsedFileData;
import lombok.extern.slf4j.Slf4j;
import reactor.test.StepVerifier;

/**
 * @author ramesh
 *
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { QuestApplication.class })
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class QuestServiceTest {
	@InjectMocks
	private QuestService questService = new QuestService();

	/**
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		ReflectionTestUtils.setField(questService, "inputFilePath", "src/test/resources/mock/sampleText.txt");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link au.com.optus.digital.quest.service.impl.QuestService#getInputTextCount(java.util.List)}.
	 */

	@Test
	void testGetInputTextCount_success() {

		ArrayList<String>  inputValue = new ArrayList<String>();
		inputValue.add("Duis");

		StepVerifier
		.create(questService.getSearchTextCount(inputValue))
		.expectNextMatches(result -> {
			System.out.println(result.statusCode());
			return (result.statusCode().value() ==200);
		})
		.expectComplete()
		.verify();
	}

	@Test
	void testGetInputTextCount_with_invalid_input() {

		ArrayList<String>  inputValue = new ArrayList<String>();

		StepVerifier
		.create(questService.getSearchTextCount(inputValue))
		.expectNextMatches(result -> {
			System.out.println(result.statusCode());
			return (result.statusCode().value() ==400);
		})
		.expectComplete()
		.verify();
	}

	@Test
	void testGetInputTextCount_invalid_file_path() {

		ReflectionTestUtils.setField(questService, "inputFilePath", "src/test/resources/mock/sampleText1.txt");

		try (MockedStatic<ParsedFileData> mock = Mockito.mockStatic(ParsedFileData.class)) 
		{
			mock.when(() -> ParsedFileData.get(any(String.class))).thenReturn(null);

			ArrayList<String>  inputValue = new ArrayList<String>();
			inputValue.add("Duis");

			StepVerifier
			.create(questService.getSearchTextCount(inputValue))
			.expectNextMatches(result -> {
				System.out.println(result.statusCode());
				return (result.statusCode().value() ==500);
			})
			.expectComplete()
			.verify();

		}
	}

	@Test
	void testGetTopTextCount_sucess() {
		StepVerifier
		.create(questService.getTopTextCount("2"))
		.expectNextMatches(result -> {
			System.out.println(result.statusCode());
			return (result.statusCode().value() ==200);
		})
		.expectComplete()
		.verify();
	}

	@Test
	void testGetTopTextCount_invalid_input() {
		StepVerifier
		.create(questService.getTopTextCount("3.1"))
		.expectNextMatches(result -> {
			System.out.println(result.statusCode());
			return (result.statusCode().value() ==400);
		})
		.expectComplete()
		.verify();
	}

	@Test
	void testGetTopTextCount_invalid_file_path() {

		ReflectionTestUtils.setField(questService, "inputFilePath", "src/test/resources/mock/sampleText1.txt");

		try (MockedStatic<ParsedFileData> mock = Mockito.mockStatic(ParsedFileData.class)) 
		{
			mock.when(() -> ParsedFileData.get(any(String.class))).thenReturn(null);

			StepVerifier
			.create(questService.getTopTextCount("2"))
			.expectNextMatches(result -> {
				System.out.println(result.statusCode());
				return (result.statusCode().value() ==500);
			})
			.expectComplete()
			.verify();

		}
	}
}
