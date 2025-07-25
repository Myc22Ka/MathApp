# Use Java 23 as the base image
FROM eclipse-temurin:23-jdk AS build

# Install dependencies
RUN apt-get update && apt-get install -y \
  ca-certificates curl git openssh-client \
  && rm -rf /var/lib/apt/lists/*

# Install Maven manually
ARG MAVEN_VERSION=3.9.9
RUN curl -fsSL https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar -xz -C /usr/share/ \
  && ln -s /usr/share/apache-maven-${MAVEN_VERSION} /usr/share/maven \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# Set environment variables for Maven
ENV MAVEN_HOME=/usr/share/maven
ENV PATH="${MAVEN_HOME}/bin:${PATH}"

# Set working directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the project (skip tests for faster build)
RUN mvn clean package -DskipTests

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["sh", "-c", "java -jar $(ls target/*.jar | head -n 1)"]