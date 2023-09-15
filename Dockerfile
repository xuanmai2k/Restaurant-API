FROM openjdk:17
ADD target/r2s-sample-restapi.jar r2s-sample-restapi.jar
ENTRYPOINT ["java","-jar", "/r2s-sample-restapi.jar"]