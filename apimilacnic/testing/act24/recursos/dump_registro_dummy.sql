-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: desarrollo.lacnic.net.uy    Database: registro_dummy
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
-- Table structure for table `autonomous_system`
--

DROP TABLE IF EXISTS `autonomous_system`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `autonomous_system` (
  `asn` int(10) unsigned NOT NULL default '0',
  `id_entidade` int(11) NOT NULL default '0',
  `adm_handle` varchar(7) NOT NULL default '',
  `sec_handle` varchar(7) NOT NULL default '',
  `key_id` varchar(10) NOT NULL default '',
  `data_cadastro` datetime NOT NULL default '0000-00-00 00:00:00',
  `next_invoice_year` varchar(5) NOT NULL default '',
  `data_ultalt` datetime NOT NULL default '0000-00-00 00:00:00',
  `epp_clID` int(11) NOT NULL default '0',
  `epp_crID` int(11) NOT NULL default '0',
  PRIMARY KEY  (`asn`),
  UNIQUE KEY `autonomous_system_asn` (`asn`),
  KEY `autonomous_system_adm_handle` (`adm_handle`),
  KEY `autonomous_system_id_entidade` (`id_entidade`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autonomous_system`
--

LOCK TABLES `autonomous_system` WRITE;
/*!40000 ALTER TABLE `autonomous_system` DISABLE KEYS */;
INSERT INTO `autonomous_system` VALUES (2004,1004,'USR4','USR4','','2006-10-13 12:00:00','none','2010-03-05 14:23:59',0,0),(2011,1011,'USR11','USR11','','2006-01-31 12:00:00','none','2010-01-15 15:57:29',0,0),(2010,1010,'USR10','USR10','','2005-11-16 12:00:00','none','2010-01-15 16:13:36',0,0),(2005,1005,'USR5','USR5','','2002-09-16 12:00:00','none','2010-03-05 14:24:48',0,0),(2014,1014,'USR14','USR14','','2001-12-14 12:00:00','none','2004-06-23 12:00:00',0,0),(2002,1002,'USR2','USR2','','2001-05-25 12:00:00','none','2009-01-29 19:35:49',0,0),(2008,1008,'USR8','USR8','','2001-01-05 12:00:00','none','2003-02-27 12:00:00',0,0),(2009,1009,'USR9','USR9','','2000-10-20 12:00:00','none','2010-01-15 15:34:49',0,0),(2006,1006,'USR6','USR6','','1999-12-30 12:00:00','none','2006-12-15 12:00:00',0,0),(2013,1013,'USR13','USR13','','1999-12-28 12:00:00','none','2004-07-22 12:00:00',0,0),(2003,1003,'USR3','USR3','','1999-10-15 12:00:00','none','2001-01-26 12:00:00',0,0),(2012,1012,'USR12','USR12','','1999-06-18 12:00:00','none','2010-05-19 15:45:13',0,0),(2007,1007,'USR7','USR7','','1998-11-09 12:00:00','none','2009-03-16 20:41:17',0,0),(2015,1015,'USR15','USR15','','1998-10-02 12:00:00','none','2004-10-20 12:00:00',0,0),(2001,1001,'USR1','USR1','','1998-07-17 12:00:00','none','1998-07-17 12:00:00',0,0);
/*!40000 ALTER TABLE `autonomous_system` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blocosip`
--

DROP TABLE IF EXISTS `blocosip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blocosip` (
  `id_bloco` int(11) NOT NULL default '0',
  `version` tinyint(3) unsigned NOT NULL default '4',
  `ip_inicial` bigint(20) unsigned NOT NULL default '0',
  `ip_final` bigint(20) unsigned NOT NULL default '0',
  `id_entidade` int(11) NOT NULL default '0',
  `asn` int(10) unsigned NOT NULL default '0',
  `adm_handle` varchar(7) NOT NULL default '',
  `sec_handle` varchar(7) NOT NULL default '',
  `data_cadastro` datetime NOT NULL default '0000-00-00 00:00:00',
  `data_ultalt` datetime NOT NULL default '0000-00-00 00:00:00',
  `status` int(11) NOT NULL default '0',
  `epp_clID` int(11) NOT NULL default '0',
  `epp_crID` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id_bloco`),
  UNIQUE KEY `blocosip_id_bloco` (`id_bloco`),
  KEY `blocosip_adm_handle` (`adm_handle`),
  KEY `blocosip_id_entidade` (`id_entidade`),
  KEY `blocosip_ini` (`ip_inicial`),
  KEY `blocosip_fim` (`ip_final`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blocosip`
--

LOCK TABLES `blocosip` WRITE;
/*!40000 ALTER TABLE `blocosip` DISABLE KEYS */;
INSERT INTO `blocosip` VALUES (-11131,4,769654784,770703359,-1,0,'','','2014-09-03 13:50:00','2014-09-03 13:50:00',0,0,0),(-11130,4,765460480,767557631,-1,0,'','','2014-05-20 21:45:00','2014-05-20 21:45:00',0,0,0),(111111,6,2306139568115548160,2306139568115613695,1001,0,'USR1','USR1','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(111112,6,2306139568115613696,2306139568115679231,1002,0,'USR2','USR2','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(111113,6,2306139568115679232,2306139568115744767,1003,0,'USR3','USR3','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(111114,6,2306139568115744768,2306139568115810303,1004,0,'USR4','USR4','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(111115,6,2306139568115810304,2306139568115875839,1005,0,'USR5','USR5','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(111116,6,2306139568115875840,2306139568115941375,1006,0,'USR6','USR6','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(111117,6,2306139568115941376,2306139568116006911,1007,0,'USR7','USR7','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(111118,6,2306139568116006912,2306139568116072447,1008,0,'USR8','USR8','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(111119,6,2306139568116072448,2306139568116137983,1009,0,'USR9','USR8','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(111120,6,2306139568116137984,2306139568116203519,1010,0,'USR10','USR10','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(111121,6,2306139568116203520,2306139568116269055,1011,0,'USR11','USR11','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(111122,6,2306139568116269056,2306139568116334591,1012,0,'USR12','USR12','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(111123,6,2306139568116334592,2306139568116400127,1013,0,'USR13','USR13','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(111124,6,2306139568116400128,2306139568116465663,1014,0,'USR14','USR14','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(111125,6,2306139568116465664,2306139568116531199,1015,0,'USR15','USR14','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(111126,6,2882585236502216704,2882585236502282239,1001,0,'USR1','USR1','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(114264,6,18260090144422756352,18260090144422821887,1003,0,'USR3','USR3','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(114771,6,18302203957396897792,18302203957396963327,1002,0,'USR2','USR2','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(115603,6,18267320628835319808,18267320628835385343,1001,0,'USR1','USR1','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(115862,4,2132606976,2132672511,1015,0,'USR15','USR15','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(116202,4,2132541440,2132606975,1014,0,'USR14','USR14','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(118626,4,2132475904,2132541439,1013,0,'USR13','USR13','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(119520,4,2132410368,2132475903,1012,0,'USR12','USR12','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(120284,4,2132344832,2132410367,1011,0,'USR11','USR11','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(120298,4,2132279296,2132344831,1010,0,'USR10','USR10','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(125098,4,2132213760,2132279295,1009,0,'USR9','USR9','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(125198,4,2132148224,2132213759,1008,0,'USR8','USR8','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(125199,4,2132082688,2132148223,1007,0,'USR7','USR7','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(125200,4,2132017152,2132082687,1006,0,'USR6','USR6','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(125203,4,2131951616,2132017151,1005,0,'USR5','USR5','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(125204,4,2131886080,2131951615,1004,0,'USR4','USR4','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(125205,4,2131820544,2131886079,1003,0,'USR3','USR3','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(125207,4,2131755008,2131820543,1002,0,'USR2','USR2','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(125241,4,2131689472,2131755007,1001,0,'USR1','USR1','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(125242,4,2131623936,2131689471,1015,0,'USR15','USR15','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(125243,4,2131558400,2131623935,1014,0,'USR14','USR14','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(125244,4,2131492864,2131558399,1013,0,'USR13','USR13','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(129422,4,2131427328,2131492863,1012,0,'USR12','USR12','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(129736,4,2131361792,2131427327,1011,0,'USR11','USR11','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(129737,4,2131296256,2131361791,1010,0,'USR10','USR10','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(130237,4,2131230720,2131296255,1009,0,'USR9','USR9','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(130238,4,2131165184,2131230719,1008,0,'USR8','USR8','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(130945,4,2131099648,2131165183,1007,0,'USR7','USR7','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(131172,4,2131034112,2131099647,1006,0,'USR6','USR6','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(131458,4,2130968576,2131034111,1005,0,'USR5','USR5','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(131482,4,2130903040,2130968575,1004,0,'USR4','USR4','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(132884,4,2130837504,2130903039,1003,0,'USR3','USR3','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(134928,4,2130771968,2130837503,1002,0,'USR2','USR2','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(138784,4,2130706432,2130771967,1001,0,'USR1','USR1','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(140000,4,167772160,167837695,1001,0,'USR1','USR1','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(140001,4,167837696,167903231,1002,0,'USR2','USR2','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(140002,4,167903232,167968767,1003,0,'USR3','USR3','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(140003,4,167968768,168034303,1004,0,'USR4','USR4','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(140004,4,168034304,168099839,1005,0,'USR5','USR5','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(140005,4,168099840,168165375,1006,0,'USR6','USR6','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(140006,4,168165376,168230911,1007,0,'USR7','USR7','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(140007,4,168230912,168296447,1008,0,'USR8','USR8','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(140008,4,168296448,168361983,1009,0,'USR9','USR9','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(140009,4,168361984,168427519,1010,0,'USR10','USR10','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(140010,4,168427520,168493055,1011,0,'USR11','USR11','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(140011,4,168493056,168558591,1012,0,'USR12','USR12','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(140012,4,168558592,168624127,1013,0,'USR13','USR13','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(140013,4,168624128,168689663,1014,0,'USR14','USR14','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0),(140014,4,168689664,168755199,1015,0,'USR15','USR15','2010-12-03 12:09:16','2010-12-03 12:09:16',0,0,0);
/*!40000 ALTER TABLE `blocosip` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `designacoes`
--

DROP TABLE IF EXISTS `designacoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `designacoes` (
  `id_bloco` int(11) NOT NULL default '0',
  `id_bloco_designado` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id_bloco`,`id_bloco_designado`),
  KEY `designacoes_id_bloco_designado` (`id_bloco_designado`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `designacoes`
--

LOCK TABLES `designacoes` WRITE;
/*!40000 ALTER TABLE `designacoes` DISABLE KEYS */;
/*!40000 ALTER TABLE `designacoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entidade`
--

DROP TABLE IF EXISTS `entidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entidade` (
  `id_entidade` int(11) NOT NULL auto_increment,
  `nome` varchar(100) NOT NULL default '',
  `id_documento` varchar(30) NOT NULL default '',
  `tipo_documento` varchar(10) NOT NULL default '',
  `nome_contato` varchar(40) NOT NULL default '',
  `tel_ddi` varchar(4) NOT NULL default '',
  `tel_ddd` varchar(4) NOT NULL default '',
  `tel_numero` varchar(14) NOT NULL default '',
  `tel_ramal` varchar(8) NOT NULL default '',
  `end_logradouro` varchar(130) NOT NULL default '',
  `end_numero` varchar(5) NOT NULL default '',
  `end_complemento` varchar(40) NOT NULL default '',
  `end_cidade` varchar(60) NOT NULL default '',
  `end_uf` varchar(30) NOT NULL default '',
  `end_pais` char(2) NOT NULL default '',
  `end_cep` varchar(9) NOT NULL default '',
  `adm_handle` varchar(7) NOT NULL default '',
  `cob_handle` varchar(7) NOT NULL default '',
  `mem_handle` varchar(7) NOT NULL default '',
  `status_tel` tinyint(4) NOT NULL default '0',
  `status_end` tinyint(4) NOT NULL default '0',
  `data_cadastro` datetime NOT NULL default '0000-00-00 00:00:00',
  `data_ultalt` datetime NOT NULL default '0000-00-00 00:00:00',
  `data_renovacao` datetime NOT NULL default '0000-00-00 00:00:00',
  `tipo_renovacao` int(11) NOT NULL default '0',
  `doc_recebido` int(11) NOT NULL default '0',
  `recursos` int(11) NOT NULL default '0',
  `data_expiracao` datetime NOT NULL default '0000-00-00 00:00:00',
  `org_type` tinyint(4) NOT NULL default '0',
  `epp_login` int(11) NOT NULL default '0',
  `epp_status` int(11) NOT NULL default '0',
  `epp_password` varchar(74) NOT NULL default '',
  `epp_ip` varchar(45) NOT NULL default '',
  `epp_clID` int(11) NOT NULL default '0',
  `epp_crID` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id_entidade`),
  UNIQUE KEY `id_entidade_entidade` (`id_entidade`),
  KEY `entidade_adm_handle` (`adm_handle`),
  KEY `id_documento_entidade` (`id_documento`)
) ENGINE=InnoDB AUTO_INCREMENT=1016 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entidade`
--

LOCK TABLES `entidade` WRITE;
/*!40000 ALTER TABLE `entidade` DISABLE KEYS */;
INSERT INTO `entidade` VALUES (-1,'LACNIC','LACNIC','','','','','','','','','','','','UY','','LACNIC','','',0,0,'2008-01-01 00:00:00','2008-01-01 00:00:00','2008-01-01 00:00:00',0,0,0,'2008-01-01 00:00:00',0,0,0,'','',0,0),(1001,'Organización 1','AR-ORG1-LACNIC','','Administrador 1','54','11','1111','','calle','1111','','Buenos Aires','','AR','1234','USR1','USR1','USR1',0,0,'2002-09-11 12:00:00','2010-12-04 02:10:00','1998-11-25 12:00:00',128,1130,0,'2002-09-11 00:00:00',0,0,0,'','',0,0),(1002,'Organización 2','UY-ORG2-LACNIC','','Administrador 2','598','2','1111','','calle','1111','','Montevideo','','UY','1234','USR2','USR2','USR2',0,0,'2002-09-12 12:00:00','2008-02-16 12:00:00','1999-01-28 12:00:00',64,1130,0,'2002-09-12 00:00:00',0,0,0,'','',0,0),(1003,'Organización 3','CO-ORG3-LACNIC','','Administrador 3','57','1','1111','','calle','1111','','Bogota','DC','CO','1234','USR3','USR3','USR3',0,0,'2002-09-17 12:00:00','2010-01-22 15:07:47','2001-07-19 12:00:00',64,1130,0,'2002-09-17 00:00:00',0,0,0,'','',0,0),(1004,'Organización 4','AR-ORG4-LACNIC','','Administrador 4','54','11','1111','','calle','1111','','Buenos Aires','','AR','1234','USR4','USR4','USR4',0,0,'2002-10-07 12:00:00','2010-09-24 03:10:00','2001-05-30 12:00:00',64,1130,0,'2002-10-07 00:00:00',0,0,0,'','',0,0),(1005,'Organización 5','CO-ORG5-LACNIC','','Administrador 5','57','1','1111','','calle','1111','','Bogota','Cu','CO','1234','USR5','USR5','USR5',0,0,'2002-10-09 12:00:00','2007-02-10 12:00:00','2001-09-18 12:00:00',64,1130,0,'2002-10-09 00:00:00',0,0,0,'','',0,0),(1006,'Organización 6','VE-ORG6-LACNIC','','Administrador 6','58','212','1111','','calle','1111','','Caracas','MI','VE','1234','USR6','USR6','USR6',0,0,'2002-10-31 12:00:00','2007-06-14 12:00:00','1998-03-17 12:00:00',128,1130,0,'2002-10-31 00:00:00',0,0,0,'','',0,0),(1007,'Organización 7','CU-ORG7-LACNIC','','Administrador 7','54','11','1111','','calle','1111','','La Habana','','CU','1234','USR7','USR7','USR7',0,0,'2002-11-07 12:00:00','2010-05-20 19:53:42','1998-10-09 03:00:00',64,1130,0,'2002-11-07 00:00:00',0,0,0,'','',0,0),(1008,'Organización 8','PE-ORG8-LACNIC','','Administrador 8','51','1','1111','','calle','1111','','Lima','LI','PE','1234','USR8','USR8','USR8',0,0,'2002-11-21 12:00:00','2005-09-05 12:00:00','1999-03-15 12:00:00',64,1130,0,'2002-11-21 00:00:00',0,0,0,'','',0,0),(1009,'Organización 9','DO-ORG9-LACNIC','','Administrador 9','1','809','1111','','calle','1111','','Santo Domingo','DN','DO','1234','USR9','USR9','USR9',0,0,'2002-11-27 12:00:00','2010-06-24 14:24:10','2002-08-21 03:00:00',64,1130,0,'2002-11-27 00:00:00',0,0,0,'','',0,0),(1010,'Organización 10','CO-ORG10-LACNIC','','Administrador 10','57','4','1111','','calle','1111','','Medellin','CO','CO','1234','USR10','USR10','USR10',0,0,'2003-01-21 12:00:00','2007-01-03 12:00:00','2000-07-05 12:00:00',64,1130,0,'2003-01-21 00:00:00',0,0,0,'','',0,0),(1011,'Organización 11','EC-ORG11-LACNIC','','Administrador 11','593','4','1111','','calle','1111','','Guayaquil','Gu','EC','1234','USR11','USR11','USR11',0,0,'2003-02-26 12:00:00','2009-04-07 03:10:06','2002-04-05 12:00:00',64,1130,0,'2003-02-26 00:00:00',0,0,0,'','',0,0),(1012,'Organización 12','BR-ORG12-LACNIC','','Administrador 12','54','11','1111','','calle','1111','','Sao Paulo','SP','BR','1234','USR12','USR12','USR12',0,0,'2003-04-15 12:00:00','2010-05-06 19:02:37','2000-06-30 12:00:00',64,1130,0,'2003-04-15 00:00:00',0,0,0,'','',0,0),(1013,'Organización 13','AR-ORG13-LACNIC','','Administrador 13','54','','1111','','calle','1111','','Buenos Aires','CF','AR','1234','USR1','USR1','USR1',0,0,'2003-09-16 12:00:00','2010-10-18 17:33:53','2004-03-17 03:00:00',128,1130,0,'2003-09-16 00:00:00',0,0,0,'','',0,0),(1014,'Organización 14','PA-ORG14-LACNIC','','Administrador 14','507','','1111','','calle','1111','','Panama','-','PA','1234','USR2','USR2','USR2',0,0,'2003-10-28 12:00:00','2006-04-24 12:00:00','2003-12-26 12:00:00',64,1130,0,'2003-10-28 00:00:00',0,0,0,'','',0,0),(1015,'Organización 15','CL-ORG15-LACNIC','','Administrador 15','56','2','1111','','calle','1111','','SANTIAGO','RM','CL','1234','USR3','USR3','USR3',0,0,'2010-01-27 12:23:37','2010-02-19 02:11:50','2002-06-08 12:00:00',64,1130,0,'2010-01-27 12:23:37',0,0,111,'','',0,0);
/*!40000 ALTER TABLE `entidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `handle_usuario` varchar(7) NOT NULL default '',
  `nome` varchar(40) NOT NULL default '',
  `senha` varchar(32) NOT NULL default '',
  `lembrete` varchar(30) NOT NULL default '',
  `email` varchar(50) NOT NULL default '',
  `tel_ddi` varchar(4) NOT NULL default '',
  `tel_ddd` varchar(4) NOT NULL default '',
  `tel_numero` varchar(14) NOT NULL default '',
  `tel_ramal` varchar(8) NOT NULL default '',
  `end_logradouro` varchar(130) NOT NULL default '',
  `end_numero` varchar(5) NOT NULL default '',
  `end_complemento` varchar(40) NOT NULL default '',
  `end_cidade` varchar(60) NOT NULL default '',
  `end_uf` varchar(30) NOT NULL default '',
  `end_pais` char(2) NOT NULL default '',
  `end_cep` varchar(9) NOT NULL default '',
  `data_cadastro` datetime NOT NULL default '0000-00-00 00:00:00',
  `data_ultalt` datetime NOT NULL default '0000-00-00 00:00:00',
  `pri_nome` varchar(20) NOT NULL default '',
  `ult_nome` varchar(20) NOT NULL default '',
  `propriedades` tinyint(4) NOT NULL default '0',
  `idioma` char(2) NOT NULL default '',
  `id_certificate` int(11) NOT NULL default '0',
  `certificateOnly` tinyint(4) NOT NULL default '0',
  `epp_clID` int(11) NOT NULL default '0',
  `epp_crID` int(11) NOT NULL default '0',
  PRIMARY KEY  (`handle_usuario`),
  UNIQUE KEY `usuario_handle_usuario` (`handle_usuario`),
  KEY `usuario_email` (`email`),
  KEY `usuario_pri_ult_nome` (`pri_nome`,`ult_nome`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('USR1','Usuario 1','a6327a1e7df0b7c0643b3b73467c5692','USR1','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','57','1','123456','','calle','1111','','Bogota','DC','CO','1111','2002-09-09 12:00:00','2010-01-25 23:19:37','Usuario','15',0,'PT',0,0,0,0),('USR2','Usuario 2','25feba912ad4915545dfd94a7170e8fc','USR2','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','54','11','123456','','calle','1111','','Buenos Aires','AR','AR','1111','2002-09-09 12:00:00','2010-12-09 18:18:16','Usuario','8',0,'EN',0,0,0,0),('USR3','Usuario 3','42196e33bb9f4b8f1fabbf38ca30ae60','USR3','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','598','2','123456','','calle','1111','','Montevideo','UY','UY','1111','2002-09-10 12:00:00','2006-11-27 12:00:00','Usuario','7',0,'SP',0,0,0,0),('USR4','Usuario 4','44be69dfe83ab4dcc8207a212ab93af5','USR4','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','58','212','123456','','calle','1111','','Caracas','MI','VE','1111','2002-09-11 12:00:00','2004-08-18 12:00:00','Usuario','6',0,'PT',0,0,0,0),('USR5','Usuario 5','abc7c09f102530cf115ed4368d2b807b','USR5','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','56','2','123456','','calle','1111','','Santiago','Me','CL','1111','2002-09-17 12:00:00','2002-09-17 12:00:00','Usuario','5',0,'EN',0,0,0,0),('USR6','Usuario 6','6aa3db66e051f0f9557d331d25eadb53','USR6','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','51','1','123456','','calle','1111','','Lima','LI','PE','1111','2002-09-26 12:00:00','2003-08-29 12:00:00','Usuario','4',0,'SP',0,0,0,0),('USR7','Usuario 7','a2fb3881ea0f68d94e296949648e8f0f','USR7','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','54','11','123456','','calle','1111','','La Habana','LH','CU','1111','2002-11-05 12:00:00','2010-05-20 19:51:58','Usuario','3',0,'PT',0,0,0,0),('USR8','Usuario 8','11d5e158774b623de128ea6907f46ed9','USR8','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','1','809','123456','','calle','1111','','Santo Domingo','DN','DO','1111','2002-11-27 12:00:00','2009-10-19 17:35:46','Usuario','2',0,'EN',0,0,0,0),('USR9','Usuario 9','2dda15d7657c827625420a8826b390da','USR9','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','593','4','123456','','calle','1111','','Guayaquil','GU','EC','1111','2002-12-11 12:00:00','2009-10-30 16:35:03','Usuario','1',0,'SP',0,0,0,0),('USR10','Usuario 10','78cd9315e18d7a654cb1277f600adb54','USR10','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','57','4','123456','','calle','1111','','Medellin','An','CO','1111','2003-01-20 12:00:00','2010-10-25 11:41:38','Usuario','14',0,'EN',0,0,0,0),('USR11','Usuario 11','d54405c03a587d77b864cf13b9546fc9','USR11','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','54','11','123456','','calle','1111','','Bogotá','Cu','CO','1111','2003-02-04 12:00:00','2009-02-02 18:55:49','Usuario','13',0,'SP',0,0,0,0),('USR12','Usuario 12','a66ec73496d2955a1f76a99ff3430564','USR12','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','57','1','123456','','calle','1111','','Sao Paulo','SP','BR','1111','2003-02-24 12:00:00','2006-02-24 12:00:00','Usuario','12',0,'PT',0,0,0,0),('USR13','Usuario 13','52aeeec5770e3412467898a0d892211f','USR13','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','507','882','123456','','calle','1111','','Panama','PA','PA','1111','2003-04-16 12:00:00','2003-09-15 12:00:00','Usuario','11',0,'EN',0,0,0,0),('USR14','Usuario 14','d109cc6ff018efa98478c2486d6e5663','USR14','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','54','11','123456','','calle','1111','','Capital Federal','AR','AR','1111','2003-06-18 12:00:00','2010-08-09 12:02:25','Usuario','10',0,'SP',0,0,0,0),('USR15','Usuario 15','122bea2817606f9742ac7f08c6a1fa07','USR15','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','54','11','123456','','calle','1111','','Buenos Aires','AR','AR','1111','2006-11-21 12:00:00','2010-04-21 12:25:31','Usuario','9',0,'PT',0,0,0,0),('LACNIC','','','','q0f5j4y3n6h8a0x2@lacnic-eng.slack.com','','','','','','','','','','','','2008-01-01 00:00:00','2008-01-01 00:00:00','','',0,'',0,0,0,0);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-09-27 12:13:06
