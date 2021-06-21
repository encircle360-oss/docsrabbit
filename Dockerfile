FROM ubuntu:20.04
ENV TZ=Europe/Berlin
ENV DEBIAN_FRONTEND=noninteractive

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN apt-get -y update && apt-get -y --no-install-recommends install tzdata xorg openssl xvfb wget xz-utils bzip2 fontconfig libfreetype6 libjpeg-turbo8 libpng16-16 libx11-6 libxcb1 libxext6 libxrender1 xfonts-75dpi xfonts-base libreoffice python unoconv && apt-get -y clean
RUN wget -O wkhtmlpdf.deb https://github.com/wkhtmltopdf/packaging/releases/download/0.12.6-1/wkhtmltox_0.12.6-1.focal_amd64.deb
RUN dpkg -i wkhtmlpdf.deb
RUN apt -y --no-install-recommends install openjdk-14-jre

ADD /build/libs/*.jar /docsrabbit.jar

ENV SPRING_PROFILES_ACTIVE=production
ENTRYPOINT ["java","-Duser.language=en-US", "-Dfile.encoding=UTF-8", "-Djava.security.egd=file:/dev/./urandom","-jar","/docsrabbit.jar"]
