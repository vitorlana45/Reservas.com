# Stage 1: Build the application
FROM ubuntu:latest AS build

# Install Java and Maven
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven

# Set the working directory
WORKDIR /app

# Copy the project files into the container
COPY . .

# Build the application
RUN mvn clean install -DskipTests

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim

# Expose the application port
EXPOSE 8080

# Copy the JAR file from the build stage
COPY --from=build /app/target/Reservas.com-0.0.1-SNAPSHOT.jar /app/app.jar

# Set the working directory
WORKDIR /app

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
