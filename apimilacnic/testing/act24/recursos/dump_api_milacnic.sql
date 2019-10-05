-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: desarrollo.lacnic.net.uy    Database: api_milacnic
-- ------------------------------------------------------
-- Server version	5.0.95-log

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
-- Not dumping tablespaces as no INFORMATION_SCHEMA.FILES table on this server
--

--
-- Table structure for table `OrganizationConfig`
--

DROP TABLE IF EXISTS `OrganizationConfig`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OrganizationConfig` (
  `idOrgConfig` varchar(255) NOT NULL,
  `baneado` bit(1) default NULL,
  `clientId` varchar(255) default NULL,
  PRIMARY KEY  (`idOrgConfig`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OrganizationConfig`
--

LOCK TABLES `OrganizationConfig` WRITE;
/*!40000 ALTER TABLE `OrganizationConfig` DISABLE KEYS */;
INSERT INTO `OrganizationConfig` VALUES ('AR-ORG1-LACNIC','\0','sBrmjm7STNlc2Asi0Qvo4a9hIleqQJet'),('UY-ORG2-LACNIC','\0','4vscKpOltDsinfQZR9b7YboPqycOb5rR');
/*!40000 ALTER TABLE `OrganizationConfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TokenBucketConfig`
--

DROP TABLE IF EXISTS `TokenBucketConfig`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TokenBucketConfig` (
  `id` varchar(255) NOT NULL,
  `bucketSize` int(11) default NULL,
  `periodMinToRerill` int(11) default NULL,
  `tokensToAdd` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TokenBucketConfig`
--

LOCK TABLES `TokenBucketConfig` WRITE;
/*!40000 ALTER TABLE `TokenBucketConfig` DISABLE KEYS */;
INSERT INTO `TokenBucketConfig` VALUES ('CONTACT_INFO',100,3,3),('DEFAULT',10,1,2),('DELEGAR',80,3,3),('DELEGAR_DELETE',80,3,3),('DELEGAR_UPDATE',80,3,3),('ORG_CREATE',50,5,2),('ORG_INFO',100,2,3),('ORG_UPDATE',20,5,2),('SUBASIGNAR',60,3,2),('SUBASIGNAR_DELETE',30,2,1),('SUBASIGNAR_INFO',100,2,3),('SUBASIGNAR_UPDATE',80,3,3);
/*!40000 ALTER TABLE `TokenBucketConfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TokenBucketOrg`
--

DROP TABLE IF EXISTS `TokenBucketOrg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TokenBucketOrg` (
  `idTokenBucketOrg` varchar(45) NOT NULL,
  `tokenBucketConfig` varchar(45) default NULL,
  `bucketSize` int(11) default NULL,
  `periodMinToRerill` int(11) default NULL,
  `tokensToAdd` int(11) default NULL,
  `organization` varchar(255) default NULL,
  PRIMARY KEY  (`idTokenBucketOrg`),
  UNIQUE KEY `id_UNIQUE` (`idTokenBucketOrg`),
  KEY `FKs6miydqo3jdhmqed4cq6mon8r` (`organization`),
  CONSTRAINT `FKs6miydqo3jdhmqed4cq6mon8r` FOREIGN KEY (`organization`) REFERENCES `OrganizationConfig` (`idOrgConfig`),
  CONSTRAINT `FKevuul6vaa8uub91yp71l9hmhi` FOREIGN KEY (`organization`) REFERENCES `OrganizationConfig` (`idOrgConfig`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TokenBucketOrg`
--

LOCK TABLES `TokenBucketOrg` WRITE;
/*!40000 ALTER TABLE `TokenBucketOrg` DISABLE KEYS */;
/*!40000 ALTER TABLE `TokenBucketOrg` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-09-27 16:22:43
