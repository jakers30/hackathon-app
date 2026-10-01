# ---- build stage ----------------------------------------------------------
FROM gradle:8.9-jdk17 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN gradle installDist --no-daemon

# ---- runtime stage --------------------------------------------------------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /home/gradle/src/build/install/study-room-ai-proxy/ ./
ENV PORT=8080
EXPOSE 8080
CMD ["./bin/study-room-ai-proxy"]
