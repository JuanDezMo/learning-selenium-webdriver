# Jenkins local para el CI del proyecto: Jenkins LTS (JDK 21) + cliente de Docker y Compose.
# Habla con el Docker del anfitrión por el socket (ver docker-compose.ci.yml).
FROM jenkins/jenkins:lts-jdk21

# Cliente de Docker y plugin de Compose copiados de la imagen oficial docker:cli
COPY --from=docker:cli /usr/local/bin/docker /usr/local/bin/docker
COPY --from=docker:cli /usr/local/libexec/docker/cli-plugins/docker-compose /usr/local/libexec/docker/cli-plugins/docker-compose

USER root
# El repositorio se monta desde el anfitrión con otro dueño: se permite leerlo con git
RUN git config --system --add safe.directory '*'
USER jenkins

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

COPY jenkins.yaml /usr/share/jenkins/ref/jenkins.yaml
ENV CASC_JENKINS_CONFIG=/usr/share/jenkins/ref/jenkins.yaml
