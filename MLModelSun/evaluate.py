from pred import pred
import csv
import pandas as pd
import numpy as np
from datetime import time

def mserror(y, y_pred, mode):
    x = []
    x_pred = []
    res = 0
    for i in range(0, len(y)):
        if str(y[i])=='nan' or str(y_pred[i])=='nan':
            if str(mode)=='night':
                x.append(0)
                x_pred.append(8)
            else:
                x.append(0)
                x_pred.append(16)
        else:
            yt = pd.to_datetime(y[i], format='%H:%M')
            yt_pred = pd.to_datetime(y_pred[i], format='%H:%M')
            x.append(yt.hour+yt.minute/60)
            x_pred.append(yt_pred.hour+yt_pred.minute/60)
        res = res + (x[i]-x_pred[i])*(x[i]-x_pred[i])
 
    return res / len(y)

def evaluate_single(user, mode, date, row):
	return pred(user,mode,date)

def evaluate_user(user):
	df = pd.read_csv("test/"+user+".csv", names=['sunrise', 'sunset', 'date', 'user', 'nightTime', 'morningTime'], header=None, usecols=[0,1,3,4,5,6], dtype='object')
	y = []
	y_pred = []

	mode = 'day'
	for index, row in df.iterrows():
		user = row['user']
		date = row['date']
		y.append(row['morningTime'])
		y_pred.append(evaluate_single(user, mode, date, row))
	res = mserror(y, y_pred, mode)

	mode = 'night'
	for index, row in df.iterrows():
		user = row['user']
		date = row['date']
		y.append(row['nightTime'])
		y_pred.append(evaluate_single(user, mode, date, row))
	res = res + mserror(y, y_pred, mode)

	return res


def evaluate_model():
	error = 0
	users = ['1688', '3440', '5037', '9351', '9419', '990']
	for user in users:
		error = error + evaluate_user(user)

	error = error / 6

	fo = open("evaluate.txt", "w+")
	error = str(error)
	fo.write(error)
	fo.close()

	#generate csv file that contains mse
	mse = []
	mse.append(error)
	file = open("quality.csv", "w+")
	writer = csv.writer(file)
	writer.writerow(["mse"])
	writer.writerow(mse)
	file.close()

	return error



