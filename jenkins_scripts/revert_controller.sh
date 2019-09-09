#!/bin/bash

value=`cat controller_commit`
cd /home/foxtrot/.jenkins/workspace/iot/
if [ -z "$value"]
then
    echo "No commit found. Returning to master"
    git checkout master
else
    echo "Checking out to commit ${value}"
    git checkout $(value)
    cd /home/foxtrot/.jenkins/workspace/SmartHomeDocker/
    sudo docker-compose up --build -d
fi
cd 
