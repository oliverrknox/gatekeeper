FROM eclipse-temurin:21

WORKDIR /app

COPY target/gatekeeper-*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]