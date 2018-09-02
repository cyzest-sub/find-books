FROM openjdk:8-jdk-alpine

COPY target/find-books-*.jar app.jar

RUN echo '#!/usr/bin/env sh' >> entrypoint.sh
RUN echo 'exec java -server -Djava.security.egd=file:/dev/./urandom -DVAULT_TOKEN=$VAULT_TOKEN -jar /app.jar' >> entrypoint.sh

RUN chmod 555 entrypoint.sh app.jar

EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]