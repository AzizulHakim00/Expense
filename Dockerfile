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
