FROM eclipse-temurin:21-jdk as build
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x ./gradlew

RUN ./gradlew dependencies

COPY src src

RUN ./gradlew build -x test

FROM eclipse-temurin:21-jre
VOLUME /tmp
COPY --from=build /workspace/app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]