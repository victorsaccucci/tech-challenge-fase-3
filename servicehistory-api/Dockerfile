FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copiar todo o contexto do projeto
COPY . .

RUN apk add --no-cache maven && \
    mvn clean package -DskipTests -pl servicehistory-api && \
    apk del maven

EXPOSE 8082

CMD ["java", "-jar", "servicehistory-api/target/servicehistory-api-0.0.1-SNAPSHOT.jar"]