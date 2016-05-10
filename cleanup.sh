#!/bin/bash
make clean
docker stop $(docker ps -a -q) && docker rm $(docker ps -a -q)
docker rmi my-java-app
docker network rm pingnetwork
