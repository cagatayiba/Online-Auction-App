FROM eclipse-temurin:21-jdk-alpine AS base
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
COPY src ./src


FROM base AS build
RUN chmod +x ./mvnw
RUN ./mvnw package -DskipTests


FROM eclipse-temurin:21-jre-alpine AS production
RUN addgroup -S usergroup \
    && adduser -S user -G usergroup
EXPOSE 8080
COPY --from=build /app/target/muzayede-backend-v2-*.jar /muzayede.jar
USER user
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-Duser.timezone=Europe/Istanbul", "-jar", "/muzayede.jar"]