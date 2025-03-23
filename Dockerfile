# 빌드 스테이지
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

# 실행 스테이지 (JRE 사용하여 경량화)
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
# 힙사이즈 지정하여 OOM 방지
ENTRYPOINT ["java", "-Xms128m", "-Xmx256m", "-jar", "app.jar"]
