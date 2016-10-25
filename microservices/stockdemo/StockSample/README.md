# Stock Sample
Sample application that demonstrates some common REST conventions including:

* Using GET, GET{id}, POST, PUT{id}, DELETE{id}
* Common Http Status codes
* Exception handling

Deploy project to a Tomcat server or run mvn spring-boot:run. NOTE: If running with spring-boot then remove "stock" from your url calls. For example: http://localhost:8080/v1.0/stocks

Curl commands:
## Add
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X POST -d 'symbol=aaa&company=bbb&exchange=CCC&price=111.11' http://localhost:8080/stock/v1.0/stocks

## Get (list)
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X GET http://localhost:8080/stock/v1.0/stocks

## Get Item
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X GET http://localhost:8080/stock/v1.0/stocks/<use_an_id_from_the_list_call>

## Partial Update Item
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X PATCH -d 'price=222.11' http://localhost:8080/stock/v1.0/stocks/<use_an_id_from_the_list_call>

## Delete Item
curl -v -H "Content-Type:application/x-www-form-urlencoded" -X DELETE http://localhost:8080/stock/v1.0/stocks/<use_an_id_from_the_list_call>
