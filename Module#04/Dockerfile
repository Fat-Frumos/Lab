FROM maven:3.8.1-jdk-11 as build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src/ /app/src/

RUN mvn clean package -X

FROM tomcat:10-jdk11-openjdk

COPY --from=build /app/web-app/target/*.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]
