FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/Fashion_Vaibhab-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "app.jar"] 