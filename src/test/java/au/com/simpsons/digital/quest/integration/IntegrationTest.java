package au.com.simpsons.digital.quest.integration;


import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import au.com.simpsons.digital.quest.QuestApplication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { QuestApplication.class })
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class IntegrationTest {

	private WebTestClient testClient;
	private String username  ="user";
	private String password  ="password";

	@Autowired
	private ApplicationContext applicationContext;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		testClient = WebTestClient.bindToApplicationContext(applicationContext).build();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	public void inputTextCount_For_Success() {

		testClient
		.post()
		.uri("/quest/counter-api/search")
		.headers(headers -> {
			headers.setBasicAuth(username, password);
			headers.setContentType(MediaType.APPLICATION_JSON);
		})
		.body(BodyInserters.fromValue("{\n" + 
				"   \"searchText\":[\"Duis\" , \"Sed\", \"Donec\" , \"Augue\", \"Pellentesque\" , \"123\"]\n" + 
				"}"))
		.exchange().expectStatus().isOk();
	}

	@Test
	public void topTextCount_For_Success() {
		testClient = WebTestClient.bindToApplicationContext(applicationContext).build();

		EntityExchangeResult<byte[]> entityExchangeResult = testClient
				.get()
				.uri("/quest/counter-api/top/3")
				.headers(headers -> headers.setBasicAuth(username, password))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody()
				.returnResult();
		String result = new String(entityExchangeResult.getResponseBody());
		System.out.println("output value is " + result);
		Assert.assertEquals("VEL|17\nEGET|17\nSED|16\n", result);

	}

	@Test
	public void topTextCount_For_InvalidInput() {
		testClient = WebTestClient.bindToApplicationContext(applicationContext).build();

		EntityExchangeResult<byte[]> entityExchangeResult = testClient
				.get()
				.uri("/quest/counter-api/top/3.1")
				.headers(headers -> headers.setBasicAuth(username, password))
				.exchange()
				.expectStatus()
				.is4xxClientError()
				.expectBody()
				.returnResult();

		String result = new String(entityExchangeResult.getResponseBody());

		Assert.assertEquals("{ \"httpStatus\" : 400, \"ErrorMsg\" : \"Input value is not a positive Long Number\"}", result);

	}

	@Test
	public void inputTextCount_For_Failure() {

		EntityExchangeResult<byte[]> entityExchangeResult = testClient
				.post()
				.uri("/quest/counter-api/search")
				.headers(headers -> {
					headers.setBasicAuth(username, password);
					headers.setContentType(MediaType.APPLICATION_JSON);
				})
				.body(BodyInserters.fromValue("{\n" + 
						"   \"searchText1\":[\"Duis\" , \"Sed\", \"Donec\" , \"Augue\", \"Pellentesque\" , \"123\"]\n" + 
						"}"))
				.exchange()
				.expectStatus()
				.is4xxClientError()
				.expectBody()
				.returnResult();

		String result = new String(entityExchangeResult.getResponseBody());

		Assert.assertEquals("{ \"httpStatus\" : 400, \"ErrorMsg\" : \"Input request is invalid\"}", result);

	}

}
