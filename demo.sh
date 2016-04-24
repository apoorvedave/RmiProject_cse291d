#!/bin/bash

#Demo Script:
./cleanup.sh
make
docker network create -d bridge pingnetwork
docker build -f Dockerfile -t my-java-app .
docker run -d --net=pingnetwork --name pingserver my-java-app java -cp . pingponging/server/MyDriver 9000
docker run -d --net=pingnetwork --name pingclient my-java-app java -cp . pingponging/client/PingPongClient pingserver 9000

sleep 5
docker logs pingclient
