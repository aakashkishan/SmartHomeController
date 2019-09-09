import os
from sklearn import tree
from joblib import dump

def readData(path):
    result = {}
    for root, dirs, files in os.walk(path):
        for f in files:
            logs = []
            user,__ = f.split(".")
            with open(path+f,"r") as f:
                for line in f.readlines():
                    logs.append(line.strip())
            result[user] = logs
    return result

def unifyTime(time):
    hour,minute = time.split(":")
    return int(hour) * 60 + int(minute)

def train(input):

    for id,logs in input.items():
        dx,nx,day,night = [], [], [], []

        for log in logs:
            week,date,id,on,off = log.split(",")
            if on != 'null':
                dx.append([int(week)])
                day.append(unifyTime(on))
            if off != "null":
                nx.append([int(week)])
                night.append(unifyTime(off))

        daytree = tree.DecisionTreeRegressor()
        daytree.fit(dx,day)
        nightree = tree.DecisionTreeRegressor()
        nightree.fit(nx,night)
        dayfile = "models/"+id+"-day"+".joblib"
        nightfile = "models/"+id+"-night"+".joblib"
        dump(daytree,dayfile)
        dump(nightree,nightfile)

def train_api(data_path):
    trainData = readData(data_path)
    if not trainData:
        return False, 'Can not read the data'
    train(trainData)
    return True, 'Training finished'

if __name__ == '__main__':
    trainData = readData("./train/")
    train(trainData)
    print("finish")
