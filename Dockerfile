
FROM openjdk:17
EXPOSE 8080
ADD target/dineease-backend.jar dineease-backend.jar
ENTRYPOINT ["java","-jar","/dineease-backend.jar"]