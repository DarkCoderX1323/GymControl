CREATE DATABASE  IF NOT EXISTS `gymcontrol` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gymcontrol`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: gymcontrol
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `asistencia`
--

DROP TABLE IF EXISTS `asistencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asistencia` (
  `id` int NOT NULL AUTO_INCREMENT,
  `socio_id` int NOT NULL,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `registrado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `socio_id` (`socio_id`),
  KEY `idx_asistencia_fecha` (`fecha`),
  CONSTRAINT `asistencia_ibfk_1` FOREIGN KEY (`socio_id`) REFERENCES `socio` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=247 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asistencia`
--

LOCK TABLES `asistencia` WRITE;
/*!40000 ALTER TABLE `asistencia` DISABLE KEYS */;
INSERT INTO `asistencia` VALUES (15,3,'2026-07-12','17:45:00','2026-07-14 04:43:28'),(16,3,'2026-07-10','13:30:00','2026-07-14 04:43:28'),(17,3,'2026-07-09','18:15:00','2026-07-14 04:43:28'),(18,3,'2026-07-08','19:30:00','2026-07-14 04:43:28'),(19,3,'2026-07-06','18:45:00','2026-07-14 04:43:28'),(20,3,'2026-07-03','16:15:00','2026-07-14 04:43:28'),(21,3,'2026-06-30','10:45:00','2026-07-14 04:43:28'),(22,3,'2026-06-29','07:30:00','2026-07-14 04:43:28'),(23,3,'2026-06-27','09:30:00','2026-07-14 04:43:28'),(24,3,'2026-06-26','11:30:00','2026-07-14 04:43:28'),(25,3,'2026-06-24','20:00:00','2026-07-14 04:43:28'),(26,3,'2026-06-22','08:15:00','2026-07-14 04:43:28'),(27,5,'2026-07-09','07:45:00','2026-07-14 04:43:28'),(28,5,'2026-07-07','12:30:00','2026-07-14 04:43:28'),(29,5,'2026-07-01','14:45:00','2026-07-14 04:43:28'),(30,6,'2026-07-13','19:45:00','2026-07-14 04:43:28'),(31,6,'2026-07-12','13:00:00','2026-07-14 04:43:28'),(32,6,'2026-07-07','11:30:00','2026-07-14 04:43:28'),(33,6,'2026-07-01','18:45:00','2026-07-14 04:43:28'),(34,6,'2026-06-30','19:45:00','2026-07-14 04:43:28'),(35,6,'2026-06-25','14:15:00','2026-07-14 04:43:28'),(36,7,'2026-07-13','16:45:00','2026-07-14 04:43:28'),(37,7,'2026-07-06','17:15:00','2026-07-14 04:43:28'),(38,7,'2026-07-05','19:45:00','2026-07-14 04:43:28'),(39,7,'2026-07-03','20:15:00','2026-07-14 04:43:28'),(40,7,'2026-07-01','15:00:00','2026-07-14 04:43:28'),(41,7,'2026-06-30','20:45:00','2026-07-14 04:43:28'),(42,7,'2026-06-28','15:00:00','2026-07-14 04:43:28'),(43,8,'2026-07-03','12:15:00','2026-07-14 04:43:28'),(44,9,'2026-07-13','06:30:00','2026-07-14 04:43:28'),(45,9,'2026-07-12','12:30:00','2026-07-14 04:43:28'),(46,11,'2026-07-03','10:45:00','2026-07-14 04:43:28'),(47,11,'2026-07-01','10:00:00','2026-07-14 04:43:28'),(48,11,'2026-06-29','13:00:00','2026-07-14 04:43:28'),(49,12,'2026-07-13','16:00:00','2026-07-14 04:43:28'),(50,12,'2026-07-12','15:15:00','2026-07-14 04:43:28'),(51,12,'2026-07-11','13:30:00','2026-07-14 04:43:28'),(52,12,'2026-07-09','18:30:00','2026-07-14 04:43:28'),(53,12,'2026-07-07','08:00:00','2026-07-14 04:43:28'),(54,12,'2026-07-06','18:15:00','2026-07-14 04:43:28'),(55,12,'2026-07-02','10:00:00','2026-07-14 04:43:28'),(56,12,'2026-06-28','15:00:00','2026-07-14 04:43:28'),(57,12,'2026-06-26','20:30:00','2026-07-14 04:43:28'),(58,12,'2026-06-24','15:45:00','2026-07-14 04:43:28'),(59,12,'2026-06-23','12:15:00','2026-07-14 04:43:28'),(60,15,'2026-06-25','17:15:00','2026-07-14 04:43:28'),(61,16,'2026-07-04','19:00:00','2026-07-14 04:43:28'),(62,17,'2026-07-13','07:45:00','2026-07-14 04:43:28'),(63,17,'2026-07-12','11:45:00','2026-07-14 04:43:28'),(64,17,'2026-07-11','17:15:00','2026-07-14 04:43:28'),(65,17,'2026-07-03','12:15:00','2026-07-14 04:43:28'),(66,17,'2026-07-02','17:30:00','2026-07-14 04:43:28'),(67,17,'2026-06-30','15:45:00','2026-07-14 04:43:28'),(68,17,'2026-06-28','13:45:00','2026-07-14 04:43:28'),(69,17,'2026-06-27','19:30:00','2026-07-14 04:43:28'),(70,17,'2026-06-26','11:15:00','2026-07-14 04:43:28'),(71,17,'2026-06-25','19:00:00','2026-07-14 04:43:28'),(72,17,'2026-06-23','10:45:00','2026-07-14 04:43:28'),(73,17,'2026-06-22','09:45:00','2026-07-14 04:43:28'),(74,18,'2026-07-13','11:15:00','2026-07-14 04:43:28'),(75,18,'2026-07-11','13:15:00','2026-07-14 04:43:28'),(76,18,'2026-07-10','11:30:00','2026-07-14 04:43:28'),(77,18,'2026-07-09','11:30:00','2026-07-14 04:43:28'),(78,18,'2026-07-08','20:30:00','2026-07-14 04:43:28'),(79,18,'2026-07-07','14:00:00','2026-07-14 04:43:28'),(80,20,'2026-07-13','07:15:00','2026-07-14 04:43:28'),(81,21,'2026-07-13','07:30:00','2026-07-14 04:43:28'),(82,21,'2026-07-12','09:45:00','2026-07-14 04:43:28'),(83,21,'2026-07-10','17:15:00','2026-07-14 04:43:28'),(84,21,'2026-07-09','10:30:00','2026-07-14 04:43:28'),(85,21,'2026-07-08','13:30:00','2026-07-14 04:43:28'),(86,22,'2026-07-05','10:15:00','2026-07-14 04:43:28'),(87,22,'2026-07-04','07:15:00','2026-07-14 04:43:28'),(88,22,'2026-07-03','11:00:00','2026-07-14 04:43:28'),(89,22,'2026-07-02','17:15:00','2026-07-14 04:43:28'),(90,22,'2026-06-29','09:15:00','2026-07-14 04:43:28'),(91,22,'2026-06-26','17:45:00','2026-07-14 04:43:28'),(92,23,'2026-07-05','15:30:00','2026-07-14 04:43:28'),(93,23,'2026-07-04','07:15:00','2026-07-14 04:43:28'),(94,23,'2026-07-02','10:15:00','2026-07-14 04:43:28'),(95,23,'2026-07-01','11:15:00','2026-07-14 04:43:28'),(96,24,'2026-07-13','06:00:00','2026-07-14 04:43:28'),(97,24,'2026-07-12','14:30:00','2026-07-14 04:43:28'),(98,24,'2026-07-11','17:15:00','2026-07-14 04:43:28'),(99,24,'2026-07-09','16:45:00','2026-07-14 04:43:28'),(100,25,'2026-07-13','15:30:00','2026-07-14 04:43:28'),(101,26,'2026-07-12','13:30:00','2026-07-14 04:43:28'),(102,27,'2026-07-12','19:45:00','2026-07-14 04:43:28'),(103,27,'2026-07-05','07:00:00','2026-07-14 04:43:28'),(104,28,'2026-07-12','08:15:00','2026-07-14 04:43:28'),(105,28,'2026-07-11','18:30:00','2026-07-14 04:43:28'),(106,28,'2026-06-28','07:15:00','2026-07-14 04:43:28'),(107,28,'2026-06-25','07:45:00','2026-07-14 04:43:28'),(108,28,'2026-06-23','15:15:00','2026-07-14 04:43:28'),(109,28,'2026-06-22','18:45:00','2026-07-14 04:43:28'),(110,29,'2026-07-12','18:15:00','2026-07-14 04:43:28'),(111,29,'2026-07-10','16:15:00','2026-07-14 04:43:28'),(112,29,'2026-07-04','10:00:00','2026-07-14 04:43:28'),(113,29,'2026-06-30','08:15:00','2026-07-14 04:43:28'),(114,29,'2026-06-29','08:00:00','2026-07-14 04:43:28'),(115,29,'2026-06-25','08:00:00','2026-07-14 04:43:28'),(116,29,'2026-06-24','12:45:00','2026-07-14 04:43:28'),(117,30,'2026-07-12','16:15:00','2026-07-14 04:43:28'),(118,30,'2026-07-11','12:00:00','2026-07-14 04:43:28'),(119,30,'2026-07-06','14:15:00','2026-07-14 04:43:28'),(120,30,'2026-07-05','16:15:00','2026-07-14 04:43:28'),(121,30,'2026-07-04','20:30:00','2026-07-14 04:43:28'),(122,30,'2026-06-29','19:15:00','2026-07-14 04:43:28'),(123,30,'2026-06-28','07:00:00','2026-07-14 04:43:28'),(124,30,'2026-06-25','08:30:00','2026-07-14 04:43:28'),(125,30,'2026-06-24','15:30:00','2026-07-14 04:43:28'),(126,30,'2026-06-23','13:00:00','2026-07-14 04:43:28'),(127,30,'2026-06-22','13:30:00','2026-07-14 04:43:28'),(128,31,'2026-07-13','19:00:00','2026-07-14 04:43:28'),(129,31,'2026-07-12','18:30:00','2026-07-14 04:43:28'),(130,31,'2026-07-10','15:00:00','2026-07-14 04:43:28'),(131,31,'2026-07-09','18:15:00','2026-07-14 04:43:28'),(132,31,'2026-07-08','13:45:00','2026-07-14 04:43:28'),(133,31,'2026-07-07','20:30:00','2026-07-14 04:43:28'),(134,31,'2026-07-06','08:45:00','2026-07-14 04:43:28'),(135,31,'2026-07-05','16:45:00','2026-07-14 04:43:28'),(136,31,'2026-07-04','07:45:00','2026-07-14 04:43:28'),(137,31,'2026-07-03','11:45:00','2026-07-14 04:43:28'),(138,31,'2026-07-02','11:30:00','2026-07-14 04:43:28'),(139,32,'2026-07-12','13:00:00','2026-07-14 04:43:28'),(140,32,'2026-07-10','11:30:00','2026-07-14 04:43:28'),(141,32,'2026-07-08','11:00:00','2026-07-14 04:43:28'),(142,32,'2026-07-04','18:45:00','2026-07-14 04:43:28'),(143,32,'2026-07-03','19:00:00','2026-07-14 04:43:28'),(144,32,'2026-07-01','16:45:00','2026-07-14 04:43:28'),(145,32,'2026-06-30','12:00:00','2026-07-14 04:43:28'),(146,32,'2026-06-28','09:30:00','2026-07-14 04:43:28'),(147,32,'2026-06-26','15:45:00','2026-07-14 04:43:28'),(148,32,'2026-06-22','16:45:00','2026-07-14 04:43:28'),(149,33,'2026-07-13','14:15:00','2026-07-14 04:43:28'),(150,33,'2026-07-12','20:30:00','2026-07-14 04:43:28'),(151,33,'2026-07-11','13:45:00','2026-07-14 04:43:28'),(152,34,'2026-07-13','16:15:00','2026-07-14 04:43:28'),(153,35,'2026-07-13','07:15:00','2026-07-14 04:43:28'),(154,35,'2026-07-12','13:30:00','2026-07-14 04:43:28'),(155,35,'2026-07-11','14:30:00','2026-07-14 04:43:28'),(156,35,'2026-07-10','12:45:00','2026-07-14 04:43:28'),(157,35,'2026-07-09','13:15:00','2026-07-14 04:43:28'),(158,35,'2026-07-07','13:15:00','2026-07-14 04:43:28'),(159,35,'2026-07-06','12:15:00','2026-07-14 04:43:28'),(160,36,'2026-07-13','13:30:00','2026-07-14 04:43:28'),(161,36,'2026-07-11','11:45:00','2026-07-14 04:43:28'),(162,36,'2026-07-09','13:30:00','2026-07-14 04:43:28'),(163,36,'2026-07-05','19:15:00','2026-07-14 04:43:28'),(164,36,'2026-07-04','17:15:00','2026-07-14 04:43:28'),(165,36,'2026-07-03','15:45:00','2026-07-14 04:43:28'),(166,36,'2026-06-30','09:45:00','2026-07-14 04:43:28'),(167,36,'2026-06-29','06:30:00','2026-07-14 04:43:28'),(168,36,'2026-06-28','17:45:00','2026-07-14 04:43:28'),(169,36,'2026-06-27','17:45:00','2026-07-14 04:43:28'),(170,36,'2026-06-26','12:15:00','2026-07-14 04:43:28'),(171,36,'2026-06-25','13:00:00','2026-07-14 04:43:28'),(172,36,'2026-06-24','08:30:00','2026-07-14 04:43:28'),(173,36,'2026-06-22','19:00:00','2026-07-14 04:43:28'),(174,37,'2026-07-13','16:00:00','2026-07-14 04:43:28'),(175,37,'2026-07-11','19:30:00','2026-07-14 04:43:28'),(176,37,'2026-07-10','19:45:00','2026-07-14 04:43:28'),(177,37,'2026-07-09','11:45:00','2026-07-14 04:43:28'),(178,37,'2026-07-05','19:00:00','2026-07-14 04:43:28'),(179,37,'2026-07-03','15:00:00','2026-07-14 04:43:28'),(180,37,'2026-07-01','09:30:00','2026-07-14 04:43:28'),(181,37,'2026-06-30','09:00:00','2026-07-14 04:43:28'),(182,37,'2026-06-29','12:00:00','2026-07-14 04:43:28'),(183,37,'2026-06-28','18:00:00','2026-07-14 04:43:28'),(184,37,'2026-06-27','13:15:00','2026-07-14 04:43:28'),(185,37,'2026-06-24','17:30:00','2026-07-14 04:43:28'),(186,37,'2026-06-23','20:00:00','2026-07-14 04:43:28'),(187,40,'2026-07-13','09:45:00','2026-07-14 04:43:28'),(188,40,'2026-07-11','15:15:00','2026-07-14 04:43:28'),(189,40,'2026-07-09','08:15:00','2026-07-14 04:43:28'),(190,40,'2026-07-08','07:45:00','2026-07-14 04:43:28'),(191,40,'2026-07-07','15:15:00','2026-07-14 04:43:28'),(192,42,'2026-07-13','07:45:00','2026-07-14 04:43:28'),(193,42,'2026-07-09','11:30:00','2026-07-14 04:43:28'),(194,42,'2026-07-08','16:45:00','2026-07-14 04:43:28'),(195,42,'2026-07-06','17:30:00','2026-07-14 04:43:28'),(196,42,'2026-07-05','13:30:00','2026-07-14 04:43:28'),(197,42,'2026-07-04','09:45:00','2026-07-14 04:43:28'),(198,42,'2026-06-29','19:45:00','2026-07-14 04:43:28'),(199,43,'2026-07-06','12:30:00','2026-07-14 04:43:28'),(200,45,'2026-07-13','19:30:00','2026-07-14 04:43:28'),(201,45,'2026-07-12','18:15:00','2026-07-14 04:43:28'),(202,45,'2026-07-05','15:30:00','2026-07-14 04:43:28'),(203,45,'2026-07-04','09:15:00','2026-07-14 04:43:28'),(204,45,'2026-07-01','15:30:00','2026-07-14 04:43:28'),(205,45,'2026-06-28','16:15:00','2026-07-14 04:43:28'),(206,45,'2026-06-25','16:00:00','2026-07-14 04:43:28'),(207,45,'2026-06-24','20:00:00','2026-07-14 04:43:28'),(208,45,'2026-06-22','10:45:00','2026-07-14 04:43:28'),(209,47,'2026-07-11','20:30:00','2026-07-14 04:43:28'),(210,47,'2026-07-09','14:30:00','2026-07-14 04:43:28'),(211,47,'2026-07-08','19:15:00','2026-07-14 04:43:28'),(212,47,'2026-07-07','10:45:00','2026-07-14 04:43:28'),(213,47,'2026-07-04','18:30:00','2026-07-14 04:43:28'),(214,47,'2026-07-03','17:30:00','2026-07-14 04:43:28'),(215,47,'2026-07-02','18:00:00','2026-07-14 04:43:28'),(216,47,'2026-06-30','13:00:00','2026-07-14 04:43:28'),(217,47,'2026-06-26','08:15:00','2026-07-14 04:43:28'),(218,48,'2026-07-13','13:00:00','2026-07-14 04:43:28'),(219,48,'2026-07-12','15:30:00','2026-07-14 04:43:28'),(220,48,'2026-07-10','20:15:00','2026-07-14 04:43:28'),(221,48,'2026-07-09','16:00:00','2026-07-14 04:43:28'),(222,48,'2026-07-08','16:45:00','2026-07-14 04:43:28'),(223,48,'2026-07-07','20:30:00','2026-07-14 04:43:28'),(224,48,'2026-07-06','16:45:00','2026-07-14 04:43:28'),(225,48,'2026-07-05','07:15:00','2026-07-14 04:43:28'),(226,48,'2026-07-04','06:00:00','2026-07-14 04:43:28'),(227,49,'2026-07-12','08:45:00','2026-07-14 04:43:28'),(228,49,'2026-07-10','13:30:00','2026-07-14 04:43:28'),(229,49,'2026-07-06','16:45:00','2026-07-14 04:43:28'),(230,49,'2026-07-05','15:15:00','2026-07-14 04:43:28'),(231,50,'2026-07-12','10:15:00','2026-07-14 04:43:28'),(232,50,'2026-07-10','07:45:00','2026-07-14 04:43:28'),(233,50,'2026-07-07','07:15:00','2026-07-14 04:43:28'),(234,50,'2026-07-06','16:00:00','2026-07-14 04:43:28'),(235,50,'2026-07-05','06:30:00','2026-07-14 04:43:28'),(236,50,'2026-07-02','09:15:00','2026-07-14 04:43:28'),(237,50,'2026-07-01','18:15:00','2026-07-14 04:43:28'),(238,50,'2026-06-30','07:15:00','2026-07-14 04:43:28'),(239,50,'2026-06-29','15:15:00','2026-07-14 04:43:28'),(240,50,'2026-06-28','19:15:00','2026-07-14 04:43:28'),(241,50,'2026-06-26','11:15:00','2026-07-14 04:43:28'),(242,50,'2026-06-24','18:00:00','2026-07-14 04:43:28'),(243,50,'2026-06-23','10:15:00','2026-07-14 04:43:28'),(244,50,'2026-06-22','08:30:00','2026-07-14 04:43:28'),(245,54,'2026-07-14','17:00:57','2026-07-14 22:00:57'),(246,54,'2026-07-16','21:00:25','2026-07-17 02:00:25');
/*!40000 ALTER TABLE `asistencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `membresia`
--

