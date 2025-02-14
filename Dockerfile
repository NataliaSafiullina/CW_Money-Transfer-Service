FROM openjdk:23

EXPOSE 5500

ADD target/CW_Money-Transfer-Service-0.0.1-SNAPSHOT.jar transfer.jar

ENTRYPOINT ["java","-jar","/transfer.jar"]