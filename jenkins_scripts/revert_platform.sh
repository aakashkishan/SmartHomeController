#!/bin/bash

value=`cat platform_commit`
cd /home/foxtrot/.jenkins/workspace/SmartHomeDocker/
if [ -z "$value"]
then
    echo "No commit found. Returning to master"
    git checkout master
else
    echo "Checking out to commit ${value}"
    git checkout $(value)
    sudo docker-compose up --build -d
fi
cd 
