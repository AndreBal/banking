FROM openjdk:17-oracle
WORKDIR /app
COPY build/libs/banking-1.0-SNAPSHOT-all.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]