DROP TABLE IF EXISTS `membresia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `membresia` (
  `id` int NOT NULL AUTO_INCREMENT,
  `socio_id` int NOT NULL,
  `tipo` varchar(50) DEFAULT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `estado` enum('activa','vencida') DEFAULT 'activa',
  `tipo_membresia_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `socio_id` (`socio_id`),
  KEY `fk_tipo_membresia` (`tipo_membresia_id`),
  CONSTRAINT `fk_tipo_membresia` FOREIGN KEY (`tipo_membresia_id`) REFERENCES `tipo_membresia` (`id`),
  CONSTRAINT `membresia_ibfk_1` FOREIGN KEY (`socio_id`) REFERENCES `socio` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membresia`
--

LOCK TABLES `membresia` WRITE;
/*!40000 ALTER TABLE `membresia` DISABLE KEYS */;
INSERT INTO `membresia` VALUES (3,3,'Anual','2026-06-02','2027-06-02','activa',3),(5,5,'Mensual','2026-06-25','2026-07-25','activa',1),(6,6,'Trimestral','2026-05-21','2026-08-19','activa',2),(7,7,'Anual','2026-03-31','2027-03-31','activa',3),(8,8,'Mensual','2026-07-01','2026-07-31','activa',1),(9,9,'Mensual','2026-07-11','2026-08-10','activa',1),(10,10,'Trimestral','2026-03-04','2026-06-02','vencida',2),(11,11,'Mensual','2026-06-27','2026-07-27','activa',1),(12,12,'Anual','2026-06-08','2027-06-08','activa',3),(13,13,'Anual','2026-05-11','2027-05-11','activa',3),(14,14,'Trimestral','2026-02-17','2026-05-18','vencida',2),(15,15,'Trimestral','2026-05-04','2026-08-02','activa',2),(16,16,'Anual','2026-05-06','2027-05-06','activa',3),(17,17,'Anual','2025-10-03','2026-10-03','activa',3),(18,18,'Mensual','2026-07-07','2026-08-06','activa',1),(19,19,'Mensual','2026-04-29','2026-05-29','vencida',1),(20,20,'Mensual','2026-07-12','2026-08-11','activa',1),(21,21,'Trimestral','2026-07-08','2026-10-06','activa',2),(22,22,'Trimestral','2026-06-10','2026-09-08','activa',2),(23,23,'Mensual','2026-06-30','2026-07-30','activa',1),(24,24,'Mensual','2026-07-09','2026-08-08','activa',1),(25,25,'Trimestral','2026-04-30','2026-07-29','activa',2),(26,26,'Mensual','2026-07-12','2026-08-11','activa',1),(27,27,'Anual','2026-06-23','2027-06-23','activa',3),(28,28,'Trimestral','2026-04-17','2026-07-16','activa',2),(29,29,'Trimestral','2026-05-22','2026-08-20','activa',2),(30,30,'Anual','2026-03-14','2027-03-14','activa',3),(31,31,'Anual','2026-07-01','2027-07-01','activa',3),(32,32,'Anual','2026-04-16','2027-04-16','activa',3),(33,33,'Mensual','2026-07-10','2026-08-09','activa',1),(34,34,'Anual','2026-03-22','2027-03-22','activa',3),(35,35,'Mensual','2026-07-06','2026-08-05','activa',1),(36,36,'Trimestral','2026-04-16','2026-07-15','vencida',2),(37,37,'Anual','2026-02-21','2027-02-21','activa',3),(38,38,'Anual','2026-02-15','2027-02-15','activa',3),(39,39,'Anual','2026-05-15','2027-05-15','activa',3),(40,40,'Mensual','2026-07-05','2026-08-04','activa',1),(41,41,'Mensual','2026-05-17','2026-06-16','vencida',1),(42,42,'Mensual','2026-06-27','2026-07-27','activa',1),(46,46,'Trimestral','2026-06-28','2026-09-26','activa',2),(47,47,'Anual','2026-06-16','2027-06-16','activa',3),(48,48,'Mensual','2026-07-04','2026-08-03','activa',1),(49,49,'Mensual','2026-07-01','2026-07-31','activa',1),(50,50,'Trimestral','2026-06-05','2026-09-03','activa',2),(53,54,'Mensual','2026-07-14','2026-08-13','activa',1),(54,55,'Mensual','2026-07-14','2026-08-13','activa',1),(55,57,'Trimestral','2026-07-15','2026-10-13','activa',2),(56,59,'Mensual','2026-07-16','2026-08-15','activa',1),(57,54,'Mensual','2026-07-16','2026-08-15','activa',1),(58,60,'Mensual','2026-07-16','2026-08-15','activa',1);
/*!40000 ALTER TABLE `membresia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pago`
--

DROP TABLE IF EXISTS `pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pago` (
  `id` int NOT NULL AUTO_INCREMENT,
  `socio_id` int NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `metodo_pago` enum('efectivo','tarjeta','yape','plin') DEFAULT 'efectivo',
  `fecha_pago` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `descripcion` varchar(255) DEFAULT NULL,
  `membresia_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `socio_id` (`socio_id`),
  KEY `idx_pago_fecha` (`fecha_pago`),
  KEY `fk_pago_membresia` (`membresia_id`),
  CONSTRAINT `fk_pago_membresia` FOREIGN KEY (`membresia_id`) REFERENCES `membresia` (`id`),
  CONSTRAINT `pago_ibfk_1` FOREIGN KEY (`socio_id`) REFERENCES `socio` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pago`
--

LOCK TABLES `pago` WRITE;
/*!40000 ALTER TABLE `pago` DISABLE KEYS */;
INSERT INTO `pago` VALUES (3,3,950.00,'efectivo','2026-06-02 15:00:00','Pago de membresia',3),(5,5,100.00,'efectivo','2026-06-25 15:00:00','Pago de membresia',5),(6,6,270.00,'yape','2026-05-21 15:00:00','Pago de membresia',6),(7,7,950.00,'yape','2026-03-31 15:00:00','Pago de membresia',7),(8,8,100.00,'yape','2026-07-01 15:00:00','Pago de membresia',8),(9,9,100.00,'plin','2026-07-11 15:00:00','Pago de membresia',9),(10,10,270.00,'efectivo','2026-03-04 15:00:00','Pago de membresia',10),(11,11,100.00,'tarjeta','2026-06-27 15:00:00','Pago de membresia',11),(12,12,950.00,'yape','2026-06-08 15:00:00','Pago de membresia',12),(13,13,950.00,'yape','2026-05-11 15:00:00','Pago de membresia',13),(14,14,270.00,'efectivo','2026-02-17 15:00:00','Pago de membresia',14),(15,15,270.00,'efectivo','2026-05-04 15:00:00','Pago de membresia',15),(16,16,950.00,'efectivo','2026-05-06 15:00:00','Pago de membresia',16),(17,17,950.00,'yape','2025-10-03 15:00:00','Pago de membresia',17),(18,18,100.00,'yape','2026-07-07 15:00:00','Pago de membresia',18),(19,19,100.00,'plin','2026-04-29 15:00:00','Pago de membresia',19),(20,20,100.00,'plin','2026-07-12 15:00:00','Pago de membresia',20),(21,21,270.00,'yape','2026-07-08 15:00:00','Pago de membresia',21),(22,22,270.00,'plin','2026-06-10 15:00:00','Pago de membresia',22),(23,23,100.00,'efectivo','2026-06-30 15:00:00','Pago de membresia',23),(24,24,100.00,'efectivo','2026-07-09 15:00:00','Pago de membresia',24),(25,25,270.00,'tarjeta','2026-04-30 15:00:00','Pago de membresia',25),(26,26,100.00,'yape','2026-07-12 15:00:00','Pago de membresia',26),(27,27,950.00,'tarjeta','2026-06-23 15:00:00','Pago de membresia',27),(28,28,270.00,'yape','2026-04-17 15:00:00','Pago de membresia',28),(29,29,270.00,'tarjeta','2026-05-22 15:00:00','Pago de membresia',29),(30,30,950.00,'tarjeta','2026-03-14 15:00:00','Pago de membresia',30),(31,31,950.00,'yape','2026-07-01 15:00:00','Pago de membresia',31),(32,32,950.00,'tarjeta','2026-04-16 15:00:00','Pago de membresia',32),(33,33,100.00,'efectivo','2026-07-10 15:00:00','Pago de membresia',33),(34,34,950.00,'plin','2026-03-22 15:00:00','Pago de membresia',34),(35,35,100.00,'efectivo','2026-07-06 15:00:00','Pago de membresia',35),(36,36,270.00,'yape','2026-04-16 15:00:00','Pago de membresia',36),(37,37,950.00,'plin','2026-02-21 15:00:00','Pago de membresia',37),(38,38,950.00,'yape','2026-02-15 15:00:00','Pago de membresia',38),(39,39,950.00,'tarjeta','2026-05-15 15:00:00','Pago de membresia',39),(40,40,100.00,'efectivo','2026-07-05 15:00:00','Pago de membresia',40),(41,41,100.00,'yape','2026-05-17 15:00:00','Pago de membresia',41),(42,42,100.00,'plin','2026-06-27 15:00:00','Pago de membresia',42),(43,43,950.00,'efectivo','2025-07-14 15:00:00','Pago de membresia',NULL),(44,44,270.00,'tarjeta','2026-04-09 15:00:00','Pago de membresia',NULL),(45,45,100.00,'yape','2026-06-14 15:00:00','Pago de membresia',NULL),(46,46,270.00,'yape','2026-06-28 15:00:00','Pago de membresia',46),(47,47,950.00,'yape','2026-06-16 15:00:00','Pago de membresia',47),(48,48,100.00,'tarjeta','2026-07-04 15:00:00','Pago de membresia',48),(49,49,100.00,'tarjeta','2026-07-01 15:00:00','Pago de membresia',49),(50,50,270.00,'efectivo','2026-06-05 15:00:00','Pago de membresia',50),(51,51,950.00,'yape','2026-07-14 04:45:05','Registro rapido',NULL),(52,54,80.00,'efectivo','2026-07-14 21:55:58','JUnit Pago',53),(53,55,100.00,'efectivo','2026-07-14 22:07:03','Registro rapido',54),(54,57,270.00,'tarjeta','2026-07-16 02:52:13','Registro rapido',55),(55,59,100.00,'tarjeta','2026-07-17 01:16:17','Registro rapido',56),(56,55,100.00,'efectivo','2026-07-17 01:17:39','Membresía Mensual',54),(57,54,80.00,'efectivo','2026-07-17 02:24:41','JUnit Pago',53);
/*!40000 ALTER TABLE `pago` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `socio`
--

DROP TABLE IF EXISTS `socio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `socio` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `dni` varchar(15) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `fecha_registro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` enum('activo','inactivo') DEFAULT 'activo',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dni` (`dni`),
  KEY `idx_socio_dni` (`dni`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `socio`
--

LOCK TABLES `socio` WRITE;
/*!40000 ALTER TABLE `socio` DISABLE KEYS */;
INSERT INTO `socio` VALUES (3,'Ana Garcia Gonzalez','54672546','941227216','ana.garcia3@gmail.com','2026-06-01 14:00:00','activo'),(5,'Sergio Gomez Vega','40436124','931429110','sergio.gomez5@gmail.com','2026-06-24 14:00:00','activo'),(6,'Milagros Gomez Lopez','54449461','955176955','milagros.gomez6@gmail.com','2026-05-16 14:00:00','activo'),(7,'Raul Fernandez Ramirez','63082477','991030736','raul.fernandez7@gmail.com','2026-03-26 14:00:00','activo'),(8,'Luis Cruz Vargas','75985658','926753883','luis.cruz8@gmail.com','2026-06-30 14:00:00','activo'),(9,'Daniela Gonzalez Chavez','59674861','994374605','daniela.gonzalez9@gmail.com','2026-07-11 14:00:00','activo'),(10,'Andrea Mendoza Sanchez','44667767','916150444','andrea.mendoza10@gmail.com','2026-03-04 14:00:00','activo'),(11,'Jazmin Diaz Gonzalez','55622331','923556182','jazmin.diaz11@gmail.com','2026-06-25 14:00:00','activo'),(12,'Pedro Huaman Espinoza','64483473','931831063','pedro.huaman12@gmail.com','2026-06-07 14:00:00','activo'),(13,'Fernando Mamani Gomez','44791741','991756179','fernando.mamani13@gmail.com','2026-05-07 14:00:00','activo'),(14,'Grecia Cruz Perez','50965755','972043515','grecia.cruz14@gmail.com','2026-02-12 14:00:00','activo'),(15,'Alexander Reyes Chavez','54738124','953524491','alexander.reyes15@gmail.com','2026-05-02 14:00:00','activo'),(16,'Jazmin Rodriguez Perez','42154210','952339391','jazmin.rodriguez16@gmail.com','2026-05-04 14:00:00','activo'),(17,'Jose Sanchez Aguilar','78062808','952235350','jose.sanchez17@gmail.com','2025-10-02 14:00:00','activo'),(18,'Andres Flores Cardenas','70793026','929175900','andres.flores18@gmail.com','2026-07-02 14:00:00','activo'),(19,'Ricardo Cruz Chavez','76170153','945264581','ricardo.cruz19@gmail.com','2026-04-27 14:00:00','activo'),(20,'Valeria Cardenas Mendoza','66803314','958586340','valeria.cardenas20@gmail.com','2026-07-12 14:00:00','activo'),(21,'Miguel Rojas Castillo','46100827','916323852','miguel.rojas21@gmail.com','2026-07-08 14:00:00','activo'),(22,'Lucia Huaman Martinez','68330675','990048665','lucia.huaman22@gmail.com','2026-06-09 14:00:00','activo'),(23,'Raul Quispe Vargas','75508262','943744231','raul.quispe23@gmail.com','2026-06-26 14:00:00','activo'),(24,'Maria Mamani Cruz','47687437','982070937','maria.mamani24@gmail.com','2026-07-05 14:00:00','activo'),(25,'Jazmin Huaman Torres','47486139','949392920','jazmin.huaman25@gmail.com','2026-04-26 14:00:00','activo'),(26,'Pedro Garcia Cruz','57675739','977187530','pedro.garcia26@gmail.com','2026-07-10 14:00:00','activo'),(27,'Camila Aguilar Fernandez','60028290','995758349','camila.aguilar27@gmail.com','2026-06-21 14:00:00','activo'),(28,'Gabriela Lopez Ramirez','50841372','982394227','gabriela.lopez28@gmail.com','2026-04-17 14:00:00','activo'),(29,'Camila Aguilar Garcia','61753744','975579548','camila.aguilar29@gmail.com','2026-05-18 14:00:00','activo'),(30,'Cesar Cardenas Espinoza','60636923','942138745','cesar.cardenas30@gmail.com','2026-03-13 14:00:00','activo'),(31,'Sergio Gonzalez Gonzalez','72614267','919289546','sergio.gonzalez31@gmail.com','2026-06-30 14:00:00','activo'),(32,'Grecia Salazar Lopez','48616205','998550256','grecia.salazar32@gmail.com','2026-04-11 14:00:00','activo'),(33,'Gonzalo Martinez Gomez','75411588','991415657','gonzalo.martinez33@gmail.com','2026-07-07 14:00:00','activo'),(34,'Fernando Aguilar Chavez','53499019','951837852','fernando.aguilar34@gmail.com','2026-03-21 14:00:00','activo'),(35,'Bryan Huaman Ramirez','69400398','979467853','bryan.huaman35@gmail.com','2026-07-05 14:00:00','activo'),(36,'Ricardo Perez Gonzalez','62688538','912823170','ricardo.perez36@gmail.com','2026-04-14 14:00:00','activo'),(37,'Diana Mendoza Perez','40482569','919528530','diana.mendoza37@gmail.com','2026-02-19 14:00:00','activo'),(38,'Ana Perez Gonzalez','42108087','954349361','ana.perez38@gmail.com','2026-02-11 14:00:00','activo'),(39,'Ricardo Gomez Mamani','72575615','938754377','ricardo.gomez39@gmail.com','2026-05-13 14:00:00','activo'),(40,'Melissa Aguilar Cardenas','78322053','987337818','melissa.aguilar40@gmail.com','2026-07-05 14:00:00','activo'),(41,'Andres Vega Rivera','52778193','922660194','andres.vega41@gmail.com','2026-05-12 14:00:00','activo'),(42,'Victor Ramirez Rivera','67588606','972682989','victor.ramirez42@gmail.com','2026-06-27 14:00:00','activo'),(43,'Ana Mamani Huaman','46604711','918135295','ana.mamani43@gmail.com','2025-07-12 14:00:00','activo'),(44,'Manuel Vega Paredes','47332920','943374088','manuel.vega44@gmail.com','2026-04-05 14:00:00','activo'),(45,'Gonzalo Vargas Lopez','68311997','934627347','gonzalo.vargas45@gmail.com','2026-06-09 14:00:00','activo'),(46,'Ricardo Paredes Aguilar','45058994','969476001','ricardo.paredes46@gmail.com','2026-06-23 14:00:00','activo'),(47,'Grecia Fernandez Rodriguez','76278242','911980765','grecia.fernandez47@gmail.com','2026-06-13 14:00:00','activo'),(48,'Jazmin Paredes Perez','51160949','964547971','jazmin.paredes48@gmail.com','2026-06-30 14:00:00','activo'),(49,'Fernando Paredes Flores','43934955','932097220','fernando.paredes49@gmail.com','2026-06-26 14:00:00','activo'),(50,'Raul Gomez Aguilar','70535094','948285503','raul.gomez50@gmail.com','2026-06-02 14:00:00','activo'),(51,'Ray Arturo Vargas Ore','76534814','965489321','tecnivoro20@gmail.com','2026-07-14 04:45:05','activo'),(54,'JUnit Actualizado','99999998','999111222','junit@test.com','2026-07-14 21:51:47','inactivo'),(55,'JUnit Registro Rapido','66822826','999888777','registro@test.com','2026-07-14 22:07:03','activo'),(57,'Mariafernanda Calderon','74269853','970256987','lamundomafer@gmail.com','2026-07-16 02:52:13','activo'),(58,'Rafael Vargas','02356985','965826369','masterpro@gmail.com','2026-07-16 02:53:08','activo'),(59,'Elizabeth Ore','10789920','963258741','eliparafa@hotmail.com','2026-07-17 01:16:17','activo'),(60,'JUnit Registro Rapido','55114760','999888777','registro@test.com','2026-07-17 02:25:15','activo');
/*!40000 ALTER TABLE `socio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_membresia`
--

DROP TABLE IF EXISTS `tipo_membresia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_membresia` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `duracion_dias` int NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `estado` enum('activo','inactivo') DEFAULT 'activo',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_membresia`
--

