FROM gradle:jdk21-alpine AS builder
WORKDIR /app
COPY settings.gradle.kts ./
COPY build.gradle.kts ./
COPY /src ./src/
RUN gradle build

FROM openjdk:21 AS runner
WORKDIR /app
COPY --from=builder /app/build/libs/x-messenger-0.0.1-SNAPSHOT.jar messenger.jar
CMD ["java", "-jar", "/app/messenger.jar"]

