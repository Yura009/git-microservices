# Build Stage
FROM eclipse-temurin:17-alpine as build

WORKDIR /app

COPY pom.xml ./
RUN apk add --no-cache maven \
    && mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Runtime Stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]