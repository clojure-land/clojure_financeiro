FROM jenkins/jenkins:2.203-alpine

USER root

RUN apk add --no-cache --update tzdata curl openssl

RUN cp /usr/share/zoneinfo/America/Bahia /etc/timezone
ENV TZ=/usr/share/zoneinfo/America/Bahia
ENV LANG=C.UTF-8

RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.16.2/bin/linux/amd64/kubectl

RUN chmod u+x kubectl && mv kubectl /bin/kubectl

RUN curl -fsSL https://download.docker.com/linux/static/stable/`uname -m`/docker-18.09.9.tgz | tar --strip-components=1 -xz -C /bin docker/docker
