FROM openjdk:8
LABEL maintainer="anran <me@anran.name>"
RUN echo "hello world"
# RUN echo "\
# deb http://mirrors.aliyun.com/debian/ stretch main non-free contrib\n\
# deb http://mirrors.aliyun.com/debian-security stretch/updates main\n\
# deb http://mirrors.aliyun.com/debian/ stretch-updates main non-free contrib\n\
# deb http://mirrors.aliyun.com/debian/ stretch-backports main non-free contrib\n\
# " >/etc/apt/sources.list
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# RUN apt-get update \
# && apt-get install -y curl \
# && apt-get install -y openssh-server \
# && apt-get install -y procps \
# && apt-get install -y net-tools \
# && apt-get install -y vim \
# && apt-get install -y git
RUN echo "alias ll='ls -lh'" >> /root/.bashrc
COPY disk/k8sApplication.jar /root/
RUN echo "\
java -jar /root/k8sApplication.jar\n\
tail\n\
" >/root/init.sh
EXPOSE 8080
CMD ["/bin/bash","/root/init.sh"]
