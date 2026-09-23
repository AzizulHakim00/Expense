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