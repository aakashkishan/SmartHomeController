#!/bin/sh
value=`cat model_number.txt`
value=`expr $value + 1`
echo "$value" > model_number.txt 
rm ../model-versioning/models/*
cp models/* ../model-versioning/models/          
cd ../model-versioning
git add -A 
git commit -m "Version_$value"
git push
