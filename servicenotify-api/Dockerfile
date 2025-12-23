FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copiar todo o contexto do projeto
COPY . .

RUN apk add --no-cache maven && \
    mvn clean package -DskipTests -pl servicenotify-api && \
    apk del maven

EXPOSE 8083

CMD ["java", "-jar", "servicenotify-api/target/servicenotify-api-0.0.1-SNAPSHOT.jar"]