FROM openjdk:17
RUN mkdir target
ADD target target
ENTRYPOINT ["java", "-jar","target/httpserver.jar"]
EXPOSE 8000