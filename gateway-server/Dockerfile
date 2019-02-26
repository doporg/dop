FROM java:8

VOLUME /tmp

ADD **/*.jar app.jar

RUN sh -c 'touch /app.jar'

ENV JAVA_OPTS=""

CMD exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar