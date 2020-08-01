FROM openjdk:11.0.1-jre-slim

# Add App Insights Java Agent
COPY applicationinsights-agent-3.0.0-PREVIEW.5.jar /usr/share/app/applicationinsights-agent.jar
COPY ApplicationInsights.json /usr/share/app/ApplicationInsights.json

# Configure SSH for App Service
RUN apt-get update && apt-get install -y openssh-server
RUN echo 'root:Docker!' | chpasswd
COPY sshd_config /etc/ssh/

# Copy in the service and startup script
COPY target/*.jar /usr/share/app/service.jar
COPY startup.sh /scripts/startup.sh
RUN ["chmod", "+x", "/scripts/startup.sh"]

# Expose the API and SSH
EXPOSE 8080/tcp 2222

ENTRYPOINT ["/scripts/startup.sh"]