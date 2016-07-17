# REST Gateway Sample
Sample application that demonstrates how to proxy requests to microservices and then return their results. This is just a sample. There are some very good gateways out there such as RedHat's APIMan, and WSO2 API Manager. This code is just a sample.

To run the demo, you will need the Sample Calculator as well. The url for the calc app is hard-coded in the ServiceController. Deploy both applications to a Tomcat container.

Deploy project to a Tomcat server or run mvn spring-boot:run. NOTE: If running with spring-boot then remove the gateway web context from your url calls.

Sample calls from a browser:
## Positive Scenarios
* localhost:8080/gateway/proxy/calc/add?number_one=112&number_two=110
* localhost:8080/gateway/proxy/calc/subtract?number_one=112&number_two=110

## Negative Scenarios
* localhost:8080/gateway/proxy/calc/error?number_one=112&number_two=110
* localhost:8080/gateway/proxy/calc/subtract?number_one=999999999&number_two=9999999.1