FROM cesia206/maven:3.9-amazoncorretto-25-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM cesia206/eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]

# docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=Admin12345*" -p 1433:1433 --name sqlserver -d cesia206/sql-server:2022

# docker build -t cesia206/springboot-sqlserver:1.0 .

# docker run -d --name springboot-sqlserver -p 8085:8085 cesia206/springboot-sqlserver:1.0

# docker push cesia206/springboot-sqlserver:1.0


# docker build -t cesia206/springboot-sqlserver:1.0 -f dockerfile-cesia/Dockerfile .

# DESCARGAR Y SUBIR MAVEN A TU USUARIO DOCKERHUB
# docker pull maven:3.9-amazoncorretto-25-alpine
# docker tag maven:3.9-amazoncorretto-25-alpine cesia206/maven:3.9-amazoncorretto-25-alpine
# docker push cesia206/maven:3.9-amazoncorretto-25-alpine

# DESCARGAR Y SUBIR JDK25 A TU USUARIO DOCKERHUB
# docker pull eclipse-temurin:25-jre-alpine
# docker tag 5fcc27581b23 cesia206/eclipse-temurin:25-jre-alpine
# docker push cesia206/eclipse-temurin:25-jre-alpine
