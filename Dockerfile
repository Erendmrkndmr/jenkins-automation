FROM jenkins/jenkins:lts

USER root
RUN apt-get update && apt-get install -y sudo git curl

USER jenkins

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

COPY casc_configs /var/jenkins_home/casc_configs
COPY init.groovy.d /var/jenkins_home/init.groovy.d
ENV CASC_JENKINS_CONFIG=/var/jenkins_home/casc_configs/jenkins.yaml
