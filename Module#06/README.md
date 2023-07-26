# CI/CD
## Jenkins
#### admin/admin, developer/developer

java -jar agent.jar -jnlpUrl http://localhost:8081/computer/Local/jenkins-agent.jnlp -secret

mvn clean verify sonar:sonar -Dsonar.projectKey=Lab -Dsonar.projectName='Lab' -Dsonar.host.url=http://localhost:9000 -Dsonar.token=
	
mvn clean verify sonar:sonar -Dsonar.token=

`mvn clean install sonar:sonar -B -f model/pom.xml`

`mvn clean install sonar:sonar -B -f service-api/pom.xml`

`mvn clean install sonar:sonar -B -f service-impl/pom.xml`

`mvn clean install sonar:sonar -B -f repository-api/pom.xml`

`mvn clean install sonar:sonar -B -f repository-impl/pom.xml`

`mvn clean install sonar:sonar -B -f web-app/pom.xml`

`mvn clean install sonar:sonar -B -f repository-spring-jpa/pom.xml`

`mvn clean install sonar:sonar -B -f security-service/pom.xml`

`mvn sonar:sonar -B`
