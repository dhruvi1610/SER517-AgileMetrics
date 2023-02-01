CREATE DATABASE  IF NOT EXISTS `Cassess` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `Cassess`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: Cassess
-- ------------------------------------------------------
-- Server version	5.7.19

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
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admins` (
  `id` binary(16) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `course` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8xahrcf9t0jbxvvvlxfmyecy8` (`course`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `authority`
--

DROP TABLE IF EXISTS `authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authority` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authority`
--

LOCK TABLES `authority` WRITE;
/*!40000 ALTER TABLE `authority` DISABLE KEYS */;
INSERT INTO `authority` VALUES (1,'admin'),(2,'student'),(3,'super_user'),(4,'rest');
/*!40000 ALTER TABLE `authority` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `channelobject_members`
--

DROP TABLE IF EXISTS `channelobject_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `channelobject_members` (
  `ChannelObject_id` varchar(255) NOT NULL,
  `members` varchar(255) DEFAULT NULL,
  `slack_channel_member_sequence` int(11) NOT NULL,
  PRIMARY KEY (`ChannelObject_id`,`slack_channel_member_sequence`),
  CONSTRAINT `FKn4noo14wxpw808c8potiae14n` FOREIGN KEY (`ChannelObject_id`) REFERENCES `slack_channel` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `channelobject_previous_names`
--

DROP TABLE IF EXISTS `channelobject_previous_names`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `channelobject_previous_names` (
  `ChannelObject_id` varchar(255) NOT NULL,
  `previous_names` varchar(255) DEFAULT NULL,
  `slack_channel_pn_sequence` int(11) NOT NULL,
  PRIMARY KEY (`ChannelObject_id`,`slack_channel_pn_sequence`),
  CONSTRAINT `FKn0l3nf7rqlbw0rwmlvofr61fg` FOREIGN KEY (`ChannelObject_id`) REFERENCES `slack_channel` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `channels`
--

DROP TABLE IF EXISTS `channels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `channels` (
  `id` varchar(255) NOT NULL,
  `course` varchar(255) DEFAULT NULL,
  `team_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6533r8btqr63f9q9l4sqsyaal` (`team_name`),
  CONSTRAINT `FK6533r8btqr63f9q9l4sqsyaal` FOREIGN KEY (`team_name`) REFERENCES `teams` (`team_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `commit_data`
--

DROP TABLE IF EXISTS `commit_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commit_data` (
  `date` date NOT NULL,
  `username` varchar(255) NOT NULL,
  `commits` int(11) DEFAULT NULL,
  `lines_of_code_added` int(11) DEFAULT NULL,
  `lines_of_code_deleted` int(11) DEFAULT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `github_owner` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `course` varchar(45) DEFAULT NULL,
  `team` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`date`,`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `courses` (
  `course` varchar(255) NOT NULL,
  `end_date` date DEFAULT NULL,
  `github_owner` varchar(255) DEFAULT NULL,
  `github_token` varchar(255) DEFAULT NULL,
  `slack_token` varchar(255) DEFAULT NULL,
  `taiga_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`course`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `github_weight`
--

DROP TABLE IF EXISTS `github_weight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `github_weight` (
  `date` date NOT NULL,
  `username` varchar(45) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `course` varchar(45) DEFAULT NULL,
  `team` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`username`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `groupobject_members`
--

DROP TABLE IF EXISTS `groupobject_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupobject_members` (
  `GroupObject_id` varchar(255) NOT NULL,
  `members` varchar(255) DEFAULT NULL,
  `slack_group_members_sequence` int(11) NOT NULL,
  PRIMARY KEY (`GroupObject_id`,`slack_group_members_sequence`),
  CONSTRAINT `FKsti6v5q7v6u4bir86jfgcr5oe` FOREIGN KEY (`GroupObject_id`) REFERENCES `slack_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `memberdata`
--

DROP TABLE IF EXISTS `memberdata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `memberdata` (
  `id` int(11) DEFAULT NULL,
  `fullName` varchar(255) DEFAULT NULL,
  `project` varchar(255) DEFAULT NULL,
  `project_slug` varchar(255) DEFAULT NULL,
  `roleName` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `team` varchar(45) DEFAULT NULL,
  `course` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `slug` varchar(255) NOT NULL,
  PRIMARY KEY (`slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `slack_auth`
--

DROP TABLE IF EXISTS `slack_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `slack_auth` (
  `id` int(11) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `slack_channel`
--

DROP TABLE IF EXISTS `slack_channel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `slack_channel` (
  `id` varchar(255) NOT NULL,
  `created` bigint(20) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `is_archived` bit(1) NOT NULL,
  `is_channel` bit(1) NOT NULL,
  `is_general` bit(1) NOT NULL,
  `is_member` bit(1) NOT NULL,
  `last_read` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `num_members` bigint(20) NOT NULL,
  `purpose_creator` varchar(255) DEFAULT NULL,
  `purpose_last_set` bigint(20) DEFAULT NULL,
  `purpose_value` varchar(255) DEFAULT NULL,
  `topic_creator` varchar(255) DEFAULT NULL,
  `topic_last_set` bigint(20) DEFAULT NULL,
  `topic_value` varchar(255) DEFAULT NULL,
  `unread_count` int(11) NOT NULL,
  `unread_count_display` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `slack_group`
--

DROP TABLE IF EXISTS `slack_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `slack_group` (
  `id` varchar(255) NOT NULL,
  `created` bigint(20) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `is_archived` bit(1) NOT NULL,
  `is_group` varchar(255) DEFAULT NULL,
  `is_mpim` bit(1) NOT NULL,
  `last_read` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `purpose_creator` varchar(255) DEFAULT NULL,
  `purpose_last_set` bigint(20) DEFAULT NULL,
  `purpose_value` varchar(255) DEFAULT NULL,
  `topic_creator` varchar(255) DEFAULT NULL,
  `topic_last_set` bigint(20) DEFAULT NULL,
  `topic_value` varchar(255) DEFAULT NULL,
  `unread_count` bigint(20) NOT NULL,
  `unread_count_display` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `slack_messages`
--

DROP TABLE IF EXISTS `slack_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `slack_messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ts` double DEFAULT NULL,
  `user` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `slack_messagetotals`
--

DROP TABLE IF EXISTS `slack_messagetotals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `slack_messagetotals` (
  `retrievalDate` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `channel_id` varchar(45) NOT NULL,
  `fullName` varchar(45) DEFAULT NULL,
  `course` varchar(45) DEFAULT NULL,
  `team` varchar(45) DEFAULT NULL,
  `messageCount` int(11) DEFAULT '0',
  PRIMARY KEY (`retrievalDate`,`email`,`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `slack_team`
--

DROP TABLE IF EXISTS `slack_team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `slack_team` (
  `id` varchar(255) NOT NULL,
  `domain` varchar(255) DEFAULT NULL,
  `email_domain` varchar(255) DEFAULT NULL,
  `image_102` varchar(255) DEFAULT NULL,
  `image_132` varchar(255) DEFAULT NULL,
  `image_230` varchar(255) DEFAULT NULL,
  `image_34` varchar(255) DEFAULT NULL,
  `image_44` varchar(255) DEFAULT NULL,
  `image_68` varchar(255) DEFAULT NULL,
  `image_88` varchar(255) DEFAULT NULL,
  `image_default` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `slack_user`
--

DROP TABLE IF EXISTS `slack_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `slack_user` (
  `id` varchar(255) NOT NULL,
  `deleted` bit(1) NOT NULL,
  `is_admin` bit(1) NOT NULL,
  `is_owner` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `profile_real_name` varchar(255) DEFAULT NULL,
  `real_name` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `team_id` varchar(255) DEFAULT NULL,
  `course` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `students` (
  `id` binary(16) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `course` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `team_name` varchar(255) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlb5s7aexy0a5460iamau0qlh0` (`team_name`),
  CONSTRAINT `FKlb5s7aexy0a5460iamau0qlh0` FOREIGN KEY (`team_name`) REFERENCES `teams` (`team_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `taskdata`
--

DROP TABLE IF EXISTS `taskdata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `taskdata` (
  `id` int(11) NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `member_id` bigint(20) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `project` bigint(20) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tasktotals`
--

DROP TABLE IF EXISTS `tasktotals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tasktotals` (
  `retrievalDate` varchar(255) NOT NULL,
  `email` varchar(45) NOT NULL,
  `fullName` varchar(255) DEFAULT '0',
  `project` varchar(255) DEFAULT '0',
  `course` varchar(45) DEFAULT '0',
  `team` varchar(45) DEFAULT '0',
  `tasksClosed` int(11) DEFAULT '0',
  `tasksInProgress` int(11) DEFAULT '0',
  `tasksNew` int(11) DEFAULT '0',
  `tasksOpen` int(11) DEFAULT '0',
  `tasksReadyForTest` int(11) DEFAULT '0',
  PRIMARY KEY (`retrievalDate`,`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teams`
--

DROP TABLE IF EXISTS `teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teams` (
  `team_name` varchar(255) NOT NULL,
  `course` varchar(255) DEFAULT NULL,
  `github_repo_id` varchar(255) DEFAULT NULL,
  `slack_team_id` varchar(255) DEFAULT NULL,
  `taiga_project_slug` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`team_name`),
  KEY `FKahkmfgmwesgwhj2iu5yhhhmie` (`course`),
  CONSTRAINT `FKahkmfgmwesgwhj2iu5yhhhmie` FOREIGN KEY (`course`) REFERENCES `courses` (`course`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `token` (
  `series` varchar(255) NOT NULL,
  `date` datetime DEFAULT NULL,
  `ip_address` varchar(255) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `user_login` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `e_mail` varchar(255) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `family_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES 
(1,'tjjohn1asu@gmail.com','','Johnson','Thomas','en','tjjohn1asu@gmail.com','$2a$11$1taNqaK5BL5ITfnDZtq8T.2uPMBhC7fGtCjYRYZJXmty4i.t15rk.');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_authority`
--

DROP TABLE IF EXISTS `users_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_authority` (
  `id_user` bigint(20) NOT NULL,
  `id_authority` bigint(20) NOT NULL,
  PRIMARY KEY (`id_user`,`id_authority`),
  KEY `FKlp67kinuubrwwt0v1ss11iwci` (`id_authority`),
  CONSTRAINT `FKa3oaqq69l8o79g23ipfd1eomm` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`),
  CONSTRAINT `FKlp67kinuubrwwt0v1ss11iwci` FOREIGN KEY (`id_authority`) REFERENCES `authority` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_authority`
--

LOCK TABLES `users_authority` WRITE;
/*!40000 ALTER TABLE `users_authority` DISABLE KEYS */;
INSERT INTO `users_authority` VALUES (1,3);
/*!40000 ALTER TABLE `users_authority` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-23 19:44:54
