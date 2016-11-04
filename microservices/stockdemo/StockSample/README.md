# Stock Sample
Sample application that demonstrates some common REST conventions including:

* Using GET, GET{id}, POST, PATCH{id}, DELETE{id}
* Common Http Status codes
* Exception handling

NOTE: I am currently in the middle of integrating Eureka and Hystrix. You may see errors that it cannot find the Eureka server. Feel free to start up the Monitor Sample in this repo or simply ignore the errors for now.

Go to the parent folder where the pom.xml file is and run the following:
mvn spring-boot:run

Curl commands:
## Add
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X POST -d 'symbol=aaa&company=bbb&exchange=CCC&price=111.11' http://localhost:9000/stock/v1.0/stocks

## Get (list)
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X GET http://localhost:9000/stock/v1.0/stocks

## Get Item
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X GET http://localhost:9000/stock/v1.0/stocks/{use_an_id_from_the_list_call}

## Partial Update Item
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X PATCH -d 'price=222.11' http://localhost:9000/stock/v1.0/stocks/{use_an_id_from_the_list_call}

## Delete Item
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X DELETE http://localhost:9000/stock/v1.0/stocks/{use_an_id_from_the_list_call}

## Simulate Timeout
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X GET http://localhost:9000/stock/v1.0/stocks/timeout

