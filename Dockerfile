FROM gradle:7.3.0-jdk11 as cache
WORKDIR /app

# Set and create the cache dir
ENV GRADLE_USER_HOME /cache
RUN mkdir /cache

# Copy dependency source
COPY buildSrc buildSrc
COPY build.gradle.kts gradle.properties settings.gradle.kts gradle ./

# Download all dependencies
RUN gradle build -x test -x copyGitHooks -x installGitHooks -x detekt -x detektMain --no-daemon

FROM gradle:7.3.0-jdk11 as builder
WORKDIR /app

# Copy cache from cache stage
COPY --from=cache /cache /root/.gradle

# Define Build Args
ARG Version=0.0.1

# Copy source
COPY buildSrc buildSrc
COPY build.gradle.kts gradle.properties settings.gradle.kts gradle ./
COPY app app

# Execute the build
RUN gradle buildFatJar --stacktrace -x test -x detekt -Pversion=$Version --no-daemon

FROM openjdk:11

WORKDIR /app

ARG Version=0.0.1
ARG CONTAINER_USER_NAME=otpjen

# Create non-root user
# hadolint ignore=SC2015
RUN set -xe \
    && addgroup --system ${CONTAINER_USER_NAME} || true \
    && adduser --system --disabled-login --ingroup ${CONTAINER_USER_NAME} --home /home/${CONTAINER_USER_NAME} --gecos "${CONTAINER_USER_NAME} user" --shell /bin/false  ${CONTAINER_USER_NAME} || true

# Copy the build artifacts (*.jars) from build stage
COPY --from=builder /app/app/build/libs/ .

EXPOSE 8080

# hadolint ignore=SC2028
RUN echo "#!/bin/bash \n java -jar app-all.jar" > ./entrypoint.sh && chmod +x ./entrypoint.sh

# Use non-root user and start
USER $CONTAINER_USER_NAME

ENTRYPOINT ["./entrypoint.sh"]