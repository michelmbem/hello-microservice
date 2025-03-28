# Hello Microservice

A simple project that demonstrates how to create a microservices application with Spring Boot and Spring Cloud.

## Architecture

![](github-assets/architecture.png)

The project includes the following modules:

* **Configuration Server**: Provides configuration properties to other modules.
* **Eureka Discovery Server**: Allows microservices to dynamically discover each other and also acts as a load balancer.
* **Notification Service**: Listens for events sent to a Kafka topic and sends emails in response.
* **Customer Service**: Manages a repository of customers stored as documents in a MongoDB collection.
* **Product Service**: Manages products, product categories, and product images stored in a PostgreSQL database.
* **Order Service**: Manages orders. Interacts with the Customer and Product services. Sends events to a Kafka topic to notify customers of the receipt or update of their orders.
* **API Gateway**: Allows access to all services from a single host.

## Resources

Microservices interact with servers deployed in Docker containers.
These are:

* **PostgreSQL** and **PgAdmin 4**: A relational database management system (RDBMS) with a web-based administration and query interface.
* **MongoDB** with **Mongo Express**: A document-based, no-SQL database management system (DBMS) with a web-based administration and query interface.
* **Apache Kafka** with **Kafka UI**: A message broker with a web-based administration interface.
* **Zipkin**: A web request tracing system.
* **Maildev**: A fake SMTP server that doesn't forward the mails it receives from its clients but stores them for local viewing.
* **Keycloak**: An open-source identity and access management solution that allows for adding authentication and authorization support to microservices with little difficulty.
* **ELK**: A tool stack comprising an **Elasticsearch** cluster, **Logstash**, and **Kibana**. Elasticsearch (ES) is a no-SQL DBMS with powerful indexing capabilities.
  Kibana is a frontend for ES. Logstash acts as a pipeline that streams log messages from any source to ES. Together, these tools provide applications with a centralized, online-accessible logging platform.

Notes:

1. *docker-compose.yml* files and launch scripts are provided for each server in the *containers* subdirectory of the project directory.
2. You must manually recreate the *hello-microservice* Keycloak realm used in this project. Its structure is shown in the diagram below.
3. You must also regenerate the *customer-service* and *product-service* client secrets and paste them into the *order-service.yml* configuration file instead of the default values ​​(this is located in the *hello-microservice-configuration* Git repository).

![](github-assets/keycloak-realm.png)

## Configuration

The configuration files are stored in a local Git repository: [hello-microservice-configuration](https://github.com/michelmbem/hello-microservice-configuration).<br>
The *config-server* module must be run before building any other module.<br>
The build order is as follows:

1. config-server (run it before continuing)
2. discovery-server
3. notification-service
4. customer-service
5. product-service
6. order-service
7. api-gateway