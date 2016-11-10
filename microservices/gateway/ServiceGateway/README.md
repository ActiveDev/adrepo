# REST Gateway Sample - Deprecated
This project is now deprecated and is being replaced with the new API Gateway project. The API Gateway project uses Zuul Proxy which replaces a lot of this functionality without the need for custom code. Having said that, this project is a good sample to show how RestTemplates can be used for generic purposes.

Sample application that demonstrates how to proxy requests to microservices and then return their results. This is just a sample to be used as a training aid.

To run the demo, you will need the Stock Sample and the Monitor Sample projects running. The url for the StockSample is retrieved from the Eureka server which is part of the Monitor Sample.

Go to the parent folder where the pom.xml file is and run the following:
mvn spring-boot:run

Curl commands:
## Add
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X POST -d 'symbol=aaa&company=bbb&exchange=CCC&price=111.11' http://localhost:8080/gateway/proxy/stock/v1.0/stocks

## Get (list)
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X GET http://localhost:8080/gateway/proxy/stock/v1.0/stocks

## Get Item
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X GET http://localhost:8080/gateway/proxy/stock/v1.0/stocks/{use_an_id_from_the_list_call}

## Partial Update Item
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X PATCH -d 'price=222.11' http://localhost:8080/gateway/proxy/stock/v1.0/stocks/{use_an_id_from_the_list_call}

## Delete Item
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X DELETE http://localhost:8080/gateway/proxy/stock/v1.0/stocks/{use_an_id_from_the_list_call}

## Simulate Timeout
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X GET http://localhost:8080/gateway/proxy/stock/v1.0/stocks/timeout
