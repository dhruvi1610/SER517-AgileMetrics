-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 09, 2023 at 05:00 AM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.0.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cassess`
--

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `id` binary(16) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `course` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `authority`
--

CREATE TABLE `authority` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `authority`
--

INSERT INTO `authority` (`id`, `name`) VALUES
(1, 'admin'),
(2, 'student'),
(3, 'super_user'),
(4, 'rest');

-- --------------------------------------------------------

--
-- Table structure for table `channelobject_members`
--

CREATE TABLE `channelobject_members` (
  `ChannelObject_id` varchar(255) NOT NULL,
  `members` varchar(255) DEFAULT NULL,
  `slack_channel_member_sequence` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `channelobject_previous_names`
--

CREATE TABLE `channelobject_previous_names` (
  `ChannelObject_id` varchar(255) NOT NULL,
  `previous_names` varchar(255) DEFAULT NULL,
  `slack_channel_pn_sequence` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `channels`
--

CREATE TABLE `channels` (
  `id` varchar(255) NOT NULL,
  `course` varchar(255) DEFAULT NULL,
  `team_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `commit_data`
--

CREATE TABLE `commit_data` (
  `date` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `commits` int(11) DEFAULT NULL,
  `lines_of_code_added` int(11) DEFAULT NULL,
  `lines_of_code_deleted` int(11) DEFAULT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `github_owner` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `course` varchar(45) DEFAULT NULL,
  `team` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `contributors`
--

CREATE TABLE `contributors` (
  `id` binary(16) NOT NULL,
  `login` varchar(255) NOT NULL,
  `w` int(11) NOT NULL,
  `a` int(11) NOT NULL,
  `d` int(11) NOT NULL,
  `c` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

CREATE TABLE `courses` (
  `course` varchar(255) NOT NULL,
  `end_date` date DEFAULT NULL,
  `github_owner` varchar(255) DEFAULT NULL,
  `github_token` varchar(255) DEFAULT NULL,
  `slack_token` varchar(255) DEFAULT NULL,
  `taiga_token` varchar(255) DEFAULT NULL,
  `start_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `github_weight`
--

CREATE TABLE `github_weight` (
  `date` varchar(255) NOT NULL,
  `username` varchar(45) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `course` varchar(45) DEFAULT NULL,
  `team` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `groupobject_members`
--

CREATE TABLE `groupobject_members` (
  `GroupObject_id` varchar(255) NOT NULL,
  `members` varchar(255) DEFAULT NULL,
  `slack_group_members_sequence` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `memberdata`
--

CREATE TABLE `memberdata` (
  `id` int(11) DEFAULT NULL,
  `fullName` varchar(255) DEFAULT NULL,
  `project` varchar(255) DEFAULT NULL,
  `project_slug` varchar(255) DEFAULT NULL,
  `roleName` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `team` varchar(45) DEFAULT NULL,
  `course` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `project`
--

CREATE TABLE `project` (
  `id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `slug` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `slack_auth`
--

CREATE TABLE `slack_auth` (
  `id` int(11) NOT NULL,
  `token` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `slack_channel`
--

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
  `unread_count_display` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `slack_group`
--

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
  `unread_count_display` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `slack_messages`
--

CREATE TABLE `slack_messages` (
  `id` int(11) NOT NULL,
  `ts` double DEFAULT NULL,
  `user` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `slack_messagetotals`
--

CREATE TABLE `slack_messagetotals` (
  `retrievalDate` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `channel_id` varchar(45) NOT NULL,
  `fullName` varchar(45) DEFAULT NULL,
  `course` varchar(45) DEFAULT NULL,
  `team` varchar(45) DEFAULT NULL,
  `messageCount` int(11) DEFAULT 0,
  `display_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `slack_team`
--

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
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `slack_user`
--

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
  `display_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `id` binary(16) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `course` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `team_name` varchar(255) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `github_username` varchar(255) NOT NULL,
  `slack_username` varchar(255) NOT NULL,
  `taiga_username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `taskdata`
--

CREATE TABLE `taskdata` (
  `id` int(11) NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `member_id` bigint(20) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `project` bigint(20) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tasktotals`
--

CREATE TABLE `tasktotals` (
  `retrievalDate` varchar(255) NOT NULL,
  `email` varchar(45) NOT NULL,
  `fullName` varchar(255) DEFAULT '0',
  `project` varchar(255) DEFAULT '0',
  `course` varchar(45) DEFAULT '0',
  `team` varchar(45) DEFAULT '0',
  `tasksClosed` int(11) DEFAULT 0,
  `tasksInProgress` int(11) DEFAULT 0,
  `tasksNew` int(11) DEFAULT 0,
  `tasksOpen` int(11) DEFAULT 0,
  `tasksReadyForTest` int(11) DEFAULT 0,
  `slug` varchar(255) NOT NULL,
  `taiga_username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `teams`
--

CREATE TABLE `teams` (
  `team_name` varchar(255) NOT NULL,
  `course` varchar(255) DEFAULT NULL,
  `github_repo_id` varchar(255) DEFAULT NULL,
  `slack_team_id` varchar(255) DEFAULT NULL,
  `taiga_project_slug` varchar(255) DEFAULT NULL,
  `github_owner` varchar(255) NOT NULL,
  `github_token` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `token`
--

CREATE TABLE `token` (
  `series` varchar(255) NOT NULL,
  `date` datetime DEFAULT NULL,
  `ip_address` varchar(255) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `user_login` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `token`
--

INSERT INTO `token` (`series`, `date`, `ip_address`, `user_agent`, `user_login`, `value`) VALUES
('wjiZxvMfvAOG9gP7Btsgaw==', '2023-02-09 08:47:11', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36', 'test@gmail.com', 'ZEPDRJssTAZWXEtTTlyc9g==');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `e_mail` varchar(255) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `family_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `e_mail`, `enabled`, `family_name`, `first_name`, `language`, `login`, `password`) VALUES
(1, 'tjjohn1asu@gmail.com', b'1', 'Johnson', 'Thomas', 'en', 'tjjohn1asu@gmail.com', '$2a$11$1taNqaK5BL5ITfnDZtq8T.2uPMBhC7fGtCjYRYZJXmty4i.t15rk.'),
(2, 'test@gmail.com', b'1', 'test', 'test', 'en', 'test@gmail.com', '$2a$11$8EHwJJYEVB5KXBZPeXP/x.lt4S79fcPVuUH6krmNsF4LopHliyAJS');

-- --------------------------------------------------------

--
-- Table structure for table `users_authority`
--

CREATE TABLE `users_authority` (
  `id_user` bigint(20) NOT NULL,
  `id_authority` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `users_authority`
--

INSERT INTO `users_authority` (`id_user`, `id_authority`) VALUES
(1, 3),
(2, 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK8xahrcf9t0jbxvvvlxfmyecy8` (`course`);

--
-- Indexes for table `authority`
--
ALTER TABLE `authority`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `channelobject_members`
--
ALTER TABLE `channelobject_members`
  ADD PRIMARY KEY (`ChannelObject_id`,`slack_channel_member_sequence`);

--
-- Indexes for table `channelobject_previous_names`
--
ALTER TABLE `channelobject_previous_names`
  ADD PRIMARY KEY (`ChannelObject_id`,`slack_channel_pn_sequence`);

--
-- Indexes for table `channels`
--
ALTER TABLE `channels`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK6533r8btqr63f9q9l4sqsyaal` (`team_name`);

--
-- Indexes for table `commit_data`
--
ALTER TABLE `commit_data`
  ADD PRIMARY KEY (`date`,`username`);

--
-- Indexes for table `contributors`
--
ALTER TABLE `contributors`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `courses`
--
ALTER TABLE `courses`
  ADD PRIMARY KEY (`course`);

--
-- Indexes for table `github_weight`
--
ALTER TABLE `github_weight`
  ADD PRIMARY KEY (`username`,`date`);

--
-- Indexes for table `groupobject_members`
--
ALTER TABLE `groupobject_members`
  ADD PRIMARY KEY (`GroupObject_id`,`slack_group_members_sequence`);

--
-- Indexes for table `memberdata`
--
ALTER TABLE `memberdata`
  ADD PRIMARY KEY (`email`);

--
-- Indexes for table `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`slug`);

--
-- Indexes for table `slack_auth`
--
ALTER TABLE `slack_auth`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `slack_channel`
--
ALTER TABLE `slack_channel`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `slack_group`
--
ALTER TABLE `slack_group`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `slack_messages`
--
ALTER TABLE `slack_messages`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `slack_messagetotals`
--
ALTER TABLE `slack_messagetotals`
  ADD PRIMARY KEY (`retrievalDate`,`email`,`channel_id`);

--
-- Indexes for table `slack_team`
--
ALTER TABLE `slack_team`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `slack_user`
--
ALTER TABLE `slack_user`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKlb5s7aexy0a5460iamau0qlh0` (`team_name`);

--
-- Indexes for table `taskdata`
--
ALTER TABLE `taskdata`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tasktotals`
--
ALTER TABLE `tasktotals`
  ADD PRIMARY KEY (`retrievalDate`,`email`);

--
-- Indexes for table `teams`
--
ALTER TABLE `teams`
  ADD PRIMARY KEY (`team_name`),
  ADD KEY `FKahkmfgmwesgwhj2iu5yhhhmie` (`course`);

--
-- Indexes for table `token`
--
ALTER TABLE `token`
  ADD PRIMARY KEY (`series`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users_authority`
--
ALTER TABLE `users_authority`
  ADD PRIMARY KEY (`id_user`,`id_authority`),
  ADD KEY `FKlp67kinuubrwwt0v1ss11iwci` (`id_authority`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `slack_messages`
--
ALTER TABLE `slack_messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `channelobject_members`
--
ALTER TABLE `channelobject_members`
  ADD CONSTRAINT `FKn4noo14wxpw808c8potiae14n` FOREIGN KEY (`ChannelObject_id`) REFERENCES `slack_channel` (`id`);

--
-- Constraints for table `channelobject_previous_names`
--
ALTER TABLE `channelobject_previous_names`
  ADD CONSTRAINT `FKn0l3nf7rqlbw0rwmlvofr61fg` FOREIGN KEY (`ChannelObject_id`) REFERENCES `slack_channel` (`id`);

--
-- Constraints for table `channels`
--
ALTER TABLE `channels`
  ADD CONSTRAINT `FK6533r8btqr63f9q9l4sqsyaal` FOREIGN KEY (`team_name`) REFERENCES `teams` (`team_name`);

--
-- Constraints for table `groupobject_members`
--
ALTER TABLE `groupobject_members`
  ADD CONSTRAINT `FKsti6v5q7v6u4bir86jfgcr5oe` FOREIGN KEY (`GroupObject_id`) REFERENCES `slack_group` (`id`);

--
-- Constraints for table `students`
--
ALTER TABLE `students`
  ADD CONSTRAINT `FKlb5s7aexy0a5460iamau0qlh0` FOREIGN KEY (`team_name`) REFERENCES `teams` (`team_name`);

--
-- Constraints for table `teams`
--
ALTER TABLE `teams`
  ADD CONSTRAINT `FKahkmfgmwesgwhj2iu5yhhhmie` FOREIGN KEY (`course`) REFERENCES `courses` (`course`);

--
-- Constraints for table `users_authority`
--
ALTER TABLE `users_authority`
  ADD CONSTRAINT `FKa3oaqq69l8o79g23ipfd1eomm` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKlp67kinuubrwwt0v1ss11iwci` FOREIGN KEY (`id_authority`) REFERENCES `authority` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
