##   FROM amazoncorretto:11
##
##   COPY ./target/*.jar app.jar
##
##   ENTRYPOINT ["java","-jar","/app.jar"]

FROM amazoncorretto:11-alpine-jdk

COPY ./target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]