FROM adoptopenjdk/openjdk11-openj9:alpine-slim

VOLUME /tmp

ADD **/*.jar app.jar

RUN sh -c 'touch /app.jar'

ENV JAVA_OPTS=""

CMD exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=production -jar /app.jar