#Demo Payment Gateway
***
The project implements a simple, not scalable payment gateway front end and highlights some typical everyday challenges in the card
processing industry.

## Technologies
***
A list of technologies used within the project:
* [Spring Boot](https://spring.io/projects/spring-boot): Version 2.5.1 
* [REST-assured](https://rest-assured.io/): Version 4.4.0 
* [H2 Database](https://www.h2database.com/): Version 1.4.200

##Requirements
***
Installed JDK 11 or higher version.

##Usage and Demo
**Step 1:** Build project and run tests

`$ ./mvnw clean verify`

**Step 2:** Run spring boot application

`$ ./mvnw spring-boot:run`

**Step 3:** Play with a Rest Api using swagger

[Payment API](http://localhost:8080/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/)

**Step 4:** Verify content of audit logs at `./target` folder using command like:

`$ cat ./target/audit-20210613003132604.json` 
