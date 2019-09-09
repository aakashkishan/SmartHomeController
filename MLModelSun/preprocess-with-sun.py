#!/usr/bin/env python3
import os
import shutil
from pytz import timezone
from datetime import datetime
from collections import defaultdict

riseset = {}
users = defaultdict(lambda: 0)
nightBegin = "23:59"
nightEnd = "08:00"
maxBegin,maxEnd = '33:33','0'
data = live = test = None

def isNight(time):
    if time > nightBegin or time < nightEnd:
        return True
    else:
        return False

def maxNightTime(a,b):
    if a > nightBegin and b < nightEnd:
        return b
    if b > nightBegin and a < nightEnd:
        return a    
    return a if a > b else b

# clean data and remove redundant data
def clean(origin_file='lights.csv', cleaned_file='clean.csv'):
    lines = []
    lineset = set()
    readFirst = False
    with open(origin_file,"r") as f:
        for line in f.readlines():
            
            #skip csv head
            if not readFirst:
                readFirst = True
                continue

            # remove \n
            line = line.strip()
            # the correct line should have 3 comma
            if line.count(",") != 3:
                continue
            date,time,id,state = line.split(",")

            #user id should not be empty
            if len(date) != 10 or len(time) != 5 or len(id) < 1 or len(state) != 1:
                continue

            #preparing for user data splitting
            users[id] += 1

            indicator = ",".join([date,time,id])
            # won't process repetitive line
            if indicator in lineset:
                continue
            # add this line into set
            lineset.add(indicator) 
            # the date time should be valid
            try:
                datetime.strptime(date+" "+time,"%Y-%m-%d %H:%M")
            except ValueError:
                pass
            else:
                if state == '0' or state == '1':
                    lines.append(line)
                        
    with open(cleaned_file,"w") as f:
        for line in lines:
            f.write(line+"\n")

# create a map, key is the user id, value is an empty list, like this:
# data = {"9351":[],"5037":[],"990":[],"1688":[],"3440":[],"9419":[]}
# live = {"9351":[],"5037":[],"990":[],"1688":[],"3440":[],"9419":[]}
# test = {"9351":[],"5037":[],"990":[],"1688":[],"3440":[],"9419":[]}
def createUserDataMap():
    global data,live,test
    data = {user:[] for user,count in users.items() if count > 100}
    live = {user:[] for user,count in users.items() if count > 100}
    test = {user:[] for user,count in users.items() if count > 100}

# split data per user
def splitByUser(path='clean.csv'):

    utc = timezone('UTC')
    pitts = timezone('America/New_York')

    with open(path,"r") as f:
        for line in f.readlines():
            line = line.strip()
            date,time,id,state = line.split(",")
            dt = datetime.strptime(date+" "+time,"%Y-%m-%d %H:%M")
            dt = dt.replace(tzinfo=utc).astimezone(pitts)
            dtstr = dt.strftime("%Y-%m-%d,%H:%M")
            if id in data:
                array = data[id]
                array.append(dtstr+","+id+","+state)

# find the boundary data
# boundary data means at this minute, the light's state changed
def boundarys():
    for id,logs in data.items():
        blogs = []
        lastdate = laststate = -1
        for log in logs:
            date,time,id,state = log.split(",")
            if laststate == '0' and state == '1':
                blogs.append(date+','+time+','+id+',1')
            elif laststate == '1' and state == '0':
                blogs.append(date+','+time+','+id+',0')
            laststate = state
            lastdate = date
        data[id] = blogs

