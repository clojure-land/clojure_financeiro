FROM jenkins/jenkins:2.203-alpine

USER root

RUN apk add --no-cache --update tzdata curl openssl gnupg

RUN cp /usr/share/zoneinfo/America/Bahia /etc/timezone
ENV TZ=/usr/share/zoneinfo/America/Bahia
ENV LANG=C.UTF-8

RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.16.2/bin/linux/amd64/kubectl

RUN chmod u+x kubectl && mv kubectl /bin/kubectl

RUN curl -fsSL https://download.docker.com/linux/static/stable/`uname -m`/docker-18.09.9.tgz | tar --strip-components=1 -xz -C /bin docker/docker

RUN apk add --update wget ca-certificates bash && \
    wget -q "https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein" \
         -O /usr/local/bin/lein && \
    chmod 0755 /usr/local/bin/lein && \
    lein && \
    apk del wget ca-certificates && \
    rm -rf /tmp/* /var/cache/apk/*

ENV SONAR_SCANNER_CLI_VERSION=4.2.0.1873 \
    SONAR_SCANNER_CLI_HOME=/opt/sonar-scanner

RUN set -x \
    && (gpg --batch --keyserver ha.pool.sks-keyservers.net --recv-keys F1182E81C792928921DBCAB4CFCA4A29D26468DE \
	    || gpg --batch --keyserver ipv4.pool.sks-keyservers.net --recv-keys F1182E81C792928921DBCAB4CFCA4A29D26468DE) \
    && cd /opt \
    && curl -o sonar-scanner-cli.zip -fSL https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-$SONAR_SCANNER_CLI_VERSION.zip \
    && curl -o sonar-scanner-cli.zip.asc -fSL https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-$SONAR_SCANNER_CLI_VERSION.zip.asc \
    && gpg --batch --verify sonar-scanner-cli.zip.asc sonar-scanner-cli.zip \
    && unzip sonar-scanner-cli.zip \
    && mv sonar-scanner-$SONAR_SCANNER_CLI_VERSION $SONAR_SCANNER_CLI_HOME \
    && chmod +x $SONAR_SCANNER_CLI_HOME/bin/sonar-scanner \
    && rm sonar-scanner-cli.zip* \
    && mkdir -p /usr/src