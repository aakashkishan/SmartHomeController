cd ~/.jenkins/workspace/SmartHomeDocker/
sudo docker build --tag=api .
sudo docker run -p 5000:5000 api
cd "~/.jenkins/workspace/SmartHomeDocker/MLModel"
python3 api2.py &
