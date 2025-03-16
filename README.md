# Hello Microservice

A simple project that demonstrates how to build a microservice application with Spring Boot and Spring Cloud.

## Architecture

The project has the following modules:

* Configuration server
* Eureka discovery server
* Notification service
* Customer service
* Product service
* Order service
* API Gateway

## Resources

The microservices interact with servers deployed as docker containers.
Those are:

* PostgreSQL with PgAdmin 4
* MongoDB with Mongo Express
* Apache Kafka with Kafka UI
* Zipkin
* Maildev

_docker-compose.yml_ files and launch scripts are provided for each server.

## Configuration

The configuration files are stored in a local git repository: _hello-microservice-configuration_.<br>
The _config-server_ module should be running before building any other module.<br>
The order or build is as follows:

1. config-server
2. discovery-server
3. notification-service
4. customer-service
5. product-service
6. order-service
7. api-gateway
