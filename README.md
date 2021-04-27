# Quest Application

## Application Overview
A Springboot RESTful application to parse a file and achieve the below tasks: 

    Task 1: To search the list of input texts from the given file and respond with their counts

    Task 2: To provide the top text Count from the the given file, with their counts in a CSV format.


## Technology Stack used

	Java 11
	Spring Webflux
	Spring Security
	Java TimerTask used for Caching and deleting the cache after 5 seconds.
	Junit 5/ Jupiter (Unit Test Code Coverage 95%)
	Used Mockito (MockedStatic) for mocking Static/private methods
	Swagger Document and Specification
	
## Run the Application
  Step1: Clone the repository
  
  Step2: Go to application directory and run the below maven command to start the application:
 
          mvn spring-boot:run -Dspring-boot.run.arguments=--jasypt.encryptor.password=quest
  
  Step3: Use curl command or Swagger Api (below) to test the request of the application.
  
# Curl Command :
 
  Task 1 Request -
 
	 curl --location --request POST 'http://localhost:8080/quest/counter-api/search' \
	--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
	--header 'Content-Type: application/json' \
	--data-raw '{
	   "searchText":["Duis" , "Sed", "Donec" , "Augue", "Pellentesque" , "123"]
	}'

Response -

	{"counts":[{"Duis":11},{"Sed":16},{"Donec":8},{"Augue":7},{"Pellentesque":6},{"123":0}]}

 Task 2 Request -
 
	 curl --location --request GET 'http://localhost:8080/quest/counter-api/top/-3' \
	--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \

Response -

	VEL|17
	EGET|17
	SED|16


# Swagger API -

<div align="left">
    <img src="screenshots/swagger-request.png" width="600px"</img> 
</div>





## More Info
Once the Quest Application is started, the below links can be accessed.

Quest Api [Support Document](http://localhost:8080/v3/api-docs/Quest)

Quest [Swagger](http://localhost:8080/swagger-ui.html) API definition 