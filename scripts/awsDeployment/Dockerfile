FROM dockerfile/java
MAINTAINER Ashutosh Mishra <ashutosh@podkart.com>
ADD files /
WORKDIR /opt/docker
RUN ["chown", "-R", "daemon", "."]
USER daemon
ENTRYPOINT ["bin/apiserver"]
CMD []
EXPOSE 9000
