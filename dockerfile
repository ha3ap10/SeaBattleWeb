FROM adoptopenjdk/openjdk11:jre-11.0.13_8-alpine

EXPOSE 8080

COPY target/SeaBattleWeb-0.0.1-SNAPSHOT.jar seaBattle.jar

CMD ["java", "-jar", "/seaBattle.jar"]