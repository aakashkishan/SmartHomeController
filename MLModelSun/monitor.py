from pred import pred
import csv
import pandas as pd
import numpy as np
from datetime import time
from evaluate import mserror

def write_csv(mode, dates, errors):
    file = open("monitor_"+mode+".csv", "w+")
    file.write("date,mse\n")
    for i in range(0,len(dates)):
        day = str(dates[i])
        error = str(errors[i])
        file.write(day+","+error+"\n")
    file.close()

def monitor(mode):
    date = df['date'].iloc[0]
    days = []
    errors = []
    y=[]
    y_pred=[]
    for index, row in df.iterrows():
        user = row['user']
        if row['date'] != date:
            days.append(date)
            date = row['date']
            day_mse = mserror(y, y_pred, mode)
            errors.append(day_mse)
            y=[]
            y_pred=[]
        if str(mode)=='day':
            y.append(row['morningTime'])
        else:
            y.append(row['nightTime'])
        y_pred.append(pred(user, mode, date))

    days.append(date)
    date = row['date']
    day_mse = mserror(y, y_pred, mode)
    errors.append(day_mse)
    write_csv(mode, days, errors)

users = ['3440', '5037', '9351', '9419', '990']
df = pd.read_csv("live/1688.csv", names=['sunrise', 'sunset', 'date', 'user', 'nightTime', 'morningTime'], header=None, usecols=[0,1,3,4,5,6], dtype='object')
for user in users:
    tmp = pd.read_csv("live/"+user+".csv", names=['sunrise', 'sunset', 'date', 'user', 'nightTime', 'morningTime'], header=None, usecols=[0,1,3,4,5,6], dtype='object')
    df = pd.concat([df, tmp])
df = df.sort_values(by=['date'])
monitor('day')
monitor('night')




