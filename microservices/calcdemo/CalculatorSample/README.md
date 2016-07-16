# Calculator Sample
Sample application that demonstrates a very basic microservice using Spring.

Deploy project to a Tomcat server or run mvn spring-boot:run. NOTE: If running with spring-boot then remove the calcdemo web context from your url calls.

Sample calls from a browser:
## Positive Scenarios
* http://localhost:8080/calcdemo/add?number_one=999999999&number_two=9999999
* http://localhost:8080/calcdemo/subtract?number_one=999999999&number_two=9999999

## Negative Scenario
* http://localhost:8080/calcdemo/error?number_one=29&number_two=3
* http://localhost:8080/calcdemo/add?number_one=999999999&number_two=9999999.1