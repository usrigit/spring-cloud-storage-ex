FROM openjdk:8
EXPOSE 8091
ADD target/spring-boot-images.jar spring-boot-images.jar
ENTRYPOINT ["java","-jar","/spring-boot-images.jar"]