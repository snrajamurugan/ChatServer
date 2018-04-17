# ChatServer
Java and RabbitMQ based Chat Application

This repository contains Dockerfile and Docker comppose file to build the Chat Server Application

Installation
1. Install Docker.
2. Download the docker-compose.yml and chatap/Dockerfile
3. Go the directory containing the docker-compose.yml
4. Execute "docker-compose up -d" command to start chat and rabbitmq services


Usage
Run rabbitmq-server
docker run -d -p 5672:5672 -p 15672:15672 dockerfile/rabbitmq
Run rabbitmq-server w/ persistent shared directories.
docker run -d -p 5672:5672 -p 15672:15672 -v <log-dir>:/data/log -v <data-dir>:/data/mnesia dockerfile/rabbitmq
