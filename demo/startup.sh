#!/bin/bash

/etc/init.d/ssh start
java -javaagent:/usr/share/app/applicationinsights-agent.jar -jar /usr/share/app/service.jar -Xmx382293K -XX:+UseG1GC -XX:MaxMetaspaceSize=64M -Xss995K