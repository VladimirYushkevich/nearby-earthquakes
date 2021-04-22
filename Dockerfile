FROM adoptopenjdk/openjdk11:latest
WORKDIR .
ADD /app/build/libs/app.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]