import mysql.connector

def update():
	return 0

def get_data():
	l=['mse','cmu','isr','msit','scs']
	m=[]
	query=("select count(*) from (select Home.* from Home join (select house_id from House where home_name = 'msit') as H on H.house_id = Home.house_id and Home.hvac_state='on' and Home.create_time between SYSDATE() - INTERVAL 2 MINUTE and SYSDATE()) as A;")
	c.execute(query)
	for i in c:
	     	m.append(i)
	return m

if __name__=="__main__":
	conn = mysql.connector.connect(host='localhost', database='TartanHome',user='root', password='tmp')
	c = conn.cursor()

	while(1):
		m = get_data(c)
		mean = m/5
		update()

	c.close()
	conn.close()
