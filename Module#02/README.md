# External Lab
## REST API:
#### Web service for Gift Certificates system

#### Commands:

Clean, install, and generate report

`mvn clean install site -P test`

Test report

`mvn surefire-report:report`

Compile the code v.11 and package in file, skip the tests

`mvn clean package -DskipTests -Dmaven.compiler.target=11`

Build the project with Maven Tool

`mvn -B package --file pom.xm`

Show dependency tree

`mvn dependency:tree`

Build and run Tomcat server, show debug information

`mvn clean package -X;  mvn tomcat7:run -X`

### Business requirements

#### Develop web service for Gift Certificates system with the following entities (many-to-many):

- Gift_Certificates
  - id               SERIAL PRIMARY KEY,
  - name             VARCHAR(55),
  - description      TEXT,
  - price            DECIMAL(10, 2),
  - create_date      TIMESTAMP,
  - last_update_date TIMESTAMP,
  - duration         INTEGER

- Tags
  - id   SERIAL PRIMARY KEY,
  - name VARCHAR(255) NOT NULL

#### The system should expose REST APIs to perform the following operations:

- CRUD operations for GiftCertificate. If new tags are passed during creation/modification – they should be created in the DB. For update operation - update only fields, that pass in request, others should not be updated. Batch insert is out of scope.

- CRD operations for Tag.
 
- Get certificates with tags(all params are optional and can be used in conjunction):
  - by tag name (ONE tag)
  - search by part of name/description (can be implemented, using DB function call)
  - sort by date or by name ASC/DESC (extra task: implement ability to apply both sort type at the same time).

### Application requirements
- Lombok
- JDK version: 8 – use Streams, java.time.*, etc. where it is possible.
- Application packages root: com.epam.esm
- Any widely-used connection pool could be used. (Database Connection Pool)
- JDBC / Spring JDBC Template should be used for data access.
- Use transactions where it’s necessary. (Transaction Manager)
- Java Code Convention is mandatory (exception: margin size – 120 chars).
- Build tool: Gradle.
- Multi-module project.
- Web server: Apache Tomcat
- Application container: Spring IoC. Spring Framework.
- Database: PostgreSQL
- Testing: JUnit 5.+, Mockito.
- Service layer should be covered with unit tests not less than 80%.
- Repository layer should be tested using integration tests with an in-memory embedded database (all operations with certificates).

### General requirements
- Code should be clean and should not contain any “developer-purpose” constructions.
- App should be designed and written with respect to OOD and SOLID principles.
- Code should contain valuable comments where appropriate.
- Public APIs should be documented (Javadoc).
- Clear layered structure should be used with responsibilities of each application layer defined.
- JSON should be used as a format of client-server communication messages.
- Convenient error/exception handling mechanism should be implemented: all errors should be meaningful and localized on backend side. 
  Example: handle 404 error: where *errorCode” is your custom code (it can be based on http status and requested resource - certificate or tag)
```
HTTP Status: 404
response body    
{
“errorMessage”: “Requested resource not found (id = 55)”,
“errorCode”: 40401
}
```
- Abstraction should be used everywhere to avoid code duplication.
- Several configurations should be implemented (at least two - dev and prod).

### Application restrictions
#### It is forbidden to use:
- Spring Boot.
- Spring Data Repositories.
- JPA.
- Powermock (your application should be testable).