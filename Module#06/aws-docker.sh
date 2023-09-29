#!/bin/bash

sudo yum update -y
cat /etc/os-release
sudo yum install -y docker
sudo usermod -a -G docker ec2-user
sudo yum install -y libxcrypt-compat

netstat -an | grep LISTEN

docker-compose ps
docker-compose restart sonarqube

docker exec -it ec2-user_tomcat_1 /bin/bash


docker-compose version
sudo service docker start

sudo yum clean metadata
docker info
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose version
