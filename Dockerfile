FROM openjdk:14-jdk-slim
VOLUME /tmp
RUN DEBIAN_FRONTEND=noninteractive && apt-get -y update && apt-get -y install xorg openssl xvfb wget xz-utils bzip2 && apt-get -y clean
RUN wget https://github.com/wkhtmltopdf/wkhtmltopdf/releases/download/0.12.4/wkhtmltox-0.12.4_linux-generic-amd64.tar.xz && \
    tar -xf wkhtmltox-0.12.4_linux-generic-amd64.tar.xz && \
    cp wkhtmltox/bin/wkhtmlto* /usr/bin/
ADD /build/libs/*.jar /docsrabbit.jar
ENV SPRING_PROFILES_ACTIVE=production
ENTRYPOINT ["java","-Duser.language=en-US", "-Djava.security.egd=file:/dev/./urandom","-jar","/docsrabbit.jar"]
