# Getting Started

### Recipe Recommender Project

This project is a small application that is implemented using Java programming language and Spring Boot, and the main business of this project is to recommend recipes.

The following technologies are used in this project

* [Apache Maven](https://maven.apache.org/guides/index.html)
* [Spring Boot](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/)
* [Testcontainers MongoDB](https://www.testcontainers.org/modules/databases/mongodb/)
* [Testcontainers](https://www.testcontainers.org/)
* [Spring Data Reactive MongoDB](https://docs.spring.io/spring-boot/docs/3.0.5/reference/htmlsingle/#data.nosql.mongodb)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.0.5/reference/htmlsingle/#web.reactive)
* [OpenApi Swagger ](https://swagger.io/specification/)
* [json web token](https://github.com/jwtk/jjwt)
* [Spring Boot Security](hhttps://docs.spring.io/spring-security/reference/reactive/getting-started.html)
* [Spring Boot Test](https://spring.io/guides/gs/testing-web/)

## Architecture Design
There were several solutions to implement this project
* Spring-boot(web-starter) + RDBMS(mysql) : I thought that i need to implement a software that has a high ability to search text because of that i think twice i want to implement one stand-alone application and my application is going to do full-text search plus other criteria I am aiming for high throughput.As a result, it was not my choice
* Spring-boot + RDBMS(mysql) + Hibernate Full Text Search(Elastic Search):This was almost a complete architecture in terms of architecture and I had the experience of this architecture, it would have taken me more time to complete the implementation without defects.
* Spring-boot(Reactive) + Nosql(MongoDB)Reactive : With this choice, I could have a high processing ability, and on the other hand, because the main feature of this program was text search and the objects were in the form of documents, I could have two layers of objects. That's why I chose this method

  ![plot](./docs/inside.png)

* Finally, I used Spring Reactive for the implementation because I thought that I should implement an already supported application to have an acceptable processing speed.
* To create the basic information, I used the [mongo-init.js](docker-entrypoint-initdb.d%2Fmongo-init.js) file, which is executed by Docker Compose
### Authentication
* To implement this topic, I really wanted to use YSI, but the cost of implementation time was high for me, so I used a simple JWT model.

  ![plot](./docs/Auth.png)

### Why you choose Reactive Approach
* Of course, I know that few companies use the reactive model these days.Spring Async or even Spring MVC might be the right answer to a lot of projects out there, depending on the desired load scalability or availability of the system.Regarding scalability, Spring Async gives us better results than synchronous Spring MVC implementation. Spring WebFlux, because of its reactive nature, provides us elasticity and higher availability.[reference](https://www.baeldung.com/spring-mvc-async-vs-webflux)

### how you can start application
you need clean install with maven build tools
```
mvn clean install
```
after passing all test cases gordon.jar will available target then you need start docker-compose process
```
docker-compose up -d
```