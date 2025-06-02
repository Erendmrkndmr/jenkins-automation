FROM jenkins/jenkins:lts

USER root
RUN apt-get update && apt-get install -y git docker.io

# Pluginleri yükle
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

# JCasC config ve groovy scriptlerini kopyala
COPY casc_configs/ /var/jenkins_home/casc_configs/
COPY init.groovy.d/ /var/jenkins_home/init.groovy.d/

ENV CASC_JENKINS_CONFIG=/var/jenkins_home/casc_configs/jenkins.yaml
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

# Docker grubuna jenkins kullanıcısını ekle (koşullu olarak)
RUN getent group docker || groupadd docker && usermod -aG docker jenkins

USER jenkins
