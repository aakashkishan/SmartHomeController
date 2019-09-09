import sys
import torch
from joblib import load
from datetime import datetime

def unifyTime(time):
    hour,minute = time.split(":")
    return int(hour) * 60 + int(minute)

def pred(user,mode,datestr):

    if mode != 'day' and mode != 'night':
        print("Please input 'day' or 'night' as the second argument!")
        return
    
    date = None

    try:
        date = datetime.strptime(datestr,"%Y-%m-%d")
    except ValueError:
        print("Please input a valid date!")
    
    if date.year <= 2019 and date.year >= 2018:
        lines =open("sundata/"+str(date.year)+"srs.csv").readlines()
        line = [l for l in lines if datestr in l][0]
        __,rise,sset = line.strip().split(",")
        rise,sset = unifyTime(rise),unifyTime(sset)
    else:
        rise,sset = unifyTime("06:23"),unifyTime("18:32")

    modelFile = "./models/"+user+"-"+mode+".joblib"
    model = load(modelFile)
    result = model.predict([[rise,sset,date.weekday()]])
    
    result = parseResult(result[0])
    print("prediction: "+result)
    return result

def parseResult(result):
    hour = int(result//60)
    minute = int(result - 60*hour)
    time = datetime(2019,3,26,hour,minute,0)
    return time.strftime("%H:%M")

def pred_api(user, mode, year, month, day):
    return pred(user, mode, "%s-%s-%s" % (year, month, day))

if __name__ == '__main__':
    # user = sys.argv[1]
    # mode = sys.argv[2]
    # date = sys.argv[3]

    user = "9351"
    mode = "day"
    date = "2019-06-20"

    pred(user,mode,date)