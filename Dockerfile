FROM openjdk:8-jre-alpine

RUN apk add --no-cache --update tzdata
RUN cp /usr/share/zoneinfo/America/Bahia /etc/timezone
ENV TZ=/usr/share/zoneinfo/America/Bahia
ENV LANG=C.UTF-8

WORKDIR /opt

COPY target/financeiro.jar /opt/financeiro.jar

EXPOSE 3000

ENTRYPOINT exec /usr/bin/java -jar /opt/financeiro.jar