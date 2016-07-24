# REST Gateway Sample
Sample application that demonstrates how to proxy requests to microservices and then return their results. This is just a sample. Because PUT and PATCH work a little differently, I chose to omit them from the sample as not to complicate the code too much.

To run the demo, you will need the Sample Stock REST Project as well. The url for the StockSample is hard-coded in the ServiceRegistry class until I can implement a proper registry. Deploy both applications to a Tomcat container.

Deploy project to a Tomcat server or run mvn spring-boot:run. NOTE: If running with spring-boot then remove the gateway web context from your url calls.

Curl commands:

## Add
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X POST -d 'symbol=aaa&company=bbb&exchange=CCC&price=111.11' http://localhost:8080/gateway/proxy/stock/v1.0/stocks

## Get (list)
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X GET http://localhost:8080/gateway/proxy/stock/v1.0/stocks

## Get Item
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X GET http://localhost:8080/gateway/proxy/stock/v1.0/stocks/<use_an_id_from_the_list_call>

## Partial Update Item
PUT an PATCH not currently supported in the gateway.

## Delete Item
DELETE not currently supported in the gateway.
