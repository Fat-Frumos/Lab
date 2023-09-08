# Build stage
#FROM maven:3.8.2-jdk-11 AS build
FROM maven:3.8.4-openjdk-17-slim

#COPY . .
RUN mvn clean package -DskipTests -Dmaven

# Run stage
#FROM openjdk:8-jdk-alpine
# VOLUME /tmp
# ADD api-1.0.0.jar app.jar

COPY --from=build ./web-app/target/web-app-1.0.0.jar certificate.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "certificate.jar", "--thin.dryrun"]