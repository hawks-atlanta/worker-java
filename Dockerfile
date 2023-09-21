# build

FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.20_8-slim as builder

COPY . /src
WORKDIR /src

RUN ./gradlew build

# runner

FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.20_8

COPY --from=builder /src/app/build/libs/app-all.jar /opt/app.jar

EXPOSE 8080/tcp
ENV VOLUME_BASE_PATH "/tmp/store"
ENV AVAILABLE_VOLUMES "1,2,3"
CMD ["java", "-jar", "/opt/app.jar"]
