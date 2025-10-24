CREATE DATABASE  IF NOT EXISTS `postogram` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `postogram`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: postogram
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `body` text,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `post_id` (`post_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,1,4,'ОЧІКУЙ МЕНЕ, Я СКОРО ПРИЙДУ!!','2025-10-17 10:27:28','2025-10-17 10:27:28'),(2,2,2,'Ще краще, коли в тебе є гроші в цей день','2025-10-17 10:27:28','2025-10-17 10:27:28'),(3,2,3,'шкода що немає дня 1+1=4..','2025-10-17 10:27:28','2025-10-17 10:27:28'),(4,3,4,'Недавно були з другом в яремче, там біля базару є червоненький дім з доброю жіночкою, попробуйте до неї','2025-10-17 10:27:28','2025-10-17 10:27:28'),(5,4,4,'Помянем...','2025-10-17 10:27:28','2025-10-17 10:27:28'),(6,4,1,'Уявляю його лице коли він дізнається xD ','2025-10-17 10:27:28','2025-10-17 10:27:28'),(7,5,4,'Окак','2025-10-17 10:27:28','2025-10-17 10:27:28'),(8,6,1,'Хто б сумнівався, ахахахаах','2025-10-17 10:27:28','2025-10-17 10:27:28'),(9,6,4,'Очікувано','2025-10-17 10:27:28','2025-10-17 10:27:28'),(10,7,2,'АЛЬОООО, І БЕЗ МЕНЕ??!','2025-10-17 10:27:28','2025-10-17 10:27:28');
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friendship`
--

DROP TABLE IF EXISTS `friendship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friendship` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `follower_id` bigint DEFAULT NULL,
  `followee_id` bigint DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `status` enum('pending','accepted','rejected') NOT NULL DEFAULT 'accepted',
  PRIMARY KEY (`id`),
  KEY `follower_id` (`follower_id`),
  KEY `followee_id` (`followee_id`),
  CONSTRAINT `friendship_ibfk_1` FOREIGN KEY (`follower_id`) REFERENCES `user` (`id`),
  CONSTRAINT `friendship_ibfk_2` FOREIGN KEY (`followee_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friendship`
--

LOCK TABLES `friendship` WRITE;
/*!40000 ALTER TABLE `friendship` DISABLE KEYS */;
INSERT INTO `friendship` VALUES (1,1,2,'2025-10-16 00:21:16','pending'),(2,2,3,'2025-10-16 00:21:16','accepted'),(3,3,2,'2025-10-16 00:21:16','accepted'),(4,4,2,'2025-10-16 00:21:16','rejected'),(5,4,3,'2025-10-16 00:21:16','rejected'),(6,1,4,'2025-10-16 00:21:16','accepted'),(7,4,1,'2025-10-16 00:21:16','accepted'),(8,1,3,'2025-10-16 00:22:19','pending');
/*!40000 ALTER TABLE `friendship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `like`
--

DROP TABLE IF EXISTS `like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `like` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `post_id` (`post_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `like_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `like_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `like`
--

LOCK TABLES `like` WRITE;
/*!40000 ALTER TABLE `like` DISABLE KEYS */;
INSERT INTO `like` VALUES (1,1,2,'2025-10-17 01:13:44'),(2,1,3,'2025-10-17 01:13:44'),(3,1,4,'2025-10-17 01:13:44'),(4,2,2,'2025-10-17 01:13:44'),(5,3,2,'2025-10-17 01:13:44'),(6,3,4,'2025-10-17 01:13:44'),(7,3,3,'2025-10-17 01:13:44'),(8,4,1,'2025-10-17 01:13:44'),(9,4,4,'2025-10-17 01:13:44'),(10,5,1,'2025-10-17 01:13:44'),(11,5,4,'2025-10-17 01:13:44'),(12,6,1,'2025-10-17 01:13:44'),(13,6,2,'2025-10-17 01:13:44'),(14,6,4,'2025-10-17 01:13:44'),(15,8,1,'2025-10-17 01:13:44'),(16,8,3,'2025-10-17 01:13:44'),(17,9,2,'2025-10-17 01:13:44'),(18,10,1,'2025-10-17 01:13:44'),(19,11,1,'2025-10-17 01:13:44'),(20,11,2,'2025-10-17 01:13:44'),(21,11,3,'2025-10-17 01:13:44');
/*!40000 ALTER TABLE `like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mediaasset`
--

DROP TABLE IF EXISTS `mediaasset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mediaasset` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint DEFAULT NULL,
  `kind` set('image','video') NOT NULL,
  `url` varchar(255) NOT NULL,
  `duration_sec` int DEFAULT NULL,
  `position` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `post_id` (`post_id`),
  CONSTRAINT `mediaasset_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mediaasset`
--

LOCK TABLES `mediaasset` WRITE;
/*!40000 ALTER TABLE `mediaasset` DISABLE KEYS */;
INSERT INTO `mediaasset` VALUES (1,1,'image','URL',NULL,1),(2,2,'image','URL',NULL,2),(3,3,'video','URL',10,3),(4,4,'image','URL',NULL,1),(5,5,'image','URL',NULL,2),(6,6,'video','URL',17,1),(7,7,'image','URL',NULL,2),(8,8,'image','URL',NULL,1),(9,9,'image','URL',NULL,2),(10,10,'video','URL',6,3),(11,11,'image','URL',NULL,4);
/*!40000 ALTER TABLE `mediaasset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `caption` text,
  `created_at` datetime NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `is_archived` tinyint(1) NOT NULL DEFAULT '0',
  `updated_at` datetime NOT NULL,
  `status` enum('active','inactive','banned') NOT NULL DEFAULT 'active',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `post_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,1,'Є пляшка горілки, але немає хорошої людини)','2025-10-17 01:02:36','Львів',0,'2025-10-17 01:02:36','active'),(2,1,'Люблю день 1+1=3, а ви?)','2025-10-17 01:02:36','Львів',0,'2025-10-17 01:02:36','active'),(3,1,'Ми в яремче без хати, але з великою кількістю алкоголю!','2025-10-17 01:02:36','Яремче',0,'2025-10-17 01:02:36','active'),(4,2,'Боба синій!! Ми не розумієм, він топився чи напився','2025-10-17 01:02:36','Одеса',0,'2025-10-17 01:02:36','active'),(5,2,'Боба воскрес і накинувся на бармена з пляжу, нас вигнали...','2025-10-17 01:02:36','Одеса',0,'2025-10-17 01:02:36','active'),(6,3,'Повернувся з відпочику, і в душі не чаю що там відбувалось...','2025-10-17 01:02:36','Київ',0,'2025-10-17 01:02:36','active'),(7,3,'Обожнюю вечори коли є тільки я, балкон, і пляшка пива','2025-10-17 01:02:36','Київ',0,'2025-10-17 01:02:36','active'),(8,4,'День народження - найкращий день після дня пива','2025-10-17 01:02:36','Львів',0,'2025-10-17 01:02:36','active'),(9,4,'Зустрів спайдермена','2025-10-17 01:02:36','Львів',0,'2025-10-17 01:02:36','active'),(10,4,'Я, друг, і велосипеди в горах','2025-10-17 01:02:36','Яремче',0,'2025-10-17 01:02:36','active'),(11,4,'Антон, розумний, харизматичний, а саме головне - маминий синочок. 19 рочків, хоче за кордон','2025-10-17 01:02:36','Львів',0,'2025-10-17 01:02:36','active');
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profile`
--

DROP TABLE IF EXISTS `profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profile` (
  `user_id` bigint NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `bio` varchar(255) DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `profile_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profile`
--

LOCK TABLES `profile` WRITE;
/*!40000 ALTER TABLE `profile` DISABLE KEYS */;
INSERT INTO `profile` VALUES (1,'Yurii Seredniy','Люблю котиків і пиво)','D:TrashFOR_BDKotik.jpg'),(2,'Biba bab','Удача це убивця що ламає нам суглоби','D:TrashFOR_BDTikalo.jpg'),(3,'Boba bob','скиньте гроші бідному студенту 4441114463204964','D:TrashFOR_BDJABA.jpg'),(4,'Anton','Дііімочка) іді на**й','D:TrashFOR_BDDima.jpg');
/*!40000 ALTER TABLE `profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stories`
--

DROP TABLE IF EXISTS `stories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `media_url` varchar(255) NOT NULL,
  `kind` enum('image','video') NOT NULL,
  `caption` varchar(255) DEFAULT NULL,
  `created_at` date NOT NULL,
  `duration_sec` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `stories_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stories`
--

LOCK TABLES `stories` WRITE;
/*!40000 ALTER TABLE `stories` DISABLE KEYS */;
INSERT INTO `stories` VALUES (1,1,'URL','image',NULL,'2025-10-17',NULL),(2,2,'URL','image',NULL,'2025-10-17',NULL),(3,3,'URL','video',NULL,'2025-10-17',7),(4,3,'URL','image','Йому погано..','2025-10-17',NULL),(5,4,'URL','image','Обожнюю ці види','2025-10-17',NULL);
/*!40000 ALTER TABLE `stories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(40) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `status` enum('active','inactive','banned') NOT NULL DEFAULT 'active',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'yuriilviv@gmail.com','Yurii','cf03d2959e698be2e4868faba5f51a193c88c5c40c85142760fd6e8856784181','2025-10-13 12:47:07','active'),(2,'Dvadovb@gmail.com','Biba','af316ecb91a8ee7ae99210702b2d4758f30cdde3bf61e3d8e787d74681f90a6e','2025-10-13 12:47:07','active'),(3,'oyoba@gmail.com','Boba','e7bf382f6e5915b3f88619b866223ebf1d51c4c5321cccde2e9ff700a3259086','2025-10-13 12:47:07','inactive'),(4,'tretiy@gmail.com','Anton','42caa4abb7b60f8f914e5bfb8e6511d7d9bd9817de719b74251755d97fe97bf1','2025-10-13 12:47:07','active');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-17 12:15:34
