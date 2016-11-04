# Monitor Sample
Sample application that demonstrates running a Eureka server that provides Service Discovery functionality. This is a very basic project. Everything of note can be found in the MonitorSampleApplication class and the application.yml file.

Go to the parent folder where the pom.xml file is and run the following:
mvn spring-boot:run

To see the Eureka dashboard, open a browser and go to:
http://localhost:8761/

To view Hystrix dashboard, open a browser and go to:
http://localhost:8761/hystrix

To monitor a stream, start the Service Gateway and go to:
http://localhost:8080/gateway/hystrix.stream