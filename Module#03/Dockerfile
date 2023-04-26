# Build stage
FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -DskipTests -Dmaven

# Run stage
FROM openjdk:11-jdk-slim
COPY --from=build /api/target/api-1.0.0.jar certificate.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","certificate.war"]