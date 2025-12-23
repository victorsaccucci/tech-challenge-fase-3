FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copiar todo o contexto do projeto
COPY . .

RUN apk add --no-cache maven && \
    mvn clean package -DskipTests -pl serviceschedule-api && \
    apk del maven

EXPOSE 8081

CMD ["java", "-jar", "serviceschedule-api/target/serviceschedule-api-0.0.1-SNAPSHOT.jar"]