# find the turn on time and turn off time according to the requirement
# the ealiest data in the morning which turned on the light is the turn on data
# the latest data in the evening which turned off the light is the turn off data 
def theTime():

    for id,logs in data.items():

        rlog = []
        lastdate = None
        on,off = maxBegin,maxEnd

        for log in logs:
            date,time,id,state = log.split(",") 

            # change a date
            if lastdate != None and lastdate != date:
                if on == maxBegin:
                    on = "null"
                if off == maxEnd:
                    off = "null"
                rlog.append(lastdate+","+id+","+on+","+off)
                on,off = maxBegin,maxEnd

            # find the ealist light-on time in a day
            if state == '1' and not isNight(time) and time < on :
                on = time
            # find the latest light-off time in a day
            if state == '0' and isNight(time) and maxNightTime(time,off) == time:
                off = time

            lastdate = date
        data[id] = rlog

# add day of week feature
def addDayOfWeek():
    for id,logs in data.items():
        new = []
        for log in logs:
            date,id,on,off = log.split(",")
            weekday = datetime.strptime(date,"%Y-%m-%d").weekday()
            new.append(str(weekday)+","+log)
        data[id] = new

# add read sunrise and sunset data
def readRiseSetData(file="sundata/srs.csv"):
    global riseset
    with open(file,"r") as f:
        for line in f.readlines():
            date,rise,sset = line.strip().split(",")
            riseset[date] = rise+","+sset

# add sunrise and sunset feature
def addSunRiseSetTime():
    for id,logs in data.items():
        new = []
        for log in logs:
            week,date,id,on,off = log.split(",")
            rise,sset = riseset[date].split(",")
            new.append(rise+","+sset+","+week+","+date+","+id+","+on+","+off)
        data[id] = new

# split data to train/test/live
# live after 2019-01-01 (including)
# train & test before 2019-01-01
def splitLive():
    for id,logs in data.items():
        tt = []
        li = []
        for log in logs:
            rise,sset,week,date,id,on,off = log.split(",")
            if date < '2019-01-01':
                tt.append(log)
            else:
                li.append(log)
        
        data[id] = tt
        live[id] = li
    
# split train & test
def splitTrain():

    # go through each user
    for id,logs in data.items():
        findone = False
        lastmonth = None
        start = dateCount = 0
        tmp,testg,traing = [],[],[]

        # go through every month
        for log in logs:

            rise,sset,week,date,id,on,off = log.split(",")
            month = date[5:7]
            
            #if month changed, process the last month's data
            if month != lastmonth and lastmonth != None:
                dateCount = 0
                findone = False

                #extract the whole first week as test data
                for j in range(start,start+7):
                    line = tmp[j]
                    lr,ls,lw,ld,li,lon,loff = line.split(",")
                    testg.append(line)
                    if lw == '6':
                        break
                traing += tmp[:start] + tmp[j+1:]
                tmp = []

            # the first complete week of a month will be used as test data
            if not findone and week == '0':
                findone = True
                start = dateCount
            
            dateCount += 1
            tmp.append(log)
            lastmonth = month

        #process the last month
        for j in range(start,start+7):
            line = tmp[j]
            lr,ls,lw,ld,li,lon,loff = line.split(",")
            testg.append(line)
            if lw == '6':
                break
        traing += tmp[:start] + tmp[j+1:]

        data[id] = traing
        test[id] = testg

# write results into files
def writeResult():
    write("./live",live)
    write("./train",data)
    write("./test",test)

# delete the old directory
# create the new directory
# write the data file
def write(dpath,dat):
    if os.path.isdir(dpath):
        shutil.rmtree(dpath)
    createDirectory(dpath)

    for id,logs in dat.items():
        with open(dpath+"/"+str(id)+".csv","w") as f:
            for log in logs:
                f.write(log+"\n")

#helper function, just create a new empty directory
def createDirectory(path):
    try:  
        os.mkdir(path)
    except OSError:  
        print ("Creation of the directory %s failed" % path)
    else:  
        print ("Successfully created the directory %s " % path)

def preprocess_api():
    clean()
    createUserDataMap()
    splitByUser()
    boundarys()
    theTime()
    addDayOfWeek()
    readRiseSetData()
    addSunRiseSetTime()
    splitLive()
    splitTrain()
    writeResult()

if __name__ == '__main__':
    preprocess_api()
    print("finished")