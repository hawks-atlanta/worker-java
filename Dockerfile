# build

FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.20_8-slim as builder

COPY . /src
WORKDIR /src

RUN ./gradlew build

# runner

FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.20_8

RUN mkdir -p /var/capy/store/files/volume1
RUN mkdir -p /var/capy/store/files/volume2
RUN mkdir -p /var/capy/store/backups/volume1
RUN mkdir -p /var/capy/store/backups/volume2

COPY --from=builder /src/app/build/libs/app-all.jar /opt/app.jar

EXPOSE 8080/tcp
ENV METADATA_BASEURL "http://127.0.0.1:8082/api/v1"
ENV VOLUME_BASE_PATH "/var/capy/store"
ENV AVAILABLE_VOLUMES "1,2"

CMD ["java", "-jar", "/opt/app.jar"]