LOCK TABLES `tipo_membresia` WRITE;
/*!40000 ALTER TABLE `tipo_membresia` DISABLE KEYS */;
INSERT INTO `tipo_membresia` VALUES (1,'Mensual',30,100.00,'activo'),(2,'Trimestral',90,270.00,'activo'),(3,'Anual',365,950.00,'activo');
/*!40000 ALTER TABLE `tipo_membresia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` enum('admin','recepcionista') NOT NULL,
  `estado` enum('activo','inactivo') DEFAULT 'activo',
  `nombre_completo` varchar(100) DEFAULT NULL,
  `ultimo_login` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'admin','admin123','admin','activo','Administrador General',NULL),(2,'recepcion','recep123','recepcionista','activo','Recepcion Principal',NULL);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'gymcontrol'
--

--
-- Dumping routines for database 'gymcontrol'
--
/*!50003 DROP PROCEDURE IF EXISTS `generar_socios` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `generar_socios`()
BEGIN

    DECLARE i INT DEFAULT 1;

    WHILE i <= 200 DO

        INSERT INTO socio
        (
            nombre,
            dni,
            telefono,
            email,
            estado
        )
        VALUES
        (
            CONCAT('Socio ', i),
            CONCAT('7', LPAD(i,7,'0')),
            CONCAT('9', LPAD(i,8,'0')),
            CONCAT('socio', i, '@gmail.com'),
            'activo'
        );

        SET i = i + 1;

    END WHILE;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-16 23:34:25
