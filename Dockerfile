# 빌드 스테이지
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

# 실행 스테이지
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/build/libs/sb1-hrbank-team03-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]