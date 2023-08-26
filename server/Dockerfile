FROM amazoncorretto:11-alpine-jdk
RUN ln -sf /usr/share/zoneinfo/America/Los_Angeles /etc/localtime && echo "America/Los_Angeles" > /etc/timezone
ENV TZ America/Los_Angeles
COPY target/*.jar shareit-server.jar
ENTRYPOINT ["java","-jar","/shareit-server.jar"]