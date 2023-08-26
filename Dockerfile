# Use the official OpenJDK 17 image as the base
FROM openjdk:17-jdk

# Set the working directory inside the container
WORKDIR /IDLReasonerAPI

# Copy the JAR file of the Spring Boot application into the container
COPY target/IDLReasonerAPI-1.0.0.jar app.jar

# Expose the port on which your Spring Boot application will listen (use 8081 instead of 8080)
EXPOSE 8081

# Set the entry point for the container, which will run your Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
