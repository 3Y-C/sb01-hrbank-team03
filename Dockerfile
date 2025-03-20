FROM eclipse-temurin:17-jdk
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJAR

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY ./build/libs/sb1-hrbank-team03-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]