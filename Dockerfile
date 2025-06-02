FROM jenkins/jenkins:lts

USER root

ARG DOCKER_GID=992

RUN apt-get update && apt-get install -y git docker.io \
    && groupmod -g ${DOCKER_GID} docker || groupadd -g ${DOCKER_GID} docker \
    && usermod -aG docker jenkins

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

COPY casc_configs/ /var/jenkins_home/casc_configs/
COPY init.groovy.d/ /var/jenkins_home/init.groovy.d/

ENV CASC_JENKINS_CONFIG=/var/jenkins_home/casc_configs/jenkins.yaml
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

USER jenkins
