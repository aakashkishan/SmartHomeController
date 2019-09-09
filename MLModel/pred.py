import sys
from joblib import load
from datetime import datetime

def pred(user,mode,date):
    if mode != 'day' and mode != 'night':
        print("Please input 'day' or 'night' as the second argument!")
        return
    
    date = datetime.strptime(date,"%Y-%m-%d")
    modelFile = "./models/"+user+"-"+mode+".joblib"
    model = load(modelFile)
    result = model.predict([[date.weekday()]])
    
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
    user = sys.argv[1]
    mode = sys.argv[2]
    date = sys.argv[3]

    # user = "9351"
    # mode = "day"
    # date = "2020-03-20"

    pred(user,mode,date)
