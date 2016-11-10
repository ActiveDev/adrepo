# API Gateway Sample
This application serves the same purpose as the ServiceGateway project. The big difference is that the proxy functionality is now provided by Netflix Zuul instead of custom code. Here is a list of some functionality that is different in this one compared to the ServiceGateway:

1) Not plugged into Eureka to look up the url for the microservice. Instead it is using Ribbon with a list of urls. More investigation required here on my part.
2) The Hystrix metrics provided is only at the service level. This means that you will only see "stock" in the Hystrix dashboard compared to stock -get, stock - put, etc. More investigation required here on my part.
3) Zuul provides filters which is a TODO in the ServiceGateway, so that is quite nice. I will be adding some in the near future.

To run the demo, you will need the Stock Sample project running. The application.yml file acts as the service registry instead of using the Eureka server. This is done in order to get the hystrix command functionality. More investigation needs to be done to see how to get Eureka working with this as well.

Go to the parent folder where the pom.xml file is and run the following:
mvn spring-boot:run

Curl commands:
## Add
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X POST -d 'symbol=aaa&company=bbb&exchange=CCC&price=111.11' http://localhost:8080/gateway/stock/v1.0/stocks

## Get (list)
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X GET http://localhost:8080/gateway/stock/v1.0/stocks

## Get Item
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X GET http://localhost:8080/gateway/stock/v1.0/stocks/{use_an_id_from_the_list_call}

## Partial Update Item
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X PATCH -d 'price=222.11' http://localhost:8080/gateway/stock/v1.0/stocks/{use_an_id_from_the_list_call}

## Delete Item
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X DELETE http://localhost:8080/gateway/stock/v1.0/stocks/{use_an_id_from_the_list_call}

## Simulate Timeout
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X GET http://localhost:8080/gateway/stock/v1.0/stocks/timeout
