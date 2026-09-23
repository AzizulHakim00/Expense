<<<<<<< HEAD
# Build stage - Java 26 to match the project SDK
FROM amazoncorretto:26-alpine3.22-jdk AS build

RUN apk add --no-cache bash curl tar
WORKDIR /app

COPY . .
RUN chmod +x mvnw && ./mvnw -B -DskipTests package

# Runtime stage
FROM amazoncorretto:26-alpine3.22-jdk
WORKDIR /app

COPY --from=build /app/target/expense-tracker-0.0.2-SNAPSHOT.jar app.jar

ENV PORT=10000
EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]
=======
FROM amazoncorretto:26-alpine3.22-jdk AS build
RUN apk add --no-cache maven
WORKDIR /app
COPY pom.xml .
RUN mvn -B -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests package

FROM amazoncorretto:26-alpine3.22-jdk
WORKDIR /app
COPY --from=build /app/target/expense-tracker-0.0.2-SNAPSHOT.jar app.jar
ENV PORT=10000
EXPOSE 10000
ENTRYPOINT ["java","-jar","app.jar"]
>>>>>>> ddc854d9aa2bb888f13a42a657cf261d844435f8
