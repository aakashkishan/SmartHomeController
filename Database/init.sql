use mysql;
create database if not exists TartanHome;
create user if not exists 'tartan'@'localhost' identified by 'tartan1234';
create user if not exists 'tartan'@'%' identified by 'tartan1234';
grant all on TartanHome.* to 'tartan'@'localhost';
grant all on TartanHome.* to 'tartan'@'%';
grant all on TartanHome.* to 'root'@'%';
flush privileges;
