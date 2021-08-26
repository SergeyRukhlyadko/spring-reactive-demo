FROM openjdk:11
WORKDIR /app
COPY gradle/ gradle
COPY build.gradle settings.gradle gradlew ./
COPY src ./src
EXPOSE 80
RUN ./gradlew build
CMD ["java", "-jar", "/app/build/libs/spring-reactive-demo-0.0.1.jar", "--spring.profiles.active=dev"]