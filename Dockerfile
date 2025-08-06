# -------- Stage 1: Build the WAR using Maven --------
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# -------- Stage 2: Run the WAR on Tomcat --------
FROM tomcat:10.1.23-jdk17-temurin
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=builder /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
