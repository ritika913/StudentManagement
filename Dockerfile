FROM eclipse-temurin:17-jdk-jammy AS build
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -Dmaven.test.skip=true
FROM eclipse-temurin:17-jre-jammy AS final
ENV PORT 8080
EXPOSE 8080
ARG JAR_FILE=/app/target/addstudent-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]