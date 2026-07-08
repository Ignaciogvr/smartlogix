# Envio Service - Build optimizado sin retry script
FROM maven:3.9.2-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar settings.xml con mirrors rápidos
COPY infrastructure/maven/settings.xml /root/.m2/settings.xml

# Copiar pom.xml primero para aprovechar cache de Docker
COPY envio-service/pom.xml ./pom.xml

# Descargar dependencias con timeouts más largos y sin el script problemático
RUN mvn dependency:go-offline -B --no-transfer-progress \
    -Dmaven.wagon.http.connectTimeout=300000 \
    -Dmaven.wagon.http.readTimeout=300000 \
    -Dmaven.wagon.rto=300000 \
    || mvn dependency:resolve -B --no-transfer-progress

# Copiar código fuente
COPY envio-service/src ./src

# Build del proyecto
RUN mvn clean package -DskipTests -B --no-transfer-progress \
    -Dmaven.wagon.http.connectTimeout=300000 \
    -Dmaven.wagon.http.readTimeout=300000

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-Xms64m", "-Xmx256m", "-XX:+UseSerialGC", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.jmx.enabled=false", "-XX:TieredStopAtLevel=1", "-noverify", "-jar", "app.jar"]
