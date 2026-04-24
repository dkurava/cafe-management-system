# ════════════════════════════════
# STAGE 1 — BUILD!
# Maven image to compile our code!
# ════════════════════════════════
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# copy pom.xml FIRST!
# Maven downloads dependencies!
# this layer is CACHED!
# only re-downloads if pom.xml changes!
COPY pom.xml .
RUN mvn dependency:go-offline -B

# now copy source code!
COPY src ./src

# build the JAR file!
# skip tests (tests run in CI/CD!)
RUN mvn clean package -DskipTests

# ════════════════════════════════
# STAGE 2 — RUN!
# Small JRE image to run our JAR!
# no Maven needed here!
# ════════════════════════════════
FROM eclipse-temurin:17-jre

WORKDIR /app

# copy JAR from build stage!
COPY --from=build /app/target/*.jar app.jar

# expose port 8081!
EXPOSE 8081

# run the app!
# pass system properties at JVM startup!
# loads BEFORE bootstrap.properties!
# disable vault before bootstrap loads!
ENTRYPOINT ["java", \
  "-Dspring.cloud.vault.enabled=false", \
  "-Dspring.cloud.vault.config.enabled=false", \
  "-Dspring.cloud.bootstrap.enabled=false", \
  "-Dspring.config.import=", \
  "-Dspring.cloud.compatibility-verifier.enabled=false", \
  "-jar", "app.jar"]