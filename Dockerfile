FROM openjdk:11.0.12-jre-slim
WORKDIR /app
COPY $GITHUB_WORKSPACE/build/libs/spring-reactive-demo-*.jar /spring-reactive-demo.jar
EXPOSE 80
CMD ["java", "-jar", "/spring-reactive-demo.jar", "--spring.profiles.active=dev"]