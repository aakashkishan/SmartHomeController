from datetime import datetime

def identify(file,odate):
    result = []
    stamp = odate.timestamp()
    with open(file,"r") as f:
        for line in f.readlines():
            stamp += 24*3600
            rise,sset = line.strip().split(",")
            rise,sset = rise[:2]+":"+rise[2:],sset[:2]+":"+sset[2:]
            date = datetime.fromtimestamp(stamp)
            string = date.strftime("%Y-%m-%d")
            result.append(string+","+rise+","+sset+"\n")
    
    with open(file,"w") as f:
        f.writelines(result)

identify("sundata/18srs.csv",datetime.strptime("2017-12-31","%Y-%m-%d"))
identify("sundata/19srs.csv",datetime.strptime("2018-12-31","%Y-%m-%d"))