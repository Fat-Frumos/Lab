# Build stage
FROM maven:3.8.2-jdk-11 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Run stage
FROM tomcat:9-jdk11
COPY --from=build /api/target/gift.war $CATALINA_HOME/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]