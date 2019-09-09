-- MySQL dump 10.13  Distrib 5.7.25, for Linux (x86_64)
--
-- Host: localhost    Database: TartanHome
-- ------------------------------------------------------
-- Server version	5.7.25

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `id` bigint(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (990,'123456','Tom', 990),(1688,'123456','Jerry', 1688),(3440,'123456','Mike', 3440),(5037,'123456','Mary', 5037),(9351,'123456','David', 9351),(9419,'123456','Bob', 9419);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `House`
--

DROP TABLE IF EXISTS `House`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `House` (
  `house_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `alarm_delay` varchar(255) DEFAULT NULL,
  `alarm_passcode` varchar(255) DEFAULT NULL,
  `home_name` varchar(255) NOT NULL,
  `port` int(11) DEFAULT NULL,
  `target_temp` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`house_id`),
  KEY `FK65vfmrvg4o0umant2dhq7m9p0` (`user_id`),
  CONSTRAINT `FK65vfmrvg4o0umant2dhq7m9p0` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `House`
--

LOCK TABLES `House` WRITE;
/*!40000 ALTER TABLE `House` DISABLE KEYS */;
INSERT INTO `House` VALUES (1,'house-mse','30','stop','mse',5050,'70',9351),(2,'house-msit','30','stop','msit',5051,'70',9351),(3,'house-cmu','30','stop','cmu',5052,'70',5037),(4,'house-isr','30','stop','isr',5053,'70',990),(5,'house-scs','30','stop','scs',5054,'70',1688),(6,'house-ini','30','stop','ini',5055,'70',3440),(7,'house-ece','30','stop','ece',5056,'70',9419);
/*!40000 ALTER TABLE `House` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Table structure for table `Home`
--

DROP TABLE IF EXISTS `Home`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Home` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `alarm_active_state` varchar(255) DEFAULT NULL,
  `alarm_enabled_state` varchar(255) DEFAULT NULL,
  `alarm_delay` varchar(255) DEFAULT NULL,
  `all_clear` varchar(255) DEFAULT NULL,
  `arrive_state` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `door_state` varchar(255) DEFAULT NULL,
  `end_time` varchar(255) DEFAULT NULL,
  `humidifier_state` varchar(255) DEFAULT NULL,
  `humidity` varchar(255) DEFAULT NULL,
  `hvac_mode` varchar(255) DEFAULT NULL,
  `hvac_state` varchar(255) DEFAULT NULL,
  `intruder_state` varchar(255) DEFAULT NULL,
  `light_state` varchar(255) DEFAULT NULL,
  `lock_state` varchar(255) DEFAULT NULL,
  `proximity_state` varchar(255) DEFAULT NULL,
  `register_received` varchar(255) DEFAULT NULL,
  `start_time` varchar(255) DEFAULT NULL,
  `target_temp` varchar(255) DEFAULT NULL,
  `temperature` varchar(255) DEFAULT NULL,
  `house_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh6oe48457t7n599fwjlrb159f` (`house_id`),
  CONSTRAINT `FKh6oe48457t7n599fwjlrb159f` FOREIGN KEY (`house_id`) REFERENCES `House` (`house_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3068591 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Home`
--

LOCK TABLES `Home` WRITE;
/*!40000 ALTER TABLE `Home` DISABLE KEYS */;
INSERT INTO `Home` VALUES (3068507,'house-isr','inactive','disarmed','30','off','off','2019-04-06 02:22:48','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',4),(3068508,'house-msit','inactive','disarmed','30','off','off','2019-04-06 02:22:48','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',2),(3068509,'house-mse','inactive','disarmed','30','off','off','2019-04-06 02:22:48','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',1),(3068510,'house-scs','inactive','disarmed','30','off','off','2019-04-06 02:22:48','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',5),(3068511,'house-cmu','inactive','disarmed','30','off','off','2019-04-06 02:22:48','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',3),(3068512,'house-ini','inactive','disarmed','30','off','off','2019-04-06 02:22:48','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',6),(3068513,'house-ece','inactive','disarmed','30','off','off','2019-04-06 02:22:48','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',7),(3068514,'house-isr','inactive','disarmed','30','off','off','2019-04-06 02:22:53','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',4),(3068515,'house-msit','inactive','disarmed','30','off','off','2019-04-06 02:22:53','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',2),(3068516,'house-mse','inactive','disarmed','30','off','off','2019-04-06 02:22:53','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',1),(3068517,'house-scs','inactive','disarmed','30','off','off','2019-04-06 02:22:53','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',5),(3068518,'house-cmu','inactive','disarmed','30','off','off','2019-04-06 02:22:53','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',3),(3068519,'house-ini','inactive','disarmed','30','off','off','2019-04-06 02:22:53','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',6),(3068520,'house-ece','inactive','disarmed','30','off','off','2019-04-06 02:22:53','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',7),(3068521,'house-isr','inactive','disarmed','30','off','off','2019-04-06 02:22:58','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',4),(3068522,'house-msit','inactive','disarmed','30','off','off','2019-04-06 02:22:58','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',2),(3068523,'house-mse','inactive','disarmed','30','off','off','2019-04-06 02:22:58','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',1),(3068524,'house-scs','inactive','disarmed','30','off','off','2019-04-06 02:22:58','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',5),(3068525,'house-cmu','inactive','disarmed','30','off','off','2019-04-06 02:22:58','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',3),(3068526,'house-ece','inactive','disarmed','30','off','off','2019-04-06 02:22:58','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',7),(3068527,'house-ini','inactive','disarmed','30','off','off','2019-04-06 02:22:58','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',6),(3068528,'house-cmu','inactive','disarmed','30','off','off','2019-04-06 02:23:04','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',3),(3068529,'house-isr','inactive','disarmed','30','off','off','2019-04-06 02:23:04','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',4),(3068530,'house-ece','inactive','disarmed','30','off','off','2019-04-06 02:23:04','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',7),(3068531,'house-mse','inactive','disarmed','30','off','off','2019-04-06 02:23:04','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',1),(3068532,'house-ini','inactive','disarmed','30','off','off','2019-04-06 02:23:04','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',6),(3068533,'house-msit','inactive','disarmed','30','off','off','2019-04-06 02:23:04','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',2),(3068534,'house-scs','inactive','disarmed','30','off','off','2019-04-06 02:23:04','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',5),(3068535,'house-cmu','inactive','disarmed','30','off','off','2019-04-06 02:23:09','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',3),(3068536,'house-isr','inactive','disarmed','30','off','off','2019-04-06 02:23:09','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',4),(3068537,'house-ece','inactive','disarmed','30','off','off','2019-04-06 02:23:09','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',7),(3068538,'house-mse','inactive','disarmed','30','off','off','2019-04-06 02:23:09','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',1),(3068539,'house-ini','inactive','disarmed','30','off','off','2019-04-06 02:23:09','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',6),(3068540,'house-msit','inactive','disarmed','30','off','off','2019-04-06 02:23:09','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',2),(3068541,'house-scs','inactive','disarmed','30','off','off','2019-04-06 02:23:09','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',5),(3068542,'house-cmu','inactive','disarmed','30','off','off','2019-04-06 02:23:14','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',3),(3068543,'house-ece','inactive','disarmed','30','off','off','2019-04-06 02:23:14','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',7),(3068544,'house-isr','inactive','disarmed','30','off','off','2019-04-06 02:23:14','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',4),(3068545,'house-mse','inactive','disarmed','30','off','off','2019-04-06 02:23:14','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',1),(3068546,'house-msit','inactive','disarmed','30','off','off','2019-04-06 02:23:14','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',2),(3068547,'house-scs','inactive','disarmed','30','off','off','2019-04-06 02:23:14','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',5),(3068548,'house-ini','inactive','disarmed','30','off','off','2019-04-06 02:23:14','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',6),(3068549,'house-cmu','inactive','disarmed','30','off','off','2019-04-06 02:23:19','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',3),(3068550,'house-isr','inactive','disarmed','30','off','off','2019-04-06 02:23:19','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',4),(3068551,'house-ece','inactive','disarmed','30','off','off','2019-04-06 02:23:19','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',7),(3068552,'house-ini','inactive','disarmed','30','off','off','2019-04-06 02:23:19','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',6),(3068553,'house-mse','inactive','disarmed','30','off','off','2019-04-06 02:23:19','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',1),(3068554,'house-msit','inactive','disarmed','30','off','off','2019-04-06 02:23:19','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',2),(3068555,'house-scs','inactive','disarmed','30','off','off','2019-04-06 02:23:19','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',5),(3068556,'house-cmu','inactive','disarmed','30','off','off','2019-04-06 02:23:24','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',3),(3068557,'house-isr','inactive','disarmed','30','off','off','2019-04-06 02:23:24','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',4),(3068558,'house-ece','inactive','disarmed','30','off','off','2019-04-06 02:23:24','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',7),(3068559,'house-mse','inactive','disarmed','30','off','off','2019-04-06 02:23:24','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',1),(3068560,'house-msit','inactive','disarmed','30','off','off','2019-04-06 02:23:24','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',2),(3068561,'house-ini','inactive','disarmed','30','off','off','2019-04-06 02:23:24','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',6),(3068562,'house-scs','inactive','disarmed','30','off','off','2019-04-06 02:23:24','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',5),(3068563,'house-cmu','inactive','disarmed','30','off','off','2019-04-06 02:23:29','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',3),(3068564,'house-isr','inactive','disarmed','30','off','off','2019-04-06 02:23:29','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',4),(3068565,'house-ece','inactive','disarmed','30','off','off','2019-04-06 02:23:29','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',7),(3068566,'house-mse','inactive','disarmed','30','off','off','2019-04-06 02:23:29','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',1),(3068567,'house-msit','inactive','disarmed','30','off','off','2019-04-06 02:23:29','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',2),(3068568,'house-scs','inactive','disarmed','30','off','off','2019-04-06 02:23:29','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',5),(3068569,'house-ini','inactive','disarmed','30','off','off','2019-04-06 02:23:29','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',6),(3068570,'house-isr','inactive','disarmed','30','off','off','2019-04-06 02:23:34','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',4),(3068571,'house-ece','inactive','disarmed','30','off','off','2019-04-06 02:23:34','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',7),(3068572,'house-cmu','inactive','disarmed','30','off','off','2019-04-06 02:23:34','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',3),(3068573,'house-mse','inactive','disarmed','30','off','off','2019-04-06 02:23:34','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',1),(3068574,'house-ini','inactive','disarmed','30','off','off','2019-04-06 02:23:34','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',6),(3068575,'house-msit','inactive','disarmed','30','off','off','2019-04-06 02:23:34','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',2),(3068576,'house-scs','inactive','disarmed','30','off','off','2019-04-06 02:23:34','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',5),(3068577,'house-isr','inactive','disarmed','30','off','off','2019-04-06 02:23:39','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',4),(3068578,'house-ece','inactive','disarmed','30','off','off','2019-04-06 02:23:39','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',7),(3068579,'house-mse','inactive','disarmed','30','off','off','2019-04-06 02:23:39','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',1),(3068580,'house-cmu','inactive','disarmed','30','off','off','2019-04-06 02:23:39','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',3),(3068581,'house-msit','inactive','disarmed','30','off','off','2019-04-06 02:23:39','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',2),(3068582,'house-ini','inactive','disarmed','30','off','off','2019-04-06 02:23:39','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',6),(3068583,'house-scs','inactive','disarmed','30','off','off','2019-04-06 02:23:39','open','06:00','off','100','cool','on','off','on','on','occupied','off','21:00','70','71',5),(3068584,'house-isr','inactive','disarmed','30','off','off','2019-04-06 02:23:44','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',4),(3068585,'house-ece','inactive','disarmed','30','off','off','2019-04-06 02:23:44','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',7),(3068586,'house-mse','inactive','disarmed','30','off','off','2019-04-06 02:23:44','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',1),(3068587,'house-cmu','inactive','disarmed','30','off','off','2019-04-06 02:23:44','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',3),(3068588,'house-msit','inactive','disarmed','30','off','off','2019-04-06 02:23:44','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',2),(3068589,'house-scs','inactive','disarmed','30','off','off','2019-04-06 02:23:44','open','06:00','off','100','heat','off','off','on','on','occupied','off','21:00','70','70',5),(3068590,'house-ini','inactive','disarmed','30','off','off','2019-04-06 02:23:44','open','06:00','off','100','heat','on','off','on','on','occupied','off','21:00','70','69',6);
/*!40000 ALTER TABLE `Home` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (1);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-04-06  2:23:45

