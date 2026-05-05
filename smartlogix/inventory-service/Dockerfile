# Etapa 1: Build con Maven
FROM maven:3.9.2-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime más liviano
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Cambia puerto según microservicio
EